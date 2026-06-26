#!/usr/bin/env python3
"""
Patch DFU jar to replace null returns with non-null returns.

Bytecode trick: ACONST_NULL (0x01, 1 byte) + ARETURN (0xB0)
→ ALOAD_0 (0x2A) or ALOAD_1 (0x2B) + ARETURN (0xB0)

Same byte count — no file size change. No javac involved.

- Static methods with 1+ params: ALOAD_1 (return first param)
- Instance methods: ALOAD_0 (return 'this')
- Static methods with 0 params: skip (can't return non-null without expanding)
"""
import zipfile
import shutil
import os
import sys
import struct

JAR_PATH = sys.argv[1]
OUTPUT_PATH = sys.argv[2]

def parse_constant_pool(data, offset, count):
    """Parse constant pool, return (pool_list, end_offset)."""
    pool = [None]  # 1-indexed
    i = offset
    idx = 1
    while idx < count:
        tag = data[i]; i += 1
        if tag == 1:  # Utf8
            length = struct.unpack('>H', data[i:i+2])[0]; i += 2
            pool.append(('Utf8', data[i:i+length].decode('utf-8','replace')))
            i += length
        elif tag in (3, 4):  # Integer, Float
            pool.append((tag,)); i += 4
        elif tag in (5, 6):  # Long, Double (take 2 slots)
            pool.append((tag,)); pool.append(None); idx += 1; i += 8
        elif tag == 7:  # Class
            pool.append(('Class', struct.unpack('>H', data[i:i+2])[0])); i += 2
        elif tag == 8:  # String
            pool.append(('String',)); i += 2
        elif tag in (9, 10, 11):  # Field/Method/InterfaceMethod ref
            pool.append((tag,)); i += 4
        elif tag == 12:  # NameAndType
            name_idx = struct.unpack('>H', data[i:i+2])[0]
            desc_idx = struct.unpack('>H', data[i+2:i+4])[0]
            pool.append(('NameAndType', name_idx, desc_idx)); i += 4
        elif tag == 15:  # MethodHandle
            pool.append((tag,)); i += 3
        elif tag == 16:  # MethodType
            pool.append((tag,)); i += 2
        elif tag == 17:  # Dynamic
            pool.append((tag,)); i += 4
        elif tag == 18:  # InvokeDynamic
            pool.append((tag,)); i += 4
        elif tag == 19:  # Module
            pool.append((tag,)); i += 2
        elif tag == 20:  # Package
            pool.append((tag,)); i += 2
        else:
            break
        idx += 1
    return pool, i

def get_utf8(pool, idx):
    """Get Utf8 string from constant pool."""
    if idx < len(pool) and pool[idx] and pool[idx][0] == 'Utf8':
        return pool[idx][1]
    return ''

def get_nameandtype(pool, idx):
    """Get (name, descriptor) from NameAndType entry."""
    if idx < len(pool) and pool[idx] and pool[idx][0] == 'NameAndType':
        name = get_utf8(pool, pool[idx][1])
        desc = get_utf8(pool, pool[idx][2])
        return name, desc
    return '', ''

def count_params(desc):
    """Count parameters from method descriptor like '(ILjava/lang/String;)V'."""
    if not desc or not desc.startswith('('):
        return 0
    args = desc[1:desc.index(')') if ')' in desc else len(desc)]
    count = 0
    i = 0
    while i < len(args):
        # Array dimensions
        while i < len(args) and args[i] == '[':
            i += 1
        if i >= len(args):
            break
        if args[i] == 'L':  # Object type
            end = args.index(';', i)
            i = end + 1
        elif args[i] == 'Q':  # Record type (Java 16+)
            end = args.index(';', i)
            i = end + 1
        else:  # Primitive
            i += 1
        count += 1
    return count

ACONST_NULL = 0x01
ARETURN = 0xB0
ALOAD_0 = 0x2A
ALOAD_1 = 0x2B
ALOAD_2 = 0x2C

# Method access flags
ACC_STATIC = 0x0008

