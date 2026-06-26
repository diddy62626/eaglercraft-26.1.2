#!/usr/bin/env python3
"""Patch DFU jar to remove FINAL flag using zip command."""
import sys, os, struct, subprocess, tempfile, zipfile, shutil

JAR_PATH = sys.argv[1]
UNFINAL_CLASSES = [
    'com/mojang/datafixers/DataFixer.class',
    'com/mojang/datafixers/schemas/Schema.class',
]
ACC_FINAL = 0x0010
tmpdir = tempfile.mkdtemp()

with zipfile.ZipFile(JAR_PATH, 'r') as zf:
    for cls in UNFINAL_CLASSES:
        data = bytearray(zf.read(cls))
        flags = struct.unpack('>H', data[8:10])[0]
        new_flags = flags & ~ACC_FINAL
        struct.pack_into('>H', data, 8, new_flags)
        outpath = os.path.join(tmpdir, cls)
        os.makedirs(os.path.dirname(outpath), exist_ok=True)
        with open(outpath, 'wb') as f:
            f.write(data)
        print(f'  {cls}: {hex(flags)} -> {hex(new_flags)} (removed FINAL)')

for cls in UNFINAL_CLASSES:
    subprocess.run(['zip', JAR_PATH, cls], cwd=tmpdir, check=True,
                   capture_output=True, text=True)

shutil.rmtree(tmpdir)
print(f'Patched {JAR_PATH} in-place')
