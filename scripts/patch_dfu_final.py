#!/usr/bin/env python3
"""
Patch DFU jar to remove FINAL flag from DataFixer and Schema classes.
Access flags are at bytes 8-9 (after magic(4) + minor(2) + major(2)).
"""
import zipfile, os, sys, struct

JAR_PATH = sys.argv[1]
OUTPUT_PATH = sys.argv[2]

UNFINAL_CLASSES = [
    'com/mojang/datafixers/DataFixer.class',
    'com/mojang/datafixers/schemas/Schema.class',
]

ACC_FINAL = 0x0010

with zipfile.ZipFile(JAR_PATH, 'r') as zin:
    with zipfile.ZipFile(OUTPUT_PATH, 'w', zipfile.ZIP_DEFLATED) as zout:
        for item in zin.infolist():
            data = zin.read(item.filename)
            if item.filename in UNFINAL_CLASSES:
                patched = bytearray(data)
                # Access flags are at offset 8 (after magic(4)+minor(2)+major(2))
                flags = struct.unpack('>H', patched[8:10])[0]
                new_flags = flags & ~ACC_FINAL
                struct.pack_into('>H', patched, 8, new_flags)
                data = bytes(patched)
                print(f'  {item.filename}: {hex(flags)} -> {hex(new_flags)} (removed FINAL)')
            zout.writestr(item, data)

print(f'Patched JAR saved to {OUTPUT_PATH}')
