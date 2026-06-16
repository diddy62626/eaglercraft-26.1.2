package java.lang;
public interface ProcessHandle {
    long pid();
    boolean destroy();
    boolean destroyForcibly();
    boolean isAlive();
}