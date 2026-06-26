#!/usr/bin/env python3
"""Patch DFU jar to remove FINAL flag.
Uses ZIP_STORED for patched entries to avoid compression corruption.
"""
import sys, os, struct, zipfile, tempfile, shutil

JAR_PATH = sys.argv[1]
UNFINAL_CLASSES = {
    'com/mojang/datafixers/DataFixer.class',
    'com/mojang/datafixers/schemas/Schema.class',
}
ACC_FINAL = 0x0010

tmpjar = JAR_PATH + '.tmp'

with zipfile.ZipFile(JAR_PATH, 'r') as zin:
    with zipfile.ZipFile(tmpjar, 'w') as zout:
        for item in zin.infolist():
            data = zin.read(item.filename)
            if item.filename in UNFINAL_CLASSES:
                patched = bytearray(data)
                flags = struct.unpack('>H', patched[8:10])[0]
                new_flags = flags & ~ACC_FINAL
                struct.pack_into('>H', patched, 8, new_flags)
                data = bytes(patched)
                print(f'  {item.filename}: {hex(flags)} -> {hex(new_flags)} (removed FINAL)')
                # Use ZIP_STORED for patched entries (no compression = no corruption)
                zout.writestr(item.filename, data, compress_type=zipfile.ZIP_STORED)
            else:
                # Copy as-is with original compression
                zout.writestr(item, data)

shutil.move(tmpjar, JAR_PATH)
print(f'Patched {JAR_PATH}')
