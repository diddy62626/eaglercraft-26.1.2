#!/usr/bin/env python3
"""
Create a separate JAR with un-finaled versions of DataFixer and Schema.
This jar goes BEFORE the DFU jar on the classpath, so javac reads
our versions instead of the original FINAL ones.
"""
import sys, os, struct, zipfile, tempfile, subprocess, shutil

DFU_JAR = sys.argv[1]
OUTPUT_JAR = sys.argv[2]
UNFINAL_CLASSES = [
    'com/mojang/datafixers/DataFixer.class',
    'com/mojang/datafixers/schemas/Schema.class',
]
ACC_FINAL = 0x0010

with zipfile.ZipFile(DFU_JAR, 'r') as zin:
    with zipfile.ZipFile(OUTPUT_JAR, 'w', zipfile.ZIP_DEFLATED) as zout:
        for cls in UNFINAL_CLASSES:
            data = bytearray(zin.read(cls))
            flags = struct.unpack('>H', data[8:10])[0]
            new_flags = flags & ~ACC_FINAL
            struct.pack_into('>H', data, 8, new_flags)
            zout.writestr(cls, bytes(data))
            print(f'  {cls}: {hex(flags)} -> {hex(new_flags)} (removed FINAL)')

print(f'Created {OUTPUT_JAR}')
