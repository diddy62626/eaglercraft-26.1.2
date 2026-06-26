#!/usr/bin/env python3
"""
Patch DFU jar to remove FINAL flag from DataFixer and Schema.
Uses 'zip' command to replace files (avoids Python zipfile corruption).
"""
import sys, os, struct, subprocess, tempfile, zipfile

JAR_PATH = sys.argv[1]

UNFINAL_CLASSES = [
    'com/mojang/datafixers/DataFixer.class',
    'com/mojang/datafixers/schemas/Schema.class',
]

ACC_FINAL = 0x0010

# Create temp dir
tmpdir = tempfile.mkdtemp()
files_to_update = []

with zipfile.ZipFile(JAR_PATH, 'r') as zf:
    for cls in UNFINAL_CLASSES:
        # Extract
        data = bytearray(zf.read(cls))
        flags = struct.unpack('>H', data[8:10])[0]
        new_flags = flags & ~ACC_FINAL
        struct.pack_into('>H', data, 8, new_flags)
        
        # Write to temp dir
        outpath = os.path.join(tmpdir, cls)
        os.makedirs(os.path.dirname(outpath), exist_ok=True)
        with open(outpath, 'wb') as f:
            f.write(data)
        files_to_update.append(cls)
        print(f'  {cls}: {hex(flags)} -> {hex(new_flags)} (removed FINAL)')

# Use 'zip' command to update the jar
for cls in files_to_update:
    result = subprocess.run(
        ['zip', JAR_PATH, cls],
        cwd=tmpdir,
        capture_output=True, text=True
    )
    if result.returncode != 0:
        print(f'  ERROR updating {cls}: {result.stderr}')

# Cleanup
import shutil
shutil.rmtree(tmpdir)
print(f'Patched {JAR_PATH} in-place (using zip command)')
