package java.util;
public abstract class TimerTask implements Runnable {
    public abstract void run();
    public boolean cancel() { return false; }
}