def patch_class(data, cls_name):
    """Patch null returns in a class file. Returns (patched_data, count)."""
    if len(data) < 10:
        return data, 0
    
    # Parse header
    magic = struct.unpack('>I', data[0:4])[0]
    if magic != 0xCAFEBABE:
        return data, 0
    
    minor = struct.unpack('>H', data[4:6])[0]
    major = struct.unpack('>H', data[6:8])[0]
    cp_count = struct.unpack('>H', data[8:10])[0]
    
    # Parse constant pool
    pool, offset = parse_constant_pool(data, 10, cp_count)
    
    # Skip access_flags, this_class, super_class
    offset += 6
    
    # Skip interfaces
    iface_count = struct.unpack('>H', data[offset:offset+2])[0]
    offset += 2 + iface_count * 2
    
    # Skip fields
    field_count = struct.unpack('>H', data[offset:offset+2])[0]
    offset += 2
    for _ in range(field_count):
        # access_flags(2) + name_index(2) + descriptor_index(2) + attributes_count(2)
        offset += 6
        attr_count = struct.unpack('>H', data[offset:offset+2])[0]
        offset += 2
        for _ in range(attr_count):
            offset += 6  # name_index(2) + length(4)
            attr_len = struct.unpack('>I', data[offset-4:offset])[0]
            offset += attr_len
    
    # Parse methods
    method_count = struct.unpack('>H', data[offset:offset+2])[0]
    offset += 2
    
    patched = bytearray(data)
    total_count = 0
    
    for _ in range(method_count):
        # access_flags(2) + name_index(2) + descriptor_index(2)
        method_start = offset
        access_flags = struct.unpack('>H', data[offset:offset+2])[0]
        name_idx = struct.unpack('>H', data[offset+2:offset+4])[0]
        desc_idx = struct.unpack('>H', data[offset+4:offset+6])[0]
        offset += 6
        
        method_name = get_utf8(pool, name_idx)
        method_desc = get_utf8(pool, desc_idx)
        
        is_static = bool(access_flags & ACC_STATIC)
        param_count = count_params(method_desc)
        
        # Parse method attributes
        attr_count = struct.unpack('>H', data[offset:offset+2])[0]
        offset += 2
        
        for _ in range(attr_count):
            attr_name_idx = struct.unpack('>H', data[offset:offset+2])[0]
            attr_len = struct.unpack('>I', data[offset+2:offset+6])[0]
            attr_name = get_utf8(pool, attr_name_idx)
            
            if attr_name == 'Code':
                # Parse Code attribute
                code_start = offset + 6
                max_stack = struct.unpack('>H', data[code_start:code_start+2])[0]
                max_locals = struct.unpack('>H', data[code_start+2:code_start+4])[0]
                code_length = struct.unpack('>I', data[code_start+4:code_start+8])[0]
                code_offset = code_start + 8
                
                # Scan bytecode for ACONST_NULL + ARETURN
                for i in range(code_length - 1):
                    if patched[code_offset + i] == ACONST_NULL and patched[code_offset + i + 1] == ARETURN:
                        # Determine what to replace ACONST_NULL with
                        if is_static:
                            if param_count >= 1:
                                # Return first parameter (slot 1 for static)
                                patched[code_offset + i] = ALOAD_1
                            elif param_count >= 2:
                                patched[code_offset + i] = ALOAD_2
                            # else: can't fix (no params, static)
                        else:
                            # Instance method: return 'this' (slot 0)
                            patched[code_offset + i] = ALOAD_0
                        
                        if patched[code_offset + i] != ACONST_NULL:
                            total_count += 1
                
                offset = code_start - 6 + 6 + attr_len  # End of this attribute
            else:
                offset += 6 + attr_len
    
    return bytes(patched), total_count


print(f"Patching DFU jar: {JAR_PATH}")

# Read and patch the JAR
shutil.copy(JAR_PATH, OUTPUT_PATH)

with zipfile.ZipFile(JAR_PATH, 'r') as zin:
    with zipfile.ZipFile(OUTPUT_PATH + '.tmp', 'w', zipfile.ZIP_DEFLATED) as zout:
        total_patches = 0
        for item in zin.infolist():
            data = zin.read(item.filename)
            
            if item.filename.endswith('.class'):
                patched_data, count = patch_class(data, item.filename)
                if count > 0:
                    print(f"  {item.filename}: patched {count} null returns")
                    total_patches += count
                data = patched_data
            
            zout.writestr(item, data)

os.replace(OUTPUT_PATH + '.tmp', OUTPUT_PATH)
print(f"\nTotal null returns patched: {total_patches}")
print(f"Patched JAR saved to {OUTPUT_PATH}")
