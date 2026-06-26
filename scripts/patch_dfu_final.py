#!/usr/bin/env python3
"""
Patch DFU jar to remove FINAL flag from DataFixer and Schema classes.
Uses ZIP_STORED to avoid compression-related corruption.
"""
import zipfile, os, sys, struct

JAR_PATH = sys.argv[1]
OUTPUT_PATH = sys.argv[2]

UNFINAL_CLASSES = [
    'com/mojang/datafixers/DataFixer.class',
    'com/mojang/datafixers/schemas/Schema.class',
]

ACC_FINAL = 0x0010

# Read all entries
with zipfile.ZipFile(JAR_PATH, 'r') as zin:
    entries = []
    for item in zin.infolist():
        data = zin.read(item.filename)
        if item.filename in UNFINAL_CLASSES:
            patched = bytearray(data)
            flags = struct.unpack('>H', patched[8:10])[0]
            new_flags = flags & ~ACC_FINAL
            struct.pack_into('>H', patched, 8, new_flags)
            data = bytes(patched)
            print(f'  {item.filename}: {hex(flags)} -> {hex(new_flags)} (removed FINAL)')
        entries.append((item, data))

# Write with same compression as original
with zipfile.ZipFile(OUTPUT_PATH, 'w') as zout:
    for item, data in entries:
        # Preserve original compression type
        zout.writestr(item, data)

print(f'Patched JAR saved to {OUTPUT_PATH}')
