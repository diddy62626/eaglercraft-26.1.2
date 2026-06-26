#!/usr/bin/env python3
"""Extract un-finaled DataFixer and Schema to a directory."""
import sys, os, struct, zipfile

DFU_JAR = sys.argv[1]
OUTPUT_DIR = sys.argv[2]
UNFINAL_CLASSES = [
    'com/mojang/datafixers/DataFixer.class',
    'com/mojang/datafixers/schemas/Schema.class',
]
ACC_FINAL = 0x0010

with zipfile.ZipFile(DFU_JAR, 'r') as zf:
    for cls in UNFINAL_CLASSES:
        data = bytearray(zf.read(cls))
        flags = struct.unpack('>H', data[8:10])[0]
        new_flags = flags & ~ACC_FINAL
        struct.pack_into('>H', data, 8, new_flags)
        outpath = os.path.join(OUTPUT_DIR, cls)
        os.makedirs(os.path.dirname(outpath), exist_ok=True)
        with open(outpath, 'wb') as f:
            f.write(data)
        print(f'  {cls}: {hex(flags)} -> {hex(new_flags)} (removed FINAL)')

print(f'Extracted to {OUTPUT_DIR}')
