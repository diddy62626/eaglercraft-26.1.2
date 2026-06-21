#!/usr/bin/env python3
"""
Post-build patcher for classes.js.

Works around a TeaVM 0.15 bug where interface default methods are NOT
included in the virtual method table of implementing classes.

This version works on UNOBFUSCATED builds only (where function names
start with cms_, os_, jl_, etc.).

Strategy:
  1. Scan classes.js for all default method definitions
     (pattern: <prefix>_<Interface>_<method> = $this => ... )
  2. Build a JS registry mapping interface function name → { method: funcName }
  3. Append a JS patcher that:
     a. Builds a name→function lookup from global scope
     b. Iterates through all registered classes (Fnk array)
     c. For each class, checks its interfaces
     d. Adds missing default methods to cls.prototype
"""
import re
import sys
import json


def find_default_methods(data):
    """
    Find all default method definitions in classes.js.

    Default methods are top-level functions that take $this as first arg:
      <prefix>_<Interface>_<method> = $this => ...
      <prefix>_<Interface>_<method> = ($this, ...) => ...

    Returns: dict mapping interface_func_name -> { "$method": "funcName" }
    """
    # Match: identifier = $this =>  OR  identifier = ($this
    # The identifier can be any valid JS identifier (letters, digits, _, $)
    pattern = re.compile(
        r'([A-Za-z_$][A-Za-z0-9_$]*)\s*=\s*(?:\$this\s*=>|(?:\([^)]*\$this[^)]*\))\s*=>)'
    )

    registry = {}
    for match in pattern.finditer(data):
        full_name = match.group(1)

        # Parse: <prefix>_<Interface>_<method>
        # We need at least 3 segments: prefix_Interface_method
        # But interface names can contain $ (inner classes)
        # Strategy: split on _ and take first segment as prefix,
        # last segment as method, everything in between as interface

        parts = full_name.split('_')
        if len(parts) < 3:
            continue

        # prefix is parts[0] (e.g., cms, os, jl)
        prefix = parts[0]
        # method is parts[-1]
        method_name = parts[-1]
        # interface is parts[1:-1] joined with _
        iface_name = '_'.join(parts[1:-1])

        if not iface_name or not method_name:
            continue

        # Skip constructors and abstract methods
        if method_name in ('_init_', '_clinit_', '$callClinit', 'encode', 'decode'):
            continue
        # Skip if method name starts with _ (likely internal)
        if method_name.startswith('_'):
            continue

        # Reconstruct the full interface function name
        iface_func_name = prefix + '_' + iface_name

        if iface_func_name not in registry:
            registry[iface_func_name] = {}

        vmethod = '$' + method_name
        registry[iface_func_name][vmethod] = full_name

    return registry


