#!/usr/bin/env python3
"""Patch DFU jar by adding duplicate entries (jar allows duplicates)."""
import sys, os, struct, subprocess, tempfile, zipfile, shutil

JAR_PATH = sys.argv[1]
UNFINAL_CLASSES = [
    'com/mojang/datafixers/DataFixer.class',
    'com/mojang/datafixers/schemas/Schema.class',
]
ACC_FINAL = 0x0010
JAVA_HOME = os.environ.get('JAVA_HOME', '')
JAR_CMD = os.path.join(JAVA_HOME, 'bin', 'jar') if JAVA_HOME else 'jar'

tmpdir = tempfile.mkdtemp()

# Extract and patch classes
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

# Use 'jar uf' to UPDATE entries (add as new versions, overriding old)
for cls in UNFINAL_CLASSES:
    result = subprocess.run(
        [JAR_CMD, 'uf', JAR_PATH, '-C', tmpdir, cls],
        capture_output=True, text=True
    )
    if result.returncode != 0:
        print(f'  jar uf failed: {result.stderr}')
        sys.exit(1)
    else:
        print(f'  Updated {cls} in jar')

shutil.rmtree(tmpdir)
print(f'Patched {JAR_PATH}')
