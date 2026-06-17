package oshi.hardware;
/**
 * TeaVM stub for oshi PowerSource.
 */
public class PowerSource {
    public String getName() { return "Browser"; }
    public double getRemainingCapacity() { return 1.0; }
    public double getTimeRemainingEstimated() { return Double.POSITIVE_INFINITY; }
    public double getTimeRemainingInstant() { return Double.POSITIVE_INFINITY; }
    public boolean isCharging() { return true; }
    public boolean isPresent() { return false; }
    public int getCapacityUnits() { return 0; }
    public double getCurrentCapacity() { return 100; }
    public double getMaxCapacity() { return 100; }
    public double getDesignCapacity() { return 100; }
    public double getPowerUsageRate() { return 0; }
    public double getAmperage() { return 0; }
    public double getVoltage() { return 0; }
    public double getTemperature() { return 0; }
    public int getCycleCount() { return 0; }
    public String getManufacturer() { return "Browser"; }
    public String getDeviceName() { return ""; }
    public String getChemistry() { return "Li-ion"; }
    public java.util.Date getManufactureDate() { return new java.util.Date(0); }
}
