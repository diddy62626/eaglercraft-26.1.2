package java.lang.management;
public interface MemoryManagerMXBean {
    String getName();
    boolean isValid();
    String[] getMemoryPoolNames();
}