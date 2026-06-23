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
// TeaVM Default Method Patcher (hybrid: prototype + registry)
// ============================================================
(function() {
    var __registry = %s;
    var allClasses = null;
    if (typeof $rt_allClasses !== 'undefined' && Array.isArray($rt_allClasses)) allClasses = $rt_allClasses;
    else if (typeof FnY !== 'undefined' && Array.isArray(FnY)) allClasses = FnY;
    else if (typeof Fnk !== 'undefined' && Array.isArray(Fnk)) allClasses = Fnk;
    if (!allClasses) { console.warn('[DefaultMethodPatcher] Class registry not found'); return; }
    var meta = typeof $rt_meta !== 'undefined' ? $rt_meta :
               typeof GN !== 'undefined' ? GN : null;
    if (!meta) { console.warn('[DefaultMethodPatcher] Metadata symbol not found'); return; }
    console.log('[DefaultMethodPatcher] count=' + allClasses.length);

    var __funcs = {};
    for (var __n in __registry) {
        try { __funcs[__n] = eval(__n); } catch(e) {}
        for (var __m in __registry[__n]) {
            try { __funcs[__registry[__n][__m]] = eval(__registry[__n][__m]); } catch(e) {}
        }
    }

    var patched = 0, classesPatched = 0;
    for (var i = 0; i < allClasses.length; i++) {
        var cls = allClasses[i];
        if (!cls || !cls.prototype || !cls[meta]) continue;
        var clsMeta = cls[meta];
        if (!clsMeta.superinterfaces || clsMeta.superinterfaces.length === 0) continue;
        var classPatched = false;
        for (var j = 0; j < clsMeta.superinterfaces.length; j++) {
            var iface = clsMeta.superinterfaces[j];
            if (!iface) continue;

            // Method 1: Copy from interface prototype
            var proto = iface.prototype;
            for (var key in proto) {
                if (typeof proto[key] === 'function' && key !== 'constructor') {
                    if (!Object.prototype.hasOwnProperty.call(cls.prototype, key)) {
                        (function(key, fn) {
                            cls.prototype[key] = function() {
                                var args = [this];
                                for (var k = 0; k < arguments.length; k++) args.push(arguments[k]);
                                return fn.apply(null, args);
                            };
                        })(key, proto[key]);
                        patched++; classPatched = true;
                    }
                }
            }

            // Method 2: Copy from registry (for DCE'd methods not on prototype)
            for (var ifaceName in __registry) {
                var ifaceFunc = __funcs[ifaceName];
                if (!ifaceFunc || ifaceFunc !== iface) continue;
                var methods = __registry[ifaceName];
                for (var vmethod in methods) {
                    if (!Object.prototype.hasOwnProperty.call(cls.prototype, vmethod)) {
                        var func = __funcs[methods[vmethod]];
                        if (typeof func === 'function') {
                            (function(vmethod, func) {
                                cls.prototype[vmethod] = function() {
                                    var args = [this];
                                    for (var k = 0; k < arguments.length; k++) args.push(arguments[k]);
                                    return func.apply(null, args);
                                };
                            })(vmethod, func);
                            patched++; classPatched = true;
                        }
                    }
                }
            }
        }
        if (classPatched) classesPatched++;
    }
    console.log('[DefaultMethodPatcher] Patched ' + patched + ' methods across ' + classesPatched + ' classes');
})();
""" % registry_json

    return patcher
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


def patch_file_channel_open(data):
    """
    Patch jnc_FileChannel_open to return a fake FileChannel instead of
    throwing IOException("Cannot open file channel in browser").

    The original implementation always throws. We replace it with a
    function that returns a fake channel object with read/write/close
    methods that do nothing.
    """
    import re

    # Find the function: jnc_FileChannel_open = (var$1, var$2) => { ... throw ... };
    pattern = r'(jnc_FileChannel_open\s*=\s*\([^)]*\)\s*=>\s*\{)'

    match = re.search(pattern, data)
    if not match:
        print("  WARNING: jnc_FileChannel_open not found")
        return data

    # Find the matching closing brace
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
        # Replace the body with a fake channel return
        fake_body = """
    // PATCHED: Return fake FileChannel instead of throwing IOException.
    // MC's DownloadQueue needs a FileChannel for persistent storage.
    // In the browser, we don't have real file channels, so return a
    // fake one that does nothing (all reads return -1, writes no-op).
    var fakeChannel = {
        $read: function() { return -1; },
        read: function() { return -1; },
        $read0: function() { return -1; },
        read0: function() { return -1; },
        $read1: function(b) { return -1; },
        read1: function(b) { return -1; },
        $read2: function(b,o,l) { return -1; },
        read2: function(b,o,l) { return -1; },
        $read3: function(b,o,l) { return -1; },
        read3: function(b,o,l) { return -1; },
        $write: function() { return 0; },
        write: function() { return 0; },
        $write0: function() { return 0; },
        write0: function() { return 0; },
        $write1: function(b) { return 0; },
        write1: function(b) { return 0; },
        $close: function() {},
        close: function() {},
        $isOpen: function() { return 1; },
        isOpen: function() { return true; },
        $position: function() { return 0; },
        position: function() { return 0; },
        $position0: function(p) { return this; },
        position0: function(p) { return this; },
        $size: function() { return 0; },
        size: function() { return 0; },
        $truncate: function(s) { return this; },
        truncate: function(s) { return this; },
        $force: function() {},
        force: function() {},
        $lock: function() { return this; },
        lock: function() { return this; },
        $tryLock: function() { return this; },
        tryLock: function() { return this; },
        $map: function() { return null; },
        map: function() { return null; },
        $transferFrom: function() { return 0; },
        transferFrom: function() { return 0; },
        $transferTo: function() { return 0; },
        transferTo: function() { return 0; },
        $write2: function(b,p) { return 0; },
        write2: function(b,p) { return 0; },
        $read4: function(b,p) { return -1; },
        read4: function(b,p) { return -1; }
    };
    return fakeChannel;