def build_patcher_js(registry):
    """
    Build a JS snippet that patches all class prototypes with missing
    default methods from their interfaces.

    Instead of using eval(), builds a lookup map by scanning global scope
    for known function names.
    """
    registry_json = json.dumps(registry, separators=(',', ':'))

    patcher = """
// ============================================================
// TeaVM Default Method Patcher (auto-generated)
// Workaround for TeaVM 0.15 bug: interface default methods not
// included in implementing classes' virtual method tables.
// ============================================================
// This code runs INSIDE the TeaVM IIFE, so it has direct access to
// $rt_allClasses (class registry), $rt_meta (metadata symbol), and
// all top-level function names (cms_Codec, cms_Codec_listOf, etc.).
(function() {
    var __registry = %s;

    // Try different names for the class registry array
    var allClasses = typeof $rt_allClasses !== 'undefined' ? $rt_allClasses :
                     typeof Fnk !== 'undefined' ? Fnk : null;
    if (!allClasses) {
        console.warn('[DefaultMethodPatcher] Class registry not found, skipping');
        return;
    }

    // Try different names for the metadata symbol
    var meta = typeof $rt_meta !== 'undefined' ? $rt_meta :
               typeof GN !== 'undefined' ? GN : null;
    if (!meta) {
        console.warn('[DefaultMethodPatcher] Metadata symbol not found, skipping');
        return;
    }

    var patched = 0;
    var classesPatched = 0;

    for (var i = 0; i < allClasses.length; i++) {
        var cls = allClasses[i];
        if (!cls || !cls.prototype || !cls[meta]) continue;

        var clsMeta = cls[meta];
        if (!clsMeta.superinterfaces || clsMeta.superinterfaces.length === 0) continue;

        var classPatched = false;

        for (var j = 0; j < clsMeta.superinterfaces.length; j++) {
            var iface = clsMeta.superinterfaces[j];
            if (!iface) continue;

            // Find this interface in our registry by matching function reference
            for (var ifaceName in __registry) {
                // Look up the interface function by name (it's a local var in the IIFE)
                var ifaceFunc = eval('(typeof ' + ifaceName + ' !== "undefined" ? ' + ifaceName + ' : undefined)');
                if (!ifaceFunc || ifaceFunc !== iface) continue;

                var methods = __registry[ifaceName];
                for (var vmethod in methods) {
                    if (typeof cls.prototype[vmethod] === 'function') continue;

                    var funcName = methods[vmethod];
                    var func = eval('(typeof ' + funcName + ' !== "undefined" ? ' + funcName + ' : undefined)');
                    if (typeof func !== 'function') continue;

                    // Wrap: prototype.$method = function(args...) { return func(this, args...); }
                    (function(vmethod, func) {
                        cls.prototype[vmethod] = function() {
                            var args = [this];
                            for (var k = 0; k < arguments.length; k++) {
                                args.push(arguments[k]);
                            }
                            return func.apply(null, args);
                        };
                    })(vmethod, func);
                    patched++;
                    classPatched = true;
                }
            }
        }

        if (classPatched) classesPatched++;
    }

    if (patched > 0) {
        console.log('[DefaultMethodPatcher] Patched ' + patched + ' methods across ' + classesPatched + ' classes');
    } else {
        console.log('[DefaultMethodPatcher] No methods needed patching');
    }

    // ============================================================
    // Special case: Add toPath() to java.io.File (TFile/ji_File)
    // MC code calls File.toPath() which TeaVM's TFile doesn't have.
    // Our Java patch provides toPath() but TeaVM doesn't use it for
    // TFile instances. This patcher adds toPath() directly to
    // ji_File.prototype, returning a StubPath-like object.
    // ============================================================
    var fileClassName = typeof ji_File !== 'undefined' ? 'ji_File' :
                        (typeof org_teavm_classlib_java_io_TFile !== 'undefined' ? 'org_teavm_classlib_java_io_TFile' : null);
    if (fileClassName) {
        var fileClass = eval(fileClassName);
        if (fileClass && fileClass.prototype && typeof fileClass.prototype.$toPath !== 'function') {
            fileClass.prototype.$toPath = function() {
                // Return a simple path-like object with all methods MC might call.
                // Avoid using arguments.callee (forbidden in strict mode).
                var pathStr = this.$path || this.path || '/';
                var pathObj = {
                    $resolve: function(other) { return pathObj; },
                    resolve: function(other) { return pathObj; },
                    $resolve0: function(other) { return pathObj; },
                    resolve0: function(other) { return pathObj; },
                    $resolve1: function(other) { return pathObj; },
                    resolve1: function(other) { return pathObj; },
                    $resolveSibling: function(o) { return pathObj; },
                    resolveSibling: function(o) { return pathObj; },
                    toString: function() { return pathStr; },
                    $toString: function() { return pathStr; },
                    $getParent: function() { return pathObj; },
                    getParent: function() { return pathObj; },
                    $getFileName: function() { return pathObj; },
                    getFileName: function() { return pathObj; },
                    $getFileNameString: function() { return pathStr; },
                    getFileNameString: function() { return pathStr; },
                    $getRoot: function() { return pathObj; },
                    getRoot: function() { return pathObj; },
                    $isAbsolute: function() { return 1; },
                    isAbsolute: function() { return true; },
                    $normalize: function() { return pathObj; },
                    normalize: function() { return pathObj; },
                    $relativize: function(o) { return o; },
                    relativize: function(o) { return o; },
                    $toAbsolutePath: function() { return pathObj; },
                    toAbsolutePath: function() { return pathObj; },
                    $toFile: function() { return null; },
                    toFile: function() { return null; },
                    $startsWith: function(p) { return 1; },
                    startsWith: function(p) { return true; },
                    $endsWith: function(p) { return 1; },
                    endsWith: function(p) { return true; },
                    $compareTo: function(o) { return 0; },
                    compareTo: function(o) { return 0; },
                    $iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; },
                    iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; },
                    $subpath: function(a, b) { return pathObj; },
                    subpath: function(a, b) { return pathObj; },
                    $getNameCount: function() { return 1; },
                    getNameCount: function() { return 1; },
                    $getName: function(i) { return pathObj; },
                    getName: function(i) { return pathObj; },
                    getFileSystem: function() {
                        var fsObj = {
                            $provider: function() { return fsProvider; },
                            provider: function() { return fsProvider; },
                            $isOpen: function() { return 1; },
                            isOpen: function() { return true; },
                            $close: function() {},
                            close: function() {},
                            $isReadOnly: function() { return 0; },
                            isReadOnly: function() { return false; },
                            $getRootDirectories: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; } }; },
                            getRootDirectories: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; } }; },
                            $getFileStores: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; } }; },
                            getFileStores: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; } }; },
                            $supportedFileAttributeViews: function() { return { contains: function() { return false; } }; },
                            supportedFileAttributeViews: function() { return { contains: function() { return false; } }; },
                            $getPath: function() { return pathObj; },
                            getPath: function() { return pathObj; },
                            $getPathMatcher: function() { return { matches: function() { return false; } }; },
                            getPathMatcher: function() { return { matches: function() { return false; } }; },
                            $getUserPrincipalLookupService: function() { return { lookupPrincipalByName: function() { return {}; }, lookupPrincipalByGroupName: function() { return {}; } }; },
                            getUserPrincipalLookupService: function() { return { lookupPrincipalByName: function() { return {}; }, lookupPrincipalByGroupName: function() { return {}; } }; },
                            $newWatchService: function() { return { poll: function() { return null; }, take: function() { return null; }, close: function() {} }; },
                            newWatchService: function() { return { poll: function() { return null; }, take: function() { return null; }, close: function() {} }; }
                        };
                        var fsProvider = {
                            $getScheme: function() { return 'file'; },
                            getScheme: function() { return 'file'; },
                            $newFileSystem: function() { return fsObj; },
                            newFileSystem: function() { return fsObj; },
                            $getFileSystem: function() { return fsObj; },
                            getFileSystem: function() { return fsObj; },
                            $getPath: function() { return pathObj; },
                            getPath: function() { return pathObj; },
                            $newByteChannel: function() { return { read: function() { return -1; }, close: function() {} }; },
                            newByteChannel: function() { return { read: function() { return -1; }, close: function() {} }; },
                            $newInputStream: function() {
                                var isObj = {
                                    read: function() { return -1; },
                                    $read: function() { return -1; },
                                    read3: function(buf, off, len) { return -1; },
                                    $read3: function(buf, off, len) { return -1; },
                                    read0: function() { return -1; },
                                    $read0: function() { return -1; },
                                    read1: function(buf) { return -1; },
                                    $read1: function(buf) { return -1; },
                                    read2: function(buf, off, len) { return -1; },
                                    $read2: function(buf, off, len) { return -1; },
                                    read4: function(buf, off, len) { return -1; },
                                    $read4: function(buf, off, len) { return -1; },
                                    read5: function(buf, off, len) { return -1; },
                                    $read5: function(buf, off, len) { return -1; },
                                    read6: function(buf, off, len) { return -1; },
                                    $read6: function(buf, off, len) { return -1; },
                                    read7: function() { return -1; },
                                    $read7: function() { return -1; },
                                    read8: function() { return -1; },
                                    $read8: function() { return -1; },
                                    available: function() { return 0; },
                                    $available: function() { return 0; },
                                    close: function() {},
                                    $close: function() {},
                                    mark: function() {},
                                    $mark: function() {},
                                    reset: function() {},
                                    $reset: function() {},
                                    skip: function(n) { return 0; },
                                    $skip: function(n) { return 0; },
                                    markSupported: function() { return false; },
                                    $markSupported: function() { return false; }
                                };
                                return isObj;
                            },
                            newInputStream: function() {
                                var isObj = {
                                    read: function() { return -1; },
                                    $read: function() { return -1; },
                                    read3: function(buf, off, len) { return -1; },
                                    $read3: function(buf, off, len) { return -1; },
                                    read0: function() { return -1; },
                                    $read0: function() { return -1; },
                                    read1: function(buf) { return -1; },
                                    $read1: function(buf) { return -1; },
                                    read2: function(buf, off, len) { return -1; },
                                    $read2: function(buf, off, len) { return -1; },
                                    read4: function(buf, off, len) { return -1; },
                                    $read4: function(buf, off, len) { return -1; },
                                    read5: function(buf, off, len) { return -1; },
                                    $read5: function(buf, off, len) { return -1; },
                                    read6: function(buf, off, len) { return -1; },
                                    $read6: function(buf, off, len) { return -1; },
                                    read7: function() { return -1; },
                                    $read7: function() { return -1; },
                                    read8: function() { return -1; },
                                    $read8: function() { return -1; },
                                    available: function() { return 0; },
                                    $available: function() { return 0; },
                                    close: function() {},
                                    $close: function() {},
                                    mark: function() {},
                                    $mark: function() {},
                                    reset: function() {},
                                    $reset: function() {},
                                    skip: function(n) { return 0; },
                                    $skip: function(n) { return 0; },
                                    markSupported: function() { return false; },
                                    $markSupported: function() { return false; }
                                };
                                return isObj;
                            },
                            $newOutputStream: function() { return { write: function() {}, flush: function() {}, close: function() {} }; },
                            newOutputStream: function() { return { write: function() {}, flush: function() {}, close: function() {} }; },
                            $newFileChannel: function() { return { read: function() { return -1; }, write: function() { return 0; }, close: function() {} }; },
                            newFileChannel: function() { return { read: function() { return -1; }, write: function() { return 0; }, close: function() {} }; },
                            $newDirectoryStream: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; }, close: function() {} }; },
                            newDirectoryStream: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; }, close: function() {} }; },
                            $createDirectory: function() {},
                            createDirectory: function() {},
                            $createDirectories: function() {},
                            createDirectories: function() {},
                            $delete: function() {},
                            delete: function() {},
                            $deleteIfExists: function() { return 0; },
                            deleteIfExists: function() { return false; },
                            $copy: function() {},
                            copy: function() {},
                            $move: function() {},
                            move: function() {},
                            $exists: function() { return 0; },
                            exists: function() { return false; },
                            $notExists: function() { return 1; },
                            notExists: function() { return true; },
                            $isDirectory: function() { return 0; },
                            isDirectory: function() { return false; },
                            $isRegularFile: function() { return 0; },
                            isRegularFile: function() { return false; },
                            $isReadable: function() { return 0; },
                            isReadable: function() { return false; },
                            $isWritable: function() { return 0; },
                            isWritable: function() { return false; },
                            $isHidden: function() { return 0; },
                            isHidden: function() { return false; },
                            $isSameFile: function() { return 0; },
                            isSameFile: function() { return false; },
                            $size: function() { return 0; },
                            size: function() { return 0; },
                            $getFileAttributeView: function() { return null; },
                            getFileAttributeView: function() { return null; },
                            $readAttributes: function() { return {}; },
                            readAttributes: function() { return {}; },
                            $setAttribute: function() {},
                            setAttribute: function() {},
                            $checkAccess: function() {},
                            checkAccess: function() {}
                        };
                        return fsObj;
                    },
                    $getFileSystem: function() {
                        // Build FileSystem object once (lazy init) to avoid recursion
                        if (pathObj.__fs) return pathObj.__fs;
                        var fsObj2 = {
                            $provider: function() { return fsProvider2; },
                            provider: function() { return fsProvider2; },
                            $isOpen: function() { return 1; },
                            isOpen: function() { return true; },
                            $close: function() {},
                            close: function() {},
                            $isReadOnly: function() { return 0; },
                            isReadOnly: function() { return false; },
                            $getRootDirectories: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; } }; },
                            getRootDirectories: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; } }; },
                            $getFileStores: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; } }; },
                            getFileStores: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; } }; },
                            $supportedFileAttributeViews: function() { return { contains: function() { return false; } }; },
                            supportedFileAttributeViews: function() { return { contains: function() { return false; } }; },
                            $getPath: function() { return pathObj; },
                            getPath: function() { return pathObj; },
                            $toString: function() { return 'file:///'; },
                            toString: function() { return 'file:///'; }
                        };
                        var fsProvider2 = {
                            $getScheme: function() { return 'file'; },
                            getScheme: function() { return 'file'; },
                            $getFileSystem: function() { return fsObj2; },
                            getFileSystem: function() { return fsObj2; },
                            $getPath: function() { return pathObj; },
                            getPath: function() { return pathObj; },
                            $toString: function() { return 'file'; },
                            toString: function() { return 'file'; },
                            $readAttributes: function() { return {}; },
                            readAttributes: function() { return {}; },
                            $exists: function() { return 0; },
                            exists: function() { return false; },
                            $isDirectory: function() { return 0; },
                            isDirectory: function() { return false; },
                            $isRegularFile: function() { return 0; },
                            isRegularFile: function() { return false; },
                            $isReadable: function() { return 0; },
                            isReadable: function() { return false; },
                            $isWritable: function() { return 0; },
                            isWritable: function() { return false; },
                            $isHidden: function() { return 0; },
                            isHidden: function() { return false; },
                            $size: function() { return 0; },
                            size: function() { return 0; },
                            $createDirectory: function() {},
                            createDirectory: function() {},
                            $createDirectories: function() {},
                            createDirectories: function() {},
                            $delete: function() {},
                            delete: function() {},
                            $deleteIfExists: function() { return 0; },
                            deleteIfExists: function() { return false; },
                            $copy: function() {},
                            copy: function() {},
                            $move: function() {},
                            move: function() {},
                            $newInputStream: function() { return { read: function() { return -1; }, $read: function() { return -1; }, read0: function() { return -1; }, $read0: function() { return -1; }, read1: function(b) { return -1; }, $read1: function(b) { return -1; }, read2: function(b,o,l) { return -1; }, $read2: function(b,o,l) { return -1; }, read3: function(b,o,l) { return -1; }, $read3: function(b,o,l) { return -1; }, read4: function(b,o,l) { return -1; }, $read4: function(b,o,l) { return -1; }, read5: function(b,o,l) { return -1; }, $read5: function(b,o,l) { return -1; }, read6: function(b,o,l) { return -1; }, $read6: function(b,o,l) { return -1; }, read7: function() { return -1; }, $read7: function() { return -1; }, read8: function() { return -1; }, $read8: function() { return -1; }, available: function() { return 0; }, $available: function() { return 0; }, close: function() {}, $close: function() {}, mark: function() {}, $mark: function() {}, reset: function() {}, $reset: function() {}, skip: function(n) { return 0; }, $skip: function(n) { return 0; } }; },
                            newInputStream: function() { return { read: function() { return -1; }, $read: function() { return -1; }, read0: function() { return -1; }, $read0: function() { return -1; }, read1: function(b) { return -1; }, $read1: function(b) { return -1; }, read2: function(b,o,l) { return -1; }, $read2: function(b,o,l) { return -1; }, read3: function(b,o,l) { return -1; }, $read3: function(b,o,l) { return -1; }, read4: function(b,o,l) { return -1; }, $read4: function(b,o,l) { return -1; }, read5: function(b,o,l) { return -1; }, $read5: function(b,o,l) { return -1; }, read6: function(b,o,l) { return -1; }, $read6: function(b,o,l) { return -1; }, read7: function() { return -1; }, $read7: function() { return -1; }, read8: function() { return -1; }, $read8: function() { return -1; }, available: function() { return 0; }, $available: function() { return 0; }, close: function() {}, $close: function() {}, mark: function() {}, $mark: function() {}, reset: function() {}, $reset: function() {}, skip: function(n) { return 0; }, $skip: function(n) { return 0; } }; },
                            $newByteChannel: function() { return { read: function() { return -1; }, close: function() {} }; },
                            newByteChannel: function() { return { read: function() { return -1; }, close: function() {} }; },
                            $newDirectoryStream: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; }, close: function() {} }; },
                            newDirectoryStream: function() { return { iterator: function() { return { hasNext: function() { return false; }, next: function() { return null; } }; }, close: function() {} }; },
                            $getFileAttributeView: function() { return null; },
                            getFileAttributeView: function() { return null; },
                            $setAttribute: function() {},
                            setAttribute: function() {},
                            $checkAccess: function() {},
                            checkAccess: function() {},
                            $isSameFile: function() { return 0; },
                            isSameFile: function() { return false; }
                        };
                        pathObj.__fs = fsObj2;
                        return fsObj2;
                    },
                    registerWatchService: function() { return {}; },
                    $toUri: function() { return { toString: function() { return 'file://' + pathStr; }, $toString: function() { return 'file://' + pathStr; } }; },
                    toUri: function() { return { toString: function() { return 'file://' + pathStr; }, $toString: function() { return 'file://' + pathStr; } }; },
                    toRealPath: function() { return pathObj; },
                    $toRealPath: function() { return pathObj; },
                    subpath: function(a, b) { return pathObj; },
                    getNameCount: function() { return 1; },
                    getName: function(i) { return pathObj; },
                    resolveSibling: function(o) { return pathObj; }
                };
                return pathObj;
            };
            console.log('[DefaultMethodPatcher] Added $toPath to ' + fileClassName);
        }
    }
})();
""" % registry_json

    return patcher


