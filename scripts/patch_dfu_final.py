#!/usr/bin/env python3
"""Extract un-finaled DataFixer and Schema to a directory.

Correctly walks the class file constant pool to find access_flags
(which lives AFTER the constant pool, at a variable offset) and
clears the ACC_FINAL bit.

The previous version of this script had a critical bug: it modified
bytes 8-9, which is `constant_pool_count` (a u2 at fixed offset 8),
NOT `access_flags`. For DataFixer.class, this changed count from
19 (0x13) to 1 (0x01) — completely corrupting the class file. TeaVM
then threw 'Index 513 out of bounds for length 1' because the
constant pool array was sized to 1 entry but indices up to 513 were
being looked up.

Class file format (JVM spec):
    u4 magic              (offset 0)
    u2 minor_version      (offset 4)
    u2 major_version      (offset 6)
    u2 constant_pool_count (offset 8)
    cp_info constant_pool[count-1]  (offset 10, variable length)
    u2 access_flags       (AFTER constant pool — variable offset!)
    u2 this_class
    u2 super_class
    ...
"""
import sys, os, struct, zipfile

DFU_JAR = sys.argv[1]
OUTPUT_DIR = sys.argv[2]
UNFINAL_CLASSES = [
    'com/mojang/datafixers/DataFixer.class',
    'com/mojang/datafixers/schemas/Schema.class',
]
ACC_FINAL = 0x0010


def skip_constant_pool(data):
    """Walk the constant pool, return the offset of access_flags.

    Each constant pool entry has a 1-byte tag followed by tag-specific data:
      1 (Utf8):               u2 length + length bytes
      3 (Integer):            u4
      4 (Float):              u4
      5 (Long):               u8 (takes 2 cp slots)
      6 (Double):             u8 (takes 2 cp slots)
      7 (Class):              u2 name_index
      8 (String):             u2 string_index
      9 (Fieldref):           u2 class_index + u2 nat_index
      10 (Methodref):         u2 class_index + u2 nat_index
      11 (InterfaceMethodref):u2 class_index + u2 nat_index
      12 (NameAndType):       u2 name_index + u2 desc_index
      15 (MethodHandle):      u1 reference_kind + u2 reference_index
      16 (MethodType):        u2 descriptor_index
      17 (Dynamic):           u2 bsm_attr_index + u2 nat_index
      18 (InvokeDynamic):     u2 bsm_attr_index + u2 nat_index
      19 (Module):            u2 name_index
      20 (Package):           u2 name_index
    """
    cp_count = struct.unpack('>H', data[8:10])[0]
    offset = 10
    i = 1
    while i < cp_count:
        tag = data[offset]
        offset += 1
        if tag == 1:  # Utf8
            slen = struct.unpack('>H', data[offset:offset+2])[0]
            offset += 2 + slen
        elif tag in (3, 4):  # Integer, Float
            offset += 4
        elif tag in (5, 6):  # Long, Double — take 2 cp slots
            offset += 8
            i += 1  # skip the second slot
        elif tag in (7, 8, 16, 19, 20):  # Class, String, MethodType, Module, Package
            offset += 2
        elif tag in (9, 10, 11, 12, 17, 18):  # Fieldref, Methodref, InterfaceMethodref, NameAndType, Dynamic, InvokeDynamic
            offset += 4
        elif tag == 15:  # MethodHandle
            offset += 3  # 1 byte reference_kind + 2 bytes reference_index
        else:
            raise ValueError(f'Unknown constant pool tag {tag} at offset {offset-1} (cp index {i})')
        i += 1
    # offset is now at access_flags
    return offset, cp_count


with zipfile.ZipFile(DFU_JAR, 'r') as zf:
    for cls in UNFINAL_CLASSES:
        data = bytearray(zf.read(cls))
        access_flags_offset, cp_count = skip_constant_pool(data)
        flags = struct.unpack('>H', data[access_flags_offset:access_flags_offset+2])[0]
        new_flags = flags & ~ACC_FINAL  # Remove FINAL only
        struct.pack_into('>H', data, access_flags_offset, new_flags)
        outpath = os.path.join(OUTPUT_DIR, cls)
        os.makedirs(os.path.dirname(outpath), exist_ok=True)
        with open(outpath, 'wb') as f:
            f.write(data)
        print(f'  {cls}: cp_count={cp_count}, access_flags at offset {access_flags_offset}: {hex(flags)} -> {hex(new_flags)} (removed FINAL)')

print(f'Extracted to {OUTPUT_DIR}')
