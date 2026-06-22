#!/usr/bin/env python3
"""
Patch teavm-classlib.jar by directly modifying .class file bytecode.
Adds missing methods to existing classes WITHOUT replacing them.

Uses the Java .class file binary format to:
1. Parse the constant pool
2. Add new constant pool entries (method names, descriptors, "Code" string)
3. Add new method entries with proper bytecode
4. Update methods_count
5. Repackage the JAR
"""
import struct
import zipfile
import shutil
import os
import sys
import io

# Constant pool tag types
CONSTANT_Utf8 = 1
CONSTANT_Integer = 3
CONSTANT_Float = 4
CONSTANT_Long = 5
CONSTANT_Double = 6
CONSTANT_Class = 7
CONSTANT_String = 8
CONSTANT_Fieldref = 9
CONSTANT_Methodref = 10
CONSTANT_InterfaceMethodref = 11
CONSTANT_NameAndType = 12
CONSTANT_MethodHandle = 15
CONSTANT_MethodType = 16
CONSTANT_InvokeDynamic = 18

class ClassFile:
    def __init__(self, data):
        self.data = bytearray(data)
        self.pos = 0
        self.constant_pool = []
        self.constant_pool_count = 0
        self.parse()
    
    def u1(self):
        v = self.data[self.pos]; self.pos += 1; return v
    def u2(self):
        v = struct.unpack('>H', self.data[self.pos:self.pos+2])[0]; self.pos += 2; return v
    def u4(self):
        v = struct.unpack('>I', self.data[self.pos:self.pos+4])[0]; self.pos += 4; return v
    def bytes(self, n):
        v = self.data[self.pos:self.pos+n]; self.pos += n; return v
    
    def parse(self):
        self.pos = 0
        magic = self.u4()
        assert magic == 0xCAFEBABE, f"Not a class file: {hex(magic)}"
        minor = self.u2()
        major = self.u2()
        
        # Parse constant pool
        self.constant_pool_count = self.u2()
        self.constant_pool = [None]  # index 0 is unused
        i = 1
        while i < self.constant_pool_count:
            tag = self.u1()
            if tag == CONSTANT_Utf8:
                length = self.u2()
                value = self.bytes(length).decode('utf-8', errors='replace')
                self.constant_pool.append(('Utf8', value))
                i += 1
            elif tag == CONSTANT_Integer:
                self.constant_pool.append(('Integer', self.u4()))
                i += 1
            elif tag == CONSTANT_Float:
                self.constant_pool.append(('Float', self.bytes(4)))
                i += 1
            elif tag == CONSTANT_Long:
                self.constant_pool.append(('Long', self.bytes(8)))
                self.constant_pool.append(None)  # placeholder for 2nd slot
                i += 2  # Long takes 2 slots
            elif tag == CONSTANT_Double:
                self.constant_pool.append(('Double', self.bytes(8)))
                self.constant_pool.append(None)  # placeholder for 2nd slot
                i += 2  # Double takes 2 slots
            elif tag == CONSTANT_Class:
                self.constant_pool.append(('Class', self.u2()))
                i += 1
            elif tag == CONSTANT_String:
                self.constant_pool.append(('String', self.u2()))
                i += 1
            elif tag == CONSTANT_Fieldref:
                self.constant_pool.append(('Fieldref', self.u2(), self.u2()))
                i += 1
            elif tag == CONSTANT_Methodref:
                self.constant_pool.append(('Methodref', self.u2(), self.u2()))
                i += 1
            elif tag == CONSTANT_InterfaceMethodref:
                self.constant_pool.append(('InterfaceMethodref', self.u2(), self.u2()))
                i += 1
            elif tag == CONSTANT_NameAndType:
                self.constant_pool.append(('NameAndType', self.u2(), self.u2()))
                i += 1
            elif tag == CONSTANT_MethodHandle:
                self.constant_pool.append(('MethodHandle', self.u1(), self.u2()))
                i += 1
            elif tag == CONSTANT_MethodType:
                self.constant_pool.append(('MethodType', self.u2()))
                i += 1
            elif tag == CONSTANT_InvokeDynamic:
                self.constant_pool.append(('InvokeDynamic', self.u2(), self.u2()))
                i += 1
            else:
                raise ValueError(f"Unknown constant pool tag: {tag} at index {i}")
        
        # Save position after constant pool
        self.after_cp_pos = self.pos
        
        # Skip to methods
        self.access_flags = self.u2()
        self.this_class = self.u2()
        self.super_class = self.u2()
        interfaces_count = self.u2()
        self.pos += interfaces_count * 2
        
        # Skip fields
        fields_count = self.u2()
        for _ in range(fields_count):
            self.skip_field_or_method()
        
        # Methods
        self.methods_count_pos = self.pos
        self.methods_count = self.u2()
        self.methods_start_pos = self.pos
        for _ in range(self.methods_count):
            self.skip_field_or_method()
        self.methods_end_pos = self.pos
        
        # Rest of the file (attributes)
        self.after_methods_pos = self.pos
    
    def skip_field_or_method(self):
        self.pos += 6  # access_flags(2) + name_index(2) + descriptor_index(2)
        attr_count = self.u2()
        for _ in range(attr_count):
            self.pos += 2  # attr name index
            attr_len = self.u4()
            self.pos += attr_len
    
    def find_utf8(self, text):
        """Find a Utf8 constant pool entry by text, return index or 0"""
        for i, entry in enumerate(self.constant_pool):
            if entry and entry[0] == 'Utf8' and entry[1] == text:
                return i
        return 0
    
    def add_utf8(self, text):
        """Add a Utf8 entry to the constant pool, return index"""
        idx = self.find_utf8(text)
        if idx > 0:
            return idx
        idx = len(self.constant_pool)
        self.constant_pool.append(('Utf8', text))
        self.constant_pool_count += 1
        return idx
    
    def add_name_and_type(self, name_idx, desc_idx):
        """Add a NameAndType entry"""
        idx = len(self.constant_pool)
        self.constant_pool.append(('NameAndType', name_idx, desc_idx))
        self.constant_pool_count += 1
        return idx
    
    def has_method(self, name, descriptor):
        """Check if a method already exists"""
        name_idx = self.find_utf8(name)
        desc_idx = self.find_utf8(descriptor)
        if name_idx == 0 or desc_idx == 0:
            return False
        
        # Re-parse methods to check
        pos = self.methods_start_pos
        for _ in range(self.methods_count):
            access = struct.unpack('>H', self.data[pos:pos+2])[0]
            n_idx = struct.unpack('>H', self.data[pos+2:pos+4])[0]
            d_idx = struct.unpack('>H', self.data[pos+4:pos+6])[0]
            if n_idx == name_idx and d_idx == desc_idx:
                return True
            pos += 6
            attr_count = struct.unpack('>H', self.data[pos:pos+2])[0]
            pos += 2
            for _ in range(attr_count):
                pos += 2
                attr_len = struct.unpack('>I', self.data[pos:pos+4])[0]
                pos += 4 + attr_len
        return False
    
    def add_method(self, name, descriptor, bytecode, max_stack=1, max_locals=1, access_flags=0x0001):
        """Add a method to the class.
        
        bytecode: raw bytecode bytes (without the Code attribute wrapper)
        access_flags: 0x0001=public, 0x0008=static, 0x0400=bridge, etc.
        """
        if self.has_method(name, descriptor):
            return False
        
        # Add constant pool entries
        name_idx = self.add_utf8(name)
        desc_idx = self.add_utf8(descriptor)
        code_str_idx = self.add_utf8("Code")
        
        # Build the method entry
        method = bytearray()
        method += struct.pack('>H', access_flags)  # access_flags
        method += struct.pack('>H', name_idx)       # name_index
        method += struct.pack('>H', desc_idx)        # descriptor_index
        
        # Code attribute
        code_attr = bytearray()
        code_attr += struct.pack('>H', code_str_idx)  # attribute_name_index
        code_length = len(bytecode)
        attr_length = 2 + 2 + 4 + code_length + 2 + 2  # max_stack + max_locals + code_length + code + exc_table_len + attr_count
        code_attr += struct.pack('>I', attr_length)     # attribute_length
        code_attr += struct.pack('>H', max_stack)       # max_stack
        code_attr += struct.pack('>H', max_locals)      # max_locals
        code_attr += struct.pack('>I', code_length)     # code_length
        code_attr += bytecode                            # code
        code_attr += struct.pack('>H', 0)               # exception_table_length
        code_attr += struct.pack('>H', 0)               # attributes_count
        
        method += struct.pack('>H', 1)  # attributes_count (just Code)
        method += code_attr
        
        # Store the method to insert later
        if not hasattr(self, 'new_methods'):
            self.new_methods = []
        self.new_methods.append(bytes(method))
        
        return True
    
    def serialize(self):
        """Serialize the modified class file"""
        output = bytearray()
        
        # Magic + version
        output += struct.pack('>I', 0xCAFEBABE)
        output += struct.pack('>H', 0)  # minor
        output += struct.pack('>H', 52)  # major (Java 20)
        
        # Constant pool count = len(self.constant_pool) (includes None at index 0 and None placeholders for Long/Double 2nd slots)
        actual_count = len(self.constant_pool)
        output += struct.pack('>H', actual_count)
        
        # Write constant pool entries
        for i in range(1, actual_count):
            entry = self.constant_pool[i]
            if entry is None:
                continue
            tag = entry[0]
            if tag == 'Utf8':
                text = entry[1].encode('utf-8')
                output += struct.pack('>B', CONSTANT_Utf8)
                output += struct.pack('>H', len(text))
                output += text
            elif tag == 'Integer':
                output += struct.pack('>B', CONSTANT_Integer)
                output += struct.pack('>I', entry[1])
            elif tag == 'Float':
                output += struct.pack('>B', CONSTANT_Float)
                output += entry[1]
            elif tag == 'Long':
                output += struct.pack('>B', CONSTANT_Long)
                output += entry[1]
            elif tag == 'Double':
                output += struct.pack('>B', CONSTANT_Double)
                output += entry[1]
            elif tag == 'Class':
                output += struct.pack('>B', CONSTANT_Class)
                output += struct.pack('>H', entry[1])
            elif tag == 'String':
                output += struct.pack('>B', CONSTANT_String)
                output += struct.pack('>H', entry[1])
            elif tag == 'Fieldref':
                output += struct.pack('>B', CONSTANT_Fieldref)
                output += struct.pack('>HH', entry[1], entry[2])
            elif tag == 'Methodref':
                output += struct.pack('>B', CONSTANT_Methodref)
                output += struct.pack('>HH', entry[1], entry[2])
            elif tag == 'InterfaceMethodref':
                output += struct.pack('>B', CONSTANT_InterfaceMethodref)
                output += struct.pack('>HH', entry[1], entry[2])
            elif tag == 'NameAndType':
                output += struct.pack('>B', CONSTANT_NameAndType)
                output += struct.pack('>HH', entry[1], entry[2])
            elif tag == 'MethodHandle':
                output += struct.pack('>B', CONSTANT_MethodHandle)
                output += struct.pack('>BH', entry[1], entry[2])
            elif tag == 'MethodType':
                output += struct.pack('>B', CONSTANT_MethodType)
                output += struct.pack('>H', entry[1])
            elif tag == 'InvokeDynamic':
                output += struct.pack('>B', CONSTANT_InvokeDynamic)
                output += struct.pack('>HH', entry[1], entry[2])
        
        # Access flags, this_class, super_class
        output += struct.pack('>H', self.access_flags)
        output += struct.pack('>H', self.this_class)
        output += struct.pack('>H', self.super_class)
        
        # Interfaces
        # Re-read from original data
        iface_pos = self.after_cp_pos + 6  # after access_flags(2) + this(2) + super(2)
        iface_count = struct.unpack('>H', self.data[iface_pos:iface_pos+2])[0]
        output += struct.pack('>H', iface_count)
        output += self.data[iface_pos+2:iface_pos+2+iface_count*2]
        
        # Fields (copy from original)
        fields_pos = iface_pos + 2 + iface_count * 2
        fields_count = struct.unpack('>H', self.data[fields_pos:fields_pos+2])[0]
        output += struct.pack('>H', fields_count)
        
        pos = fields_pos + 2
        for _ in range(fields_count):
            # Copy the field entry
            start = pos
            pos += 6  # access + name + desc
            attr_count = struct.unpack('>H', self.data[pos:pos+2])[0]
            pos += 2
            for _ in range(attr_count):
                pos += 2
                attr_len = struct.unpack('>I', self.data[pos:pos+4])[0]
                pos += 4 + attr_len
            output += self.data[start:pos]
        
        # Methods
        new_count = self.methods_count + len(getattr(self, 'new_methods', []))
        output += struct.pack('>H', new_count)
        
        # Copy existing methods
        output += self.data[self.methods_start_pos:self.methods_end_pos]
        
        # Add new methods
        for method in getattr(self, 'new_methods', []):
            output += method
        
        # Copy remaining (attributes)
        output += self.data[self.after_methods_pos:]
        
        return bytes(output)


# Define methods to add to each class
# Format: (class_path_in_jar, [(method_name, descriptor, bytecode, max_stack, max_locals, access_flags), ...])

# Bytecode snippets:
# void return:     B1 (return)
# int return 0:    03 AC (iconst_0, ireturn)
# long return 0:   09 AD (lconst_0, lreturn)
# float return 0:  0B AE (fconst_0, freturn)
# double return 0: 0E AF (dconst_0, dreturn)
# object null:     01 B0 (aconst_null, areturn)

VOID_RET = bytes([0xB1])
INT_RET = bytes([0x03, 0xAC])
LONG_RET = bytes([0x09, 0xAD])
FLOAT_RET = bytes([0x0B, 0xAE])
DOUBLE_RET = bytes([0x0E, 0xAF])
OBJ_RET = bytes([0x01, 0xB0])

PUBLIC = 0x0001
STATIC = 0x0008

METHODS_TO_ADD = {
    "org/teavm/classlib/java/lang/TRuntime": [
        ("maxMemory", "()J", LONG_RET, 1, 1, PUBLIC),
        ("addShutdownHook", "(Ljava/lang/Thread;)V", VOID_RET, 0, 2, PUBLIC),
        ("removeShutdownHook", "(Ljava/lang/Thread;)Z", INT_RET, 1, 2, PUBLIC),
    ],
    "org/teavm/classlib/java/lang/TSystem": [
        ("getenv", "()Ljava/util/Map;", OBJ_RET, 1, 0, PUBLIC | STATIC),
        ("exit", "(I)V", VOID_RET, 0, 1, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/lang/TThread": [
        ("setContextClassLoader", "(Ljava/lang/ClassLoader;)V", VOID_RET, 0, 2, PUBLIC),
        ("onSpinWait", "()V", VOID_RET, 0, 0, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/lang/TClass": [
        ("getGenericInterfaces", "()[Ljava/lang/reflect/Type;", OBJ_RET, 1, 1, PUBLIC),
        ("getGenericSuperclass", "()Ljava/lang/reflect/Type;", OBJ_RET, 1, 1, PUBLIC),
        ("getResource", "(Ljava/lang/String;)Ljava/net/URL;", OBJ_RET, 1, 2, PUBLIC),
        ("getSigners", "()[Ljava/lang/Object;", OBJ_RET, 1, 1, PUBLIC),
        ("isAnonymousClass", "()Z", INT_RET, 1, 1, PUBLIC),
    ],
    "org/teavm/classlib/java/lang/TClassLoader": [
        ("loadClass", "(Ljava/lang/String;)Ljava/lang/Class;", OBJ_RET, 1, 2, PUBLIC),
    ],
    "org/teavm/classlib/java/lang/TPackage": [
        ("getSpecificationTitle", "()Ljava/lang/String;", OBJ_RET, 1, 1, PUBLIC),
        ("getSpecificationVersion", "()Ljava/lang/String;", OBJ_RET, 1, 1, PUBLIC),
        ("getSpecificationVendor", "()Ljava/lang/String;", OBJ_RET, 1, 1, PUBLIC),
        ("getImplementationTitle", "()Ljava/lang/String;", OBJ_RET, 1, 1, PUBLIC),
        ("getImplementationVersion", "()Ljava/lang/String;", OBJ_RET, 1, 1, PUBLIC),
        ("getImplementationVendor", "()Ljava/lang/String;", OBJ_RET, 1, 1, PUBLIC),
    ],
    "org/teavm/classlib/java/lang/TInteger": [
        ("parseUnsignedInt", "(Ljava/lang/String;I)I", INT_RET, 1, 3, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/lang/TLong": [
        ("parseUnsignedLong", "(Ljava/lang/String;I)J", LONG_RET, 1, 3, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/lang/TCharacter": [
        ("codePointOf", "(Ljava/lang/String;)I", INT_RET, 1, 2, PUBLIC | STATIC),
        ("toString", "(I)Ljava/lang/String;", OBJ_RET, 1, 1, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/util/TUUID": [
        ("<init>", "(JJ)V", VOID_RET, 0, 3, PUBLIC),
        ("nameUUIDFromBytes", "([B)Ljava/util/UUID;", OBJ_RET, 1, 2, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/util/TDate": [
        ("toInstant", "()Ljava/time/Instant;", OBJ_RET, 1, 1, PUBLIC),
    ],
    "org/teavm/classlib/java/io/TFile": [
        # toPath() - returns a new StubPath(path).
        # Bytecode: aload_0 (this), getfield path, invokestatic Paths.get,
        # areturn.
        # But we can't easily reference StubPath from bytecode patcher.
        # Instead, return a non-null placeholder. The default method patcher
        # will add toPath() from our Java patch (File.java) to TFile's
        # prototype if TFile implements the same interface.
        # For now, use OBJ_RET (returns null) — the game's try/catch wrapper
        # will catch any NPE and continue.
        ("toPath", "()Ljava/nio/file/Path;", OBJ_RET, 1, 1, PUBLIC),
    ],
    "org/teavm/classlib/java/io/TBufferedReader": [
        ("transferTo", "(Ljava/io/Writer;)J", LONG_RET, 1, 2, PUBLIC),
    ],
    "org/teavm/classlib/java/net/TInetAddress": [
        ("isAnyLocalAddress", "()Z", INT_RET, 1, 1, PUBLIC),
        ("isLinkLocalAddress", "()Z", INT_RET, 1, 1, PUBLIC),
        ("isMulticastAddress", "()Z", INT_RET, 1, 1, PUBLIC),
        ("isSiteLocalAddress", "()Z", INT_RET, 1, 1, PUBLIC),
        ("getByAddress", "(Ljava/lang/String;[B)Ljava/net/InetAddress;", OBJ_RET, 1, 3, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/net/TURL": [
        ("openConnection", "(Ljava/net/Proxy;)Ljava/net/URLConnection;", OBJ_RET, 1, 2, PUBLIC),
    ],
    "org/teavm/classlib/java/net/THttpURLConnection": [
        ("getContentLengthLong", "()J", LONG_RET, 1, 1, PUBLIC),
    ],
    "org/teavm/classlib/java/nio/TByteBuffer": [
        ("slice", "(II)Ljava/nio/ByteBuffer;", OBJ_RET, 1, 3, PUBLIC),
    ],
    "org/teavm/classlib/java/nio/channels/TChannels": [
        ("newChannel", "(Ljava/io/OutputStream;)Ljava/nio/channels/WritableByteChannel;", OBJ_RET, 1, 2, PUBLIC | STATIC),
        ("newWriter", "(Ljava/nio/channels/WritableByteChannel;Ljava/nio/charset/Charset;)Ljava/io/Writer;", OBJ_RET, 1, 3, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/nio/file/TFileSystem": [
        ("getPathMatcher", "(Ljava/lang/String;)Ljava/nio/file/PathMatcher;", OBJ_RET, 1, 2, PUBLIC),
    ],
    "org/teavm/classlib/java/nio/file/TFiles": [
        ("createLink", "(Ljava/nio/file/Path;Ljava/nio/file/Path;)Ljava/nio/file/Path;", OBJ_RET, 1, 3, PUBLIC | STATIC),
        ("setLastModifiedTime", "(Ljava/nio/file/Path;Ljava/nio/file/attribute/FileTime;)Ljava/nio/file/Path;", OBJ_RET, 1, 3, PUBLIC | STATIC),
        ("getFileStore", "(Ljava/nio/file/Path;)Ljava/nio/file/FileStore;", OBJ_RET, 1, 2, PUBLIC | STATIC),
        ("getPosixFilePermissions", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/util/Set;", OBJ_RET, 1, 3, PUBLIC | STATIC),
        ("setPosixFilePermissions", "(Ljava/nio/file/Path;Ljava/util/Set;)Ljava/nio/file/Path;", OBJ_RET, 1, 3, PUBLIC | STATIC),
        ("getFileAttributeView", "(Ljava/nio/file/Path;Ljava/lang/Class;[Ljava/nio/file/LinkOption;)Ljava/nio/file/attribute/FileAttributeView;", OBJ_RET, 1, 4, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/security/TAccessController": [
        ("checkPermission", "(Ljava/security/Permission;)V", VOID_RET, 0, 2, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/security/TKeyFactory": [
        ("generatePrivate", "(Ljava/security/spec/KeySpec;)Ljava/security/PrivateKey;", OBJ_RET, 1, 2, PUBLIC),
        ("generatePublic", "(Ljava/security/spec/KeySpec;)Ljava/security/PublicKey;", OBJ_RET, 1, 2, PUBLIC),
    ],
    "org/teavm/classlib/java/util/concurrent/TCompletableFuture": [
        ("applyToEither", "(Ljava/util/concurrent/CompletionStage;Ljava/util/function/Function;)Ljava/util/concurrent/CompletableFuture;", OBJ_RET, 1, 3, PUBLIC),
        ("thenApplyAsync", "(Ljava/util/function/Function;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", OBJ_RET, 1, 3, PUBLIC),
        ("thenAcceptAsync", "(Ljava/util/function/Consumer;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", OBJ_RET, 1, 3, PUBLIC),
        ("thenRunAsync", "(Ljava/lang/Runnable;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", OBJ_RET, 1, 3, PUBLIC),
        ("thenCombine", "(Ljava/util/concurrent/CompletionStage;Ljava/util/function/BiFunction;)Ljava/util/concurrent/CompletableFuture;", OBJ_RET, 1, 3, PUBLIC),
        ("thenComposeAsync", "(Ljava/util/function/Function;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", OBJ_RET, 1, 3, PUBLIC),
        ("exceptionallyComposeAsync", "(Ljava/util/function/Function;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", OBJ_RET, 1, 3, PUBLIC),
        ("thenApplyAsync", "(Ljava/util/function/Function;)Ljava/util/concurrent/CompletableFuture;", OBJ_RET, 1, 2, PUBLIC),
    ],
    "org/teavm/classlib/java/util/concurrent/TExecutors": [
        ("newScheduledThreadPool", "(ILjava/util/concurrent/ThreadFactory;)Ljava/util/concurrent/ScheduledExecutorService;", OBJ_RET, 1, 3, PUBLIC | STATIC),
        ("callable", "(Ljava/lang/Runnable;Ljava/lang/Object;)Ljava/util/concurrent/Callable;", OBJ_RET, 1, 3, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/util/concurrent/TScheduledThreadPoolExecutor": [
        ("setContinueExistingPeriodicTasksAfterShutdownPolicy", "(Z)V", VOID_RET, 0, 2, PUBLIC),
        ("setExecuteExistingDelayedTasksAfterShutdownPolicy", "(Z)V", VOID_RET, 0, 2, PUBLIC),
    ],
    "org/teavm/classlib/java/util/concurrent/atomic/TAtomicReferenceArray": [
        ("lazySet", "(ILjava/lang/Object;)V", VOID_RET, 0, 3, PUBLIC),
    ],
    "org/teavm/classlib/java/util/concurrent/TTimeUnit": [
        ("convert", "(Ljava/time/Duration;)J", LONG_RET, 1, 2, PUBLIC),
    ],
    "org/teavm/classlib/java/util/concurrent/TConcurrentHashMap": [
        ("newKeySet", "()Ljava/util/concurrent/ConcurrentHashMap$KeySetView;", OBJ_RET, 1, 0, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/util/regex/TPattern": [
        ("asPredicate", "()Ljava/util/function/Predicate;", OBJ_RET, 1, 1, PUBLIC),
    ],
    "org/teavm/classlib/java/util/TCollections": [
        ("unmodifiableSortedMap", "(Ljava/util/SortedMap;)Ljava/util/SortedMap;", OBJ_RET, 1, 2, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/util/TSpliterators": [
        ("iterator", "(Ljava/util/Spliterator$OfInt;)Ljava/util/PrimitiveIterator$OfInt;", OBJ_RET, 1, 2, PUBLIC | STATIC),
        ("iterator", "(Ljava/util/Spliterator;)Ljava/util/Iterator;", OBJ_RET, 1, 2, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/util/stream/TStreamSupport": [
        ("intStream", "(Ljava/util/Spliterator$OfInt;Z)Ljava/util/stream/IntStream;", OBJ_RET, 1, 3, PUBLIC | STATIC),
        ("longStream", "(Ljava/util/Spliterator$OfLong;Z)Ljava/util/stream/LongStream;", OBJ_RET, 1, 3, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/lang/invoke/TMethodHandle": [
        ("bindTo", "(Ljava/lang/Object;)Ljava/lang/invoke/MethodHandle;", OBJ_RET, 1, 2, PUBLIC),
        ("asType", "(Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;", OBJ_RET, 1, 2, PUBLIC),
        ("invoke", "([Ljava/lang/Object;)Ljava/lang/Object;", OBJ_RET, 1, 2, PUBLIC),
        ("invokeExact", "([Ljava/lang/Object;)Ljava/lang/Object;", OBJ_RET, 1, 2, PUBLIC),
    ],
    "org/teavm/classlib/java/lang/invoke/TMethodHandles": [
        ("filterArguments", "(Ljava/lang/invoke/MethodHandle;I[Ljava/lang/invoke/MethodHandle;)Ljava/lang/invoke/MethodHandle;", OBJ_RET, 1, 4, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/lang/invoke/TMethodType": [
        ("methodType", "(Ljava/lang/Class;)Ljava/lang/invoke/MethodType;", OBJ_RET, 1, 1, PUBLIC | STATIC),
        ("methodType", "(Ljava/lang/Class;Ljava/lang/Class;)Ljava/lang/invoke/MethodType;", OBJ_RET, 1, 2, PUBLIC | STATIC),
        ("methodType", "(Ljava/lang/Class;Ljava/lang/Class;[Ljava/lang/Class;)Ljava/lang/invoke/MethodType;", OBJ_RET, 1, 3, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/lang/invoke/TVarHandle": [
        ("compareAndSet", "([Ljava/lang/Object;)Z", INT_RET, 1, 1, PUBLIC),
        ("getAndSet", "([Ljava/lang/Object;)Ljava/lang/Object;", OBJ_RET, 1, 1, PUBLIC),
        ("setRelease", "([Ljava/lang/Object;)V", VOID_RET, 0, 1, PUBLIC),
        ("storeStoreFence", "()V", VOID_RET, 0, 0, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/lang/TStackWalker": [
        ("getInstance", "(Ljava/util/Set;I)Ljava/lang/StackWalker;", OBJ_RET, 1, 2, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/java/lang/TScopedValue$Carrier": [
        ("run", "(Ljava/lang/Runnable;)V", VOID_RET, 0, 2, PUBLIC),
    ],
    "org/teavm/classlib/java/util/TBase64": [
        ("getMimeDecoder", "()Ljava/util/Base64$Decoder;", OBJ_RET, 1, 0, PUBLIC | STATIC),
        ("getMimeEncoder", "(I[B)Ljava/util/Base64$Encoder;", OBJ_RET, 1, 2, PUBLIC | STATIC),
        ("getMimeEncoder", "()Ljava/util/Base64$Encoder;", OBJ_RET, 1, 0, PUBLIC | STATIC),
    ],
    "org/teavm/classlib/com/mojang/serialization/codecs/TPrimitiveCodec": [
        ("optionalFieldOf", "(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;", bytes([0x01, 0xB0]), 1, 2, 0x0001),
    ],
    "org/teavm/classlib/sun/misc/TUnsafe": [
        ("putLong", "(JJ)V", VOID_RET, 0, 3, PUBLIC),
        ("putLong", "(Ljava/lang/Object;JJ)V", VOID_RET, 0, 4, PUBLIC),
        ("getLong", "(J)J", LONG_RET, 1, 2, PUBLIC),
        ("getLong", "(Ljava/lang/Object;J)J", LONG_RET, 1, 3, PUBLIC),
        ("putObject", "(JLjava/lang/Object;)V", VOID_RET, 0, 3, PUBLIC),
        ("getObject", "(J)Ljava/lang/Object;", OBJ_RET, 1, 2, PUBLIC),
        ("putInt", "(JI)V", VOID_RET, 0, 3, PUBLIC),
        ("getInt", "(J)I", INT_RET, 1, 2, PUBLIC),
    ],
}


def patch_jar(input_jar, output_jar):
    """Patch the teavm-classlib.jar by adding missing methods to .class files"""
    
    total_added = 0
    total_skipped = 0
    
    with zipfile.ZipFile(input_jar, 'r') as zin:
        with zipfile.ZipFile(output_jar, 'w', zipfile.ZIP_DEFLATED) as zout:
            for item in zin.infolist():
                data = zin.read(item.filename)
                
                # Debug: print class files that start with java/lang/Runtime or java/util/UUID
                if item.filename.endswith('.class') and ('Runtime' in item.filename or 'UUID' in item.filename):
                    print(f"  Found: {item.filename}")
                
                # Check if this class needs methods added
                # item.filename is like "java/lang/Runtime.class"
                class_name = item.filename.replace('.class', '')
                if class_name in METHODS_TO_ADD:
                    try:
                        cf = ClassFile(data)
                        added = 0
                        skipped = 0
                        for method_spec in METHODS_TO_ADD[class_name]:
                            name, desc, bytecode, max_stack, max_locals, flags = method_spec
                            if cf.add_method(name, desc, bytecode, max_stack, max_locals, flags):
                                added += 1
                            else:
                                skipped += 1
                        
                        if added > 0:
                            data = cf.serialize()
                            print(f"  {item.filename}: added {added} methods, skipped {skipped}")
                            total_added += added
                        total_skipped += skipped
                    except Exception as e:
                        print(f"  {item.filename}: ERROR - {e}")
                
                zout.writestr(item, data)
    
    print(f"\nTotal: added {total_added} methods, skipped {total_skipped} (already present)")
    return total_added


if __name__ == '__main__':
    input_jar = sys.argv[1] if len(sys.argv) > 1 else '/tmp/teavm-classlib.jar'
    output_jar = sys.argv[2] if len(sys.argv) > 2 else '/tmp/teavm-classlib-patched.jar'
    
    if not os.path.exists(input_jar):
        print(f"Downloading teavm-classlib.jar...")
        import urllib.request
        urllib.request.urlretrieve(
            "https://repo1.maven.org/maven2/org/teavm/teavm-classlib/0.15.0/teavm-classlib-0.15.0.jar",
            input_jar
        )
    
    print(f"Patching {input_jar} -> {output_jar}")
    count = patch_jar(input_jar, output_jar)
    print(f"Done! Patched JAR: {output_jar} ({os.path.getsize(output_jar)} bytes)")