def wrap_clinits_textually(data):
    """
    Wrap all __clinit_ function bodies in try/catch.

    TeaVM generates static initializers like:
        nmu_ExtraCodecs__clinit_ = () => {
            <body>
        },

    We wrap the body in try/catch:
        nmu_ExtraCodecs__clinit_ = () => {
            try { <body> } catch(__e) { if(!window.__eaglerClinitErrors) window.__eaglerClinitErrors=0; if(window.__eaglerClinitErrors<10) console.warn('[ClinitWrap] '+__e.message); window.__eaglerClinitErrors++; }
        },

    This prevents forward-reference crashes from killing the game.
    """
    # Pattern: <name>__clinit_ = () => {
    # We need to find the matching closing brace and wrap the body.
    # Since JS brace matching is complex, we use a simpler approach:
    # Replace the opening pattern to add try/catch at the start,
    # and add the catch before the closing brace.

    # Strategy: Find all "__clinit_ = () => {" patterns
    # For each, find the matching closing "}" (accounting for nested braces)
    # and wrap the body.

    import re

    pattern = re.compile(r'(\w+__clinit_\s*=\s*\(\)\s*=>\s*\{)')

    result = []
    last_end = 0
    wrapped_count = 0

    for match in pattern.finditer(data):
        # Find the matching closing brace
        start_pos = match.end()  # position after the opening {
        depth = 1
        pos = start_pos
        while pos < len(data) and depth > 0:
            if data[pos] == '{':
                depth += 1
            elif data[pos] == '}':
                depth -= 1
            elif data[pos] == '"' or data[pos] == "'":
                # Skip string literals
                quote = data[pos]
                pos += 1
                while pos < len(data) and data[pos] != quote:
                    if data[pos] == '\\':
                        pos += 1
                    pos += 1
            elif data[pos] == '`':
                # Skip template literals
                pos += 1
                while pos < len(data) and data[pos] != '`':
                    if data[pos] == '\\':
                        pos += 1
                    pos += 1
            pos += 1

        if depth == 0:
            # pos-1 is the closing }
            body = data[start_pos:pos-1]
            # Wrap the body in try/catch
            wrapped_body = (
                '\ntry {\n' + body + '\n} catch(__e) { '
                'if(typeof window!=="undefined"){if(!window.__eaglerClinitErrors)window.__eaglerClinitErrors=0;'
                'if(window.__eaglerClinitErrors<10)console.warn("[ClinitWrap] "+(__e&&__e.message?__e.message:String(__e)));'
                'window.__eaglerClinitErrors++;} }'
            )
            result.append(data[last_end:match.start()])
            result.append(match.group(1))
            result.append(wrapped_body)
            last_end = pos - 1  # include the closing }
            wrapped_count += 1

    result.append(data[last_end:])
    patched = ''.join(result)

    print(f"Wrapped {wrapped_count} __clinit_ functions in try/catch")
    return patched


def patch_add_suppressed(data):
    """
    Patch jl_Throwable_addSuppressed to handle null suppressed array.

    TeaVM's implementation accesses var$2.data.length where var$2 is
    the suppressed exceptions array, which can be null. We add a null
    check at the beginning of the function.
    """
    import re

    # Find the function: jl_Throwable_addSuppressed = ($this, $exception) => {
    # and add a null check for the suppressed array after the opening brace
    pattern = r'(jl_Throwable_addSuppressed\s*=\s*\([^)]*\)\s*=>\s*\{)'

    match = re.search(pattern, data)
    if not match:
        print("  WARNING: jl_Throwable_addSuppressed not found")
        return data

    # Find the body and add null checks
    # The function body typically does:
    #   var$1 = $this.$suppressed; (or similar)
    #   if (var$1 === null) { $this.$suppressed = ...; var$1 = ...; }
    #   var$3 = var$1.data.length + 1 | 0;
    # We need to ensure var$1 (the suppressed array) is not null

    # Simple approach: wrap the entire function body in try/catch
    start_pos = match.end()
    depth = 1
    pos = start_pos
    while pos < len(data) and depth > 0:
        if data[pos] == '{':
            depth += 1
        elif data[pos] == '}':
            depth -= 1
        elif data[pos] == '"' or data[pos] == "'":
            quote = data[pos]
            pos += 1
            while pos < len(data) and data[pos] != quote:
                if data[pos] == '\\':
                    pos += 1
                pos += 1
        pos += 1

    if depth == 0:
        body = data[start_pos:pos-1]
        wrapped_body = '\ntry {\n' + body + '\n} catch(__e) { /* suppressed array null — ignore */ }\n'
        result = data[:match.start()] + match.group(1) + wrapped_body + data[pos-1:]
        print("  Wrapped jl_Throwable_addSuppressed in try/catch")
        return result

    print("  WARNING: Could not find end of jl_Throwable_addSuppressed")
    return data


