package java.util;
public class Timer {
    public Timer() {}
    public Timer(String name) {}
    public void schedule(TimerTask task, long delay) {}
    public void schedule(TimerTask task, long delay, long period) {}
    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {}
    public void cancel() {}
}