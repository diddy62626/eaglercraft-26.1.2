package oshi.hardware;
/**
 * TeaVM stub for oshi GlobalMemory.
 */
public class GlobalMemory {
    public long getAvailable() { return 1024L * 1024 * 1024; }
    public long getTotal() { return 4L * 1024 * 1024 * 1024; }
    public long getPageSize() { return 4096; }
    public java.util.List<PhysicalMemory> getPhysicalMemory() { return new java.util.ArrayList<>(); }
    public VirtualMemory getVirtualMemory() { return new VirtualMemory(); }
}