"""
        result = data[:match.start()] + match.group(1) + fake_body + data[pos-1:]
        print("  Replaced jnc_FileChannel_open with fake channel return")
        return result

    print("  WARNING: Could not find end of jnc_FileChannel_open")
    return data


def patch_data_fixer(data):
    """
    Patch nmud_DataFixers_getDataFixer to return a dummy fixer when
    DATA_FIXER is null (because the clinit failed and was caught).

    The original: return nmud_DataFixers_DATA_FIXER.$fixerUpper0;
    Patched: return (nmud_DataFixers_DATA_FIXER && nmud_DataFixers_DATA_FIXER.$fixerUpper0) || {};
    """
    old = 'return nmud_DataFixers_DATA_FIXER.$fixerUpper0;'
    new = 'return (nmud_DataFixers_DATA_FIXER && nmud_DataFixers_DATA_FIXER.$fixerUpper0) || {};'
    if old in data:
        data = data.replace(old, new)
        print("  Patched getDataFixer with null safety")
    else:
        print("  WARNING: getDataFixer pattern not found")
    return data


def patch_shared_constants(data):
    """
    Patch SharedConstants.getCurrentVersion to return a dummy version
    when CURRENT_VERSION is null (because the clinit failed).

    Original:
        var$1 = nm_SharedConstants_CURRENT_VERSION;
        if (var$1 !== null)
            return var$1;
        var$2 = new jl_IllegalStateException;
        jl_Throwable__init_(var$2, $rt_s(1994));
        $rt_throw(var$2);

    Patched: Return a dummy version object when CURRENT_VERSION is null.
    """
    old = """var$1 = nm_SharedConstants_CURRENT_VERSION;
        if (var$1 !== null)
            return var$1;
        var$2 = new jl_IllegalStateException;
        jl_Throwable__init_(var$2, $rt_s(1994));
        $rt_throw(var$2);"""

    new = """var$1 = nm_SharedConstants_CURRENT_VERSION;
        if (var$1 !== null)
            return var$1;
        var __vstr = function(s) { var o = new String(s); o.$nativeString = s; return o; };
        return { $getName: function() { return __vstr('26.1.2'); }, getName: function() { return __vstr('26.1.2'); }, $name: function() { return __vstr('26.1.2'); }, name: function() { return __vstr('26.1.2'); }, $getProtocolVersion: function() { return 775; }, getProtocolVersion: function() { return 775; }, $getProtocolVersionIp: function() { return 775; }, $getDataVersion: function() { return 4189; }, getDataVersion: function() { return 4189; }, $dataVersion: function() { return 4189; }, dataVersion: function() { return 4189; }, $getServerBrands: function() { return []; }, getServerBrands: function() { return []; }, $toString: function() { return __vstr('26.1.2'); }, toString: function() { return '26.1.2'; }, $getId: function() { return __vstr('26.1.2'); }, getId: function() { return __vstr('26.1.2'); }, $packVersion: function() { return 18; }, packVersion: function() { return 18; }, $getWorldVersion: function() { return 4189; }, getWorldVersion: function() { return 4189; }, $getResourcePackFormat: function() { return 18; }, getResourcePackFormat: function() { return 18; }, $getDataPackFormat: function() { return 18; }, getDataPackFormat: function() { return 18; }, $stable: function() { return 1; }, stable: function() { return true; }, $isStable: function() { return 1; }, isStable: function() { return true; }, $isSnapshot: function() { return 0; }, isSnapshot: function() { return false; } };"""

    if old in data:
        data = data.replace(old, new)
        print("  Patched getCurrentVersion with dummy version return")
    else:
        print("  WARNING: getCurrentVersion pattern not found")
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

    # Patch jnc_FileChannel_open to return a fake channel instead of throwing
    print("\nPatching jnc_FileChannel_open to return fake channel...")
    data = patch_file_channel_open(data)

    # Patch nmud_DataFixers_getDataFixer to handle null DATA_FIXER
    print("\nPatching DataFixers.getDataFixer for null safety...")
    data = patch_data_fixer(data)

    # Patch SharedConstants.getCurrentVersion to return dummy version
    print("\nPatching SharedConstants.getCurrentVersion for null safety...")
    data = patch_shared_constants(data)

    # Patch ji_File_toPath to return a non-null path (TeaVM's version returns null)
    print("\nPatching ji_File_toPath to return fake path...")
    # The original function body is: case 0: return null;
    # We replace 'return null;' with 'return (function() { ... })();'
    old_return = 'ji_File_toPath = var$0 => {\n    let $ptr, $tmp;\n    $ptr = 0;\n    if ($rt_resuming()) {\n        let $thread = $rt_nativeThread();\n        $ptr = $thread.pop();var$0 = $thread.pop();\n    }\n    main: while (true) { switch ($ptr) {\n    case 0:\n        return null;'
    if old_return in data:
        new_return = '''ji_File_toPath = var$0 => {
    var __p = var$0.$path || var$0.path || '/';
    var __pathObj = {
        $resolve: function(o) { return __pathObj; }, resolve: function(o) { return __pathObj; },
        $resolveSibling: function(o) { return __pathObj; }, resolveSibling: function(o) { return __pathObj; },
        $toString: function() { var s = new String(__p); s.$nativeString = __p; return s; }, toString: function() { return __p; },
        $getParent: function() { return __pathObj; }, getParent: function() { return __pathObj; },
        $getFileName: function() { return __pathObj; }, getFileName: function() { return __pathObj; },
        $getFileNameString: function() { return __p; }, getFileNameString: function() { return __p; },
        $getRoot: function() { return __pathObj; }, getRoot: function() { return __pathObj; },
        $isAbsolute: function() { return 1; }, isAbsolute: function() { return true; },
        $normalize: function() { return __pathObj; }, normalize: function() { return __pathObj; },
        $relativize: function(o) { return o; }, relativize: function(o) { return o; },
        $toAbsolutePath: function() { return __pathObj; }, toAbsolutePath: function() { return __pathObj; },
        $toFile: function() { return null; }, toFile: function() { return null; },
        $startsWith: function(p) { return 1; }, startsWith: function(p) { return true; },
        $endsWith: function(p) { return 1; }, endsWith: function(p) { return true; },
        $compareTo: function(o) { return 0; }, compareTo: function(o) { return 0; },
        $subpath: function(a,b) { return __pathObj; }, subpath: function(a,b) { return __pathObj; },
        $getNameCount: function() { return 1; }, getNameCount: function() { return 1; },
        $getName: function(i) { return __pathObj; }, getName: function(i) { return __pathObj; },
        $toRealPath: function() { return __pathObj; }, toRealPath: function() { return __pathObj; },
        $toUri: function() { return { toString: function() { return 'file://'+__p; }, $toString: function() { return 'file://'+__p; } }; },
        toUri: function() { return { toString: function() { return 'file://'+__p; }, $toString: function() { return 'file://'+__p; } }; },
        $getFileSystem: function() {
            if (__pathObj.__fs) return __pathObj.__fs;
            var __fsp = {};
            __fsp.$getScheme = function() { return 'file'; };
            __fsp.$readAttributes = function() { return { $isDirectory: function() { return 0; }, $isRegularFile: function() { return 0; }, $size: function() { return 0; }, $isReadable: function() { return 0; }, $isWritable: function() { return 0; }, $isHidden: function() { return 0; }, $lastModifiedTime: function() { return { toMillis: function() { return 0; } }; }, $toString: function() { return '{}'; } }; };
            __fsp.$newInputStream = function() { return { $read: function() { return -1; }, $available: function() { return 0; }, $close: function() {} }; };
            __fsp.$newDirectoryStream0 = function() { return { $iterator: function() { return { $hasNext: function() { return 0; }, $next: function() { return null; } }; }, $close: function() {} }; };
            __fsp.$exists = function() { return 0; };
            __fsp.$createDirectories = function() {};
            __fsp.$createDirectory = function() {};
            __fsp.$isDirectory = function() { return 0; };
            __fsp.$toString = function() { return 'file'; };
            var __fs = {};
            __fs.$provider = function() { return __fsp; };
            __fs.$isOpen = function() { return 1; };
            __fs.$close = function() {};
            __fs.$toString = function() { return 'file:///'; };
            __pathObj.__fs = __fs;
            return __fs;
        },
        getFileSystem: function() { return __pathObj.$getFileSystem(); }
    };
    return __pathObj;
    main: while (true) { switch (0) {
    case 0:
        return __pathObj;'''
        data = data.replace(old_return, new_return)
        print("  Replaced ji_File_toPath return null with fake path")
    else:
        print("  WARNING: ji_File_toPath original pattern not found - may already be patched")

    # Textual patches that use hardcoded function names — only apply in unobfuscated builds
    is_unobfuscated = 'ji_File_toPath' in data or 'jnf_Files_exists' in data
    if is_unobfuscated:
        print("\nApplying unobfuscated-only textual patches...")
        # Patch populatePackList
        if '$discoveredPacks = var$4.$path3;' in data:
            data = data.replace('$discoveredPacks = var$4.$path3;', 'if (var$4 === null || var$4 === undefined) { return; } $discoveredPacks = var$4.$path3;')
            print("  Patched populatePackList null check")
        # Patch getExternalAssetSource
        if '$tmp = var$2.$resolve(var$3);' in data:
            data = data.replace('$tmp = var$2.$resolve(var$3);', 'if (var$2 === null || var$2 === undefined) return null; $tmp = var$2.$resolve(var$3);')
            print("  Patched getExternalAssetSource null check")

        # Patch Files.exists
        if 'jnf_Files_exists = ($path, $options) => {' in data:
            data = data.replace('jnf_Files_exists = ($path, $options) => {', 'jnf_Files_exists = ($path, $options) => {\n    if ($path === null || $path === undefined) return 0;')
            print("  Patched Files.exists null check")
    else:
        print("\nSkipping unobfuscated-only textual patches (obfuscated build)")

    # Patch jl_Object_identity to handle null this (works for both builds)
    data = data.replace(
        'jl_Object_identity = $this => {',
        'jl_Object_identity = $this => {\n    if ($this === null || $this === undefined) return 0;'
    )
    print("  Patched Object.identity null check")

    # Patch HashMap.putAll to handle null map
    if 'ju_HashMap_putAll = ' in data:
        data = data.replace(
            'ju_HashMap_putAll = ',
            'ju_HashMap_putAll = nullSafePutAll || ' if False else 'ju_HashMap_putAll = '
        )
        # Actually just add a null check at the start
        data = data.replace(
            'ju_HashMap_putAll = ($this, $map) => {',
            'ju_HashMap_putAll = ($this, var$1) => {\n    if (var$1 === null || var$1 === undefined) return;'
        )
        print("  Patched HashMap.putAll null check")

    # Patch Collection.toArray to handle null generator
    if 'ju_Collection_toArray = ' in data:
        data = data.replace(
            '$tmp = $gen.$apply1(var$2);',
            'if ($gen === null || $gen === undefined) return []; $tmp = $gen.$apply1(var$2);'
        )
        print("  Patched Collection.toArray null check (inside case 1)")

    # Patch ImmutableMap.copyOf to handle undefined/null input
    if 'cgcc_ImmutableMap_copyOf = ' in data:
        data = data.replace(
            'cgcc_ImmutableMap_copyOf = ',
            'cgcc_ImmutableMap_copyOf = '
        )
        # Add null check at the crash point
        data = data.replace(
            'var$1.data.length',
            '(var$1 && var$1.data) ? var$1.data.length : 0',
            1  # Only first occurrence in copyOf
        )
        print("  Patched ImmutableMap.copyOf null check")
    else:
        print("  WARNING: HashMap.putAll not found")

    # Replace juc_Executors_newScheduledThreadPool body (returns null from bytecode patcher)
    # or add it if DCE'd entirely
    if 'juc_Executors_newScheduledThreadPool' in data and 'juc_Executors_newScheduledThreadPool =' in data:
        # Function exists but returns null — replace body
        import re
        pattern = r'(juc_Executors_newScheduledThreadPool\s*=\s*\([^)]*\)\s*=>\s*\{)[^}]*(?:\{[^}]*\}[^}]*)*\}'
        match = re.search(pattern, data)
        if match:
            new_body = '''juc_Executors_newScheduledThreadPool = (threadCount, threadFactory) => {
    return { $execute: function(r) { try { r.$run(); } catch(e) {} }, execute: function(r) { try { r.$run(); } catch(e) {} }, $shutdown: function() {}, shutdown: function() {}, $isShutdown: function() { return 0; }, $toString: function() { return 'fake-executor'; } };
}'''
            data = data[:match.start()] + new_body + data[match.end():]
            print("  Replaced juc_Executors_newScheduledThreadPool body")
    else:
        # Function doesn't exist — add it after newFixedThreadPool
        insert_after = 'juc_Executors_newFixedThreadPool = (var$1, var$2) => {\n    return new juc_Executors$1;\n},'
        if insert_after in data:
            new_func = '''juc_Executors_newFixedThreadPool = (var$1, var$2) => {
    return new juc_Executors$1;
},
juc_Executors_newScheduledThreadPool = (threadCount, threadFactory) => {
    return { $execute: function(r) { try { r.$run(); } catch(e) {} }, execute: function(r) { try { r.$run(); } catch(e) {} }, $shutdown: function() {}, shutdown: function() {}, $isShutdown: function() { return 0; }, $toString: function() { return 'fake-executor'; } };
},'''
            data = data.replace(insert_after, new_func)
            print("  Added juc_Executors_newScheduledThreadPool (was DCE'd)")
        else:
            print("  WARNING: Could not find insertion point for newScheduledThreadPool")

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
