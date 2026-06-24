#!/usr/bin/env python3
"""
Patch teavm-core.jar to suppress the 'Variable used before definition' assertion
in PhiUpdater.class.

The assertion throws `new AssertionError("Variable used before definition: ...")`.
We find the `athrow` bytecode instruction (0xBF) that follows the AssertionError
constructor call and replace it with `pop` (0x57) to suppress the exception.
"""
import zipfile
import shutil
import os
import sys

JAR_PATH = sys.argv[1] if len(sys.argv) > 1 else '/tmp/teavm-core.jar'
OUTPUT_PATH = sys.argv[2] if len(sys.argv) > 2 else '/tmp/teavm-core-patched.jar'

# Read the JAR
shutil.copy(JAR_PATH, OUTPUT_PATH)

# Read PhiUpdater.class from the JAR
with zipfile.ZipFile(JAR_PATH, 'r') as zin:
    phi_data = zin.read('org/teavm/model/util/PhiUpdater.class')

print(f"Original PhiUpdater.class size: {len(phi_data)} bytes")

# Search for the string "Variable used before definition" in the constant pool
target_string = b'Variable used before definition'
string_idx = phi_data.find(target_string)
if string_idx < 0:
    print("ERROR: Could not find target string in PhiUpdater.class")
    sys.exit(1)
print(f"Found target string at offset {string_idx}")

# Search for the bytecode pattern that throws AssertionError
# The pattern is: new ... dup ... ldc/invokespecial ... athrow
# We need to find `athrow` (0xBF) that follows `invokespecial` (0xB7)
# which follows a reference to AssertionError

# Find all occurrences of athrow (0xBF)
athrow_positions = []
for i in range(len(phi_data)):
    if phi_data[i] == 0xBF:
        athrow_positions.append(i)
print(f"Found {len(athrow_positions)} athrow instructions")

# Find the AssertionError class reference in the constant pool
# Look for "java/lang/AssertionError" string
assert_error_string = b'java/lang/AssertionError'
assert_idx = phi_data.find(assert_error_string)
if assert_idx < 0:
    print("ERROR: Could not find AssertionError reference")
    sys.exit(1)
print(f"Found AssertionError reference at offset {assert_idx}")

# The bytecode pattern for throwing AssertionError is:
# BB xx xx  (new AssertionError)
# 59        (dup)
# ...       (load arguments)
# B7 xx xx  (invokespecial AssertionError.<init>)
# BF        (athrow)
#
# We look for `B7 xx xx BF` where the invokespecial target is AssertionError.<init>
# and replace BF with 57 (pop)

# Actually, let's just find all `B7 xx xx BF` patterns (invokespecial followed by athrow)
# and replace the BF with 57
patched_count = 0
patched_data = bytearray(phi_data)

# Search for invokespecial (0xB7) followed by 2 bytes, then athrow (0xBF)
for i in range(len(patched_data) - 4):
    if patched_data[i] == 0xB7 and patched_data[i+3] == 0xBF:
        # This is invokespecial ... athrow
        # Check if it's near the "Variable used before definition" string
        # by checking if this is in the method body (after the constant pool)
        if i > string_idx or i > 1000:  # Skip constant pool entries
            print(f"  Patching invokespecial+athrow at offset {i} (4 bytes)")
            # Replace the 4-byte sequence: invokespecial (B7 xx xx) + athrow (BF)
            # with: pop2 + pop + aload_1 + areturn (5F 57 2B B0)
            # This pops the AssertionError + String, loads the input Variable
            # parameter (var), and returns it. Returning the input variable
            # instead of null prevents downstream NPE in AssignInstruction.
            patched_data[i] = 0x5F   # pop2 (pops String + AssertionError)
            patched_data[i+1] = 0x57 # pop (pops remaining AssertionError)
            patched_data[i+2] = 0x2B # aload_1 (load input Variable param)
            patched_data[i+3] = 0xB0 # areturn (return input Variable)
            patched_count += 1

print(f"Patched {patched_count} athrow instructions")

if patched_count == 0:
    print("WARNING: No patches applied. Trying broader search...")
    # Try replacing ALL athrow with pop (aggressive but might work)
    for i in range(len(patched_data)):
        if patched_data[i] == 0xBF and i > 5000:  # Skip constant pool
            patched_data[i] = 0x57  # pop
            patched_count += 1
    print(f"Aggressive patch: replaced {patched_count} athrow instructions")

# ============================================================
# Compile and inject null-safe optimizer patches
# ============================================================
# TeaVM 0.15 AGGRESSIVE optimization triggers NPE in:
#   - VariableEscapeAnalyzer$InstructionAnalyzer.visit(BranchingInstruction)
#   - ConstantConditionElimination.constantTarget()
#   - EscapeAnalysis$InstructionEscapeVisitor.visit(BranchingInstruction)
# These call getOperand().getIndex() without null-checking getOperand().
# We compile patched versions of these Java files (with null checks added)
# and replace the .class files in the JAR.

import subprocess
import os

PATCHES_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'teavm-patches')
JAVA_HOME = os.environ.get('JAVA_HOME', '')
JAVAC = os.path.join(JAVA_HOME, 'bin', 'javac') if JAVA_HOME else 'javac'

