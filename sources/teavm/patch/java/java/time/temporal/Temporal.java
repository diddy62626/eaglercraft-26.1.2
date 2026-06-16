package java.time.temporal;
public interface Temporal extends TemporalAdjuster {
    boolean isSupported(TemporalUnit unit);
}