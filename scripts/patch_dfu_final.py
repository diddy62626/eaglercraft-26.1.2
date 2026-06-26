#!/usr/bin/env python3
"""Patch DFU jar to remove FINAL flag using jar command."""
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

# Copy jar to temp, update, copy back
tmpjar = os.path.join(tmpdir, 'patched.jar')
shutil.copy2(JAR_PATH, tmpjar)

for cls in UNFINAL_CLASSES:
    result = subprocess.run(
        [JAR_CMD, 'uf', tmpjar, '-C', tmpdir, cls],
        capture_output=True, text=True
    )
    if result.returncode != 0:
        print(f'  jar failed for {cls}: {result.stderr}')
        # Try zip as fallback
        result2 = subprocess.run(
            ['zip', tmpjar, cls],
            cwd=tmpdir, capture_output=True, text=True
        )
        if result2.returncode != 0:
            print(f'  zip also failed: {result2.stderr}')
            shutil.rmtree(tmpdir)
            sys.exit(1)

shutil.copy2(tmpjar, JAR_PATH)
shutil.rmtree(tmpdir)
print(f'Patched {JAR_PATH} in-place')