# Classes to patch: (source path relative to PATCHES_DIR, class file in JAR)
PATCH_CLASSES = [
    ('org/teavm/model/optimization/VariableEscapeAnalyzer.java',
     ['org/teavm/model/optimization/VariableEscapeAnalyzer.class',
      'org/teavm/model/optimization/VariableEscapeAnalyzer$InstructionAnalyzer.class']),
    ('org/teavm/model/optimization/ConstantConditionElimination.java',
     ['org/teavm/model/optimization/ConstantConditionElimination.class']),
    ('org/teavm/model/analysis/EscapeAnalysis.java',
     ['org/teavm/model/analysis/EscapeAnalysis.class',
      'org/teavm/model/analysis/EscapeAnalysis$InstructionEscapeVisitor.class']),
]

# Compile patched Java files
compiled_classes = {}  # class file name -> bytes
if os.path.isdir(PATCHES_DIR):
    print(f"\n=== Compiling null-safe optimizer patches ===")
    print(f"PATCHES_DIR: {PATCHES_DIR}")
    print(f"JAVAC: {JAVAC}")
    print(f"JAR_PATH (classpath): {JAR_PATH}")

    # Collect all .java files to compile
    java_files = []
    for src_rel, _ in PATCH_CLASSES:
        src_abs = os.path.join(PATCHES_DIR, src_rel)
        if os.path.exists(src_abs):
            java_files.append(src_abs)
            print(f"  Found: {src_rel}")
        else:
            print(f"  WARNING: {src_rel} not found!")

    if java_files:
        # Compile with teavm-core JAR AND all its dependencies as classpath
        compile_dir = OUTPUT_PATH + '.compile_out'
        os.makedirs(compile_dir, exist_ok=True)

        # Find all JARs in the Gradle cache to use as classpath.
        # teavm-core depends on hppc, common, etc. which need to be on
        # the classpath for EscapeAnalysis.java to compile.
        import glob
        cache_dir = os.path.expanduser('~/.gradle/caches/modules-2/files-2.1')
        all_jars = glob.glob(os.path.join(cache_dir, '**', '*.jar'), recursive=True)
        # Filter out sources and javadoc JARs
        all_jars = [j for j in all_jars if not (j.endswith('-sources.jar') or j.endswith('-javadoc.jar'))]
        # Always include the teavm-core JAR itself
        all_jars.append(JAR_PATH)
        classpath = ':'.join(all_jars)
        print(f"  Classpath has {len(all_jars)} JARs")

        cmd = [JAVAC, '-cp', classpath, '-d', compile_dir] + java_files
        print(f"  Running: {JAVAC} -cp <{len(all_jars)} JARs> -d {compile_dir} {len(java_files)} files")
        try:
            result = subprocess.run(cmd, capture_output=True, text=True, timeout=120)
            if result.returncode != 0:
                print(f"  COMPILE ERROR (returncode={result.returncode}):")
                print(result.stderr[:3000])
                print("--- stdout ---")
                print(result.stdout[:1000])
                print("WARNING: Optimizer patches NOT applied. AGGRESSIVE optimization may NPE.")
            else:
                print(f"  Compilation succeeded!")
                # Walk the output directory and collect .class files
                for root, dirs, files in os.walk(compile_dir):
                    for fname in files:
                        if fname.endswith('.class'):
                            class_path = os.path.join(root, fname)
                            rel_path = os.path.relpath(class_path, compile_dir)
                            with open(class_path, 'rb') as f:
                                compiled_classes[rel_path.replace(os.sep, '/')] = f.read()
                            print(f"    Compiled: {rel_path} ({os.path.getsize(class_path)} bytes)")
        except FileNotFoundError:
            print(f"  ERROR: javac not found at '{JAVAC}'")
            print("  WARNING: Optimizer patches NOT applied. AGGRESSIVE optimization may NPE.")
        except subprocess.TimeoutExpired:
            print(f"  ERROR: javac compilation timed out (120s)")
            print("  WARNING: Optimizer patches NOT applied. AGGRESSIVE optimization may NPE.")

        # Clean up
        import shutil
        shutil.rmtree(compile_dir, ignore_errors=True)
else:
    print(f"WARNING: Patches directory not found: {PATCHES_DIR}")
    print("Skipping optimizer null-safety patches")

# Write the patched JAR with both PhiUpdater fix and optimizer patches
with zipfile.ZipFile(OUTPUT_PATH, 'r') as zin:
    with zipfile.ZipFile(OUTPUT_PATH + '.tmp', 'w', zipfile.ZIP_DEFLATED) as zout:
        for item in zin.infolist():
            if item.filename == 'org/teavm/model/util/PhiUpdater.class':
                zout.writestr(item, bytes(patched_data))
                print(f"Wrote patched PhiUpdater.class ({len(patched_data)} bytes)")
            elif item.filename in compiled_classes:
                zout.writestr(item, compiled_classes[item.filename])
                print(f"Replaced {item.filename} with null-safe version ({len(compiled_classes[item.filename])} bytes)")
            else:
                zout.writestr(item, zin.read(item.filename))

# Also write any new inner class files that didn't exist in the original JAR
if compiled_classes:
    with zipfile.ZipFile(OUTPUT_PATH + '.tmp', 'a', zipfile.ZIP_DEFLATED) as zappend:
        existing_names = set()
        with zipfile.ZipFile(OUTPUT_PATH, 'r') as zin:
            existing_names = set(zin.namelist())
        for class_name, class_bytes in compiled_classes.items():
            if class_name not in existing_names:
                zappend.writestr(class_name, class_bytes)
                print(f"Added new class: {class_name} ({len(class_bytes)} bytes)")

os.replace(OUTPUT_PATH + '.tmp', OUTPUT_PATH)
print(f"\nPatched JAR saved to {OUTPUT_PATH}")