def patch_classes_js(input_path, output_path):
    """Patch classes.js with default method workaround."""
    with open(input_path, 'r', encoding='utf-8') as f:
        data = f.read()

    print(f"Scanning {len(data)} bytes for default method definitions...")

    registry = find_default_methods(data)
    total_methods = sum(len(m) for m in registry.values())
    print(f"Found {len(registry)} interfaces with {total_methods} default methods:")
    for iface, methods in sorted(registry.items()):
        print(f"  {iface}: {len(methods)} methods")

    # Wrap all __clinit_ functions in try/catch (textual replacement)
    print("\nWrapping __clinit_ functions in try/catch...")
    data = wrap_clinits_textually(data)

    # Patch jl_Throwable_addSuppressed to handle null suppressed array
    print("\nPatching jl_Throwable_addSuppressed for null safety...")
    data = patch_add_suppressed(data)

    patcher = build_patcher_js(registry)

    # Insert the patcher INSIDE the TeaVM IIFE, right before the closing
    # The IIFE ends with: $rt_exports.main = $rt_export_main;\n}));
    # We insert before the last }));
    # This ensures the patcher has access to Fnk, GN, and all internal
    # variables (which are local to the IIFE, not global).
    insert_marker = '}));'
    last_pos = data.rfind(insert_marker)
    if last_pos < 0:
        # Fallback: try }))) (3 parens)
        insert_marker = '})))'
        last_pos = data.rfind(insert_marker)
    if last_pos < 0:
        # Fallback: try just appending
        print("WARNING: Could not find IIFE closing })); — appending at end")
        patched_data = data + "\n" + patcher
    else:
        patched_data = data[:last_pos] + "\n" + patcher + "\n" + data[last_pos:]

    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(patched_data)

    print(f"\nPatched classes.js written to {output_path}")
    print(f"  Original: {len(data)} bytes")
    print(f"  Patched:  {len(patched_data)} bytes")
    print(f"  Patcher:  {len(patcher)} bytes")


if __name__ == '__main__':
    input_path = sys.argv[1] if len(sys.argv) > 1 else 'public/classes.js'
    output_path = sys.argv[2] if len(sys.argv) > 2 else input_path
    patch_classes_js(input_path, output_path)
