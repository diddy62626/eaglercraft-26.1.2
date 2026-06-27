#!/usr/bin/env python3
"""
Patch teavm-relocated-libs-asm-0.15.0.jar to accept class file version 70
(Java 26) in org.teavm.asm.ClassReader.

TeaVM 0.15.0's repackaged ASM only supports up to class file version 69
(Java 25 = Opcodes.V25). When running on JDK 26, TeaVM's
MetaprogrammingClassLoader tries to parse JDK 26's own classes (which
are version 70) and throws:
    IllegalArgumentException: Unsupported class file major version 70

The version check is in ClassReader.<init>(byte[], int, int):
    bipush 69       ; push Opcodes.V25
    if_icmple +22   ; if version <= 69, skip throw
    new IllegalArgumentException
    ...

This script patches the `bipush 69` (0x10 0x45) to `bipush 127` (0x10 0x7F),
changing the upper bound from 69 to 127. This accepts all class file
versions up to Java 83 (version 127), which covers all foreseeable JDK
releases.

The lower bound check (V1_1 = 45) is left intact since version 70 is
well above it.
"""
import zipfile
import shutil
import os
import sys

JAR_PATH = sys.argv[1] if len(sys.argv) > 1 else '/tmp/teavm-asm.jar'
OUTPUT_PATH = sys.argv[2] if len(sys.argv) > 2 else '/tmp/teavm-asm-patched.jar'

# Read the JAR
shutil.copy(JAR_PATH, OUTPUT_PATH)

# Read ClassReader.class from the JAR
with zipfile.ZipFile(JAR_PATH, 'r') as zin:
    cr_data = bytearray(zin.read('org/teavm/asm/ClassReader.class'))

print(f"Original ClassReader.class size: {len(cr_data)} bytes")

# Find the version check pattern: bipush 69 (0x10 0x45) followed by if_icmple (0xA4)
# The pattern `10 45 a4` is very specific - bipush 69 + if_icmple
target_pattern = bytes([0x10, 0x45, 0xA4])
patched_count = 0
for i in range(len(cr_data) - 2):
    if cr_data[i] == 0x10 and cr_data[i+1] == 0x45 and cr_data[i+2] == 0xA4:
        print(f"  Found version check at offset {i}: bipush 69 + if_icmple")
        # Change bipush 69 to bipush 127 (max positive signed byte)
        # This changes the upper bound from V25 (69) to 127 (Java 83)
        cr_data[i+1] = 0x7F
        patched_count += 1
        print(f"  Patched: bipush 69 -> bipush 127 (0x10 0x7F)")

if patched_count == 0:
    print("ERROR: Could not find version check pattern (bipush 69 + if_icmple)")
    print("Searching for bipush 69 alone...")
    for i in range(len(cr_data) - 1):
        if cr_data[i] == 0x10 and cr_data[i+1] == 0x45:
            context = cr_data[i:i+5]
            print(f"  bipush 69 at offset {i}, context: {context.hex()}")
    sys.exit(1)

print(f"Patched {patched_count} version check(s)")

# Write the patched JAR
with zipfile.ZipFile(OUTPUT_PATH, 'r') as zin:
    with zipfile.ZipFile(OUTPUT_PATH + '.tmp', 'w', zipfile.ZIP_DEFLATED) as zout:
        for item in zin.infolist():
            if item.filename == 'org/teavm/asm/ClassReader.class':
                zout.writestr(item, bytes(cr_data))
                print(f"Wrote patched ClassReader.class ({len(cr_data)} bytes)")
            else:
                zout.writestr(item, zin.read(item.filename))

os.replace(OUTPUT_PATH + '.tmp', OUTPUT_PATH)
print(f"\nPatched JAR saved to {OUTPUT_PATH}")
