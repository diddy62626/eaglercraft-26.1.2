package java.time.temporal;
public interface TemporalAdjuster {
    Temporal adjustInto(Temporal temporal);
}