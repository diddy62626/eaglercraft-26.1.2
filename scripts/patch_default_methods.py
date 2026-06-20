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
    // Wrap all static initializers (cm.clinit) in try/catch.
    // TeaVM 0.15 reorders static field initializations, causing forward
    // references (e.g., AXISANGLE4F references QUATERNIONF before it's
    // initialized). Without try/catch, this crashes the entire game.
    // With try/catch, the failing class's static fields stay null but
    // the game continues (falls back to adapter-only mode gracefully).
    // ============================================================
    var clinitsWrappedCount = 0;
    var clinitFailures = 0;
    for (var k = 0; k < allClasses.length; k++) {
        var c = allClasses[k];
        if (!c || !c[meta]) continue;
        var cm = c[meta];
        if (!cm.clinit) continue;

        // Save original clinit and replace with try/catch wrapper.
        // The original clinit pattern is:
        //   () => { m.clinit = () => {}; actualClinit(); }
        // It first replaces itself with a no-op (re-entry guard),
        // THEN calls the actual init. If actualClinit() throws,
        // our wrapper catches it. The re-entry guard ensures the
        // clinit won't be retried (preventing infinite loops).
        var origClinit = cm.clinit;
        (function(origClinit, cm) {
            cm.clinit = function() {
                try {
                    origClinit();
                } catch(e) {
                    if (clinitFailures < 10) {
                        var clsName = (cm.name || 'unknown');
                        console.warn('[ClinitWrap] ' + clsName + ' static init failed (continuing): ' + (e && e.message ? e.message : String(e)));
                    }
                    clinitFailures++;
                }
            };
        })(origClinit, cm);
        clinitsWrappedCount++;
    }
    console.log('[ClinitWrap] Wrapped ' + clinitsWrappedCount + ' static initializers with try/catch');
})();
""" % registry_json

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
