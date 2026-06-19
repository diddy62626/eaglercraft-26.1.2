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
    # Collect all function names referenced in the registry
    all_func_names = set()
    for iface, methods in registry.items():
        all_func_names.add(iface)
        for vmethod, func_name in methods.items():
            all_func_names.add(func_name)

    # Build JS array of function names to look up
    func_names_json = json.dumps(sorted(all_func_names))
    registry_json = json.dumps(registry, separators=(',', ':'))

    patcher = """
// ============================================================
// TeaVM Default Method Patcher (auto-generated)
// Workaround for TeaVM 0.15 bug: interface default methods not
// included in implementing classes' virtual method tables.
// ============================================================
(function() {
    var __registry = %s;
    var __funcNames = %s;

    // Build lookup map: function name → function reference
    var __funcs = {};
    var __global = typeof self !== 'undefined' ? self : (typeof global !== 'undefined' ? global : this);
    for (var i = 0; i < __funcNames.length; i++) {
        var name = __funcNames[i];
        if (typeof __global[name] === 'function') {
            __funcs[name] = __global[name];
        }
    }

    // GN is TeaVM's metadata symbol on class functions
    var GN = typeof Symbol !== 'undefined' ? Symbol('teavm_meta') : '__teavm_meta__';

    // Fnk is the array of all registered classes
    if (typeof Fnk === 'undefined' || !Fnk) {
        console.warn('[DefaultMethodPatcher] Fnk not found, skipping');
        return;
    }

    var patched = 0;
    var classesPatched = 0;

    for (var i = 0; i < Fnk.length; i++) {
        var cls = Fnk[i];
        if (!cls || !cls.prototype || !cls[GN]) continue;

        var meta = cls[GN];
        if (!meta.superinterfaces || meta.superinterfaces.length === 0) continue;

        var classPatched = false;

        for (var j = 0; j < meta.superinterfaces.length; j++) {
            var iface = meta.superinterfaces[j];
            if (!iface) continue;

            // Find this interface in our registry by matching function reference
            for (var ifaceName in __registry) {
                var ifaceFunc = __funcs[ifaceName];
                if (!ifaceFunc || ifaceFunc !== iface) continue;

                var methods = __registry[ifaceName];
                for (var vmethod in methods) {
                    if (typeof cls.prototype[vmethod] === 'function') continue;

                    var funcName = methods[vmethod];
                    var func = __funcs[funcName];
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
})();
""" % (registry_json, func_names_json)

    return patcher


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

    patcher = build_patcher_js(registry)

    # Append the patcher to classes.js
    patched_data = data + "\n" + patcher

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
