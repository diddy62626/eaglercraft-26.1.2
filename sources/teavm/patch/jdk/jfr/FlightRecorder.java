package jdk.jfr;

public final class FlightRecorder {
    public static void register(Class<? extends Event> eventClass) {}
    public static void unregister(Class<? extends Event> eventClass) {}
    public static void addPeriodicEvent(Class<? extends Event> eventClass, Runnable hook) {}
    public static void removePeriodicEvent(Runnable hook) {}
    public static FlightRecorder getFlightRecorder() { return new FlightRecorder(); }
    public static boolean isAvailable() { return false; }
    public static boolean isInitialized() { return false; }
    public static void initialize() {}
    public java.util.List<Recording> getRecordings() { return new java.util.ArrayList<>(); }
    public void takeSnapshot() {}
}
