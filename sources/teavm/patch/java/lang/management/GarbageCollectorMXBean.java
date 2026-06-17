package java.lang.management;
public interface GarbageCollectorMXBean extends MemoryManagerMXBean {
    long getCollectionCount();
    long getCollectionTime();
}