package oshi.hardware;
/**
 * TeaVM stub for oshi CentralProcessor.
 */
public class CentralProcessor {
    public String getProcessorIdentifier() { return "BrowserCPU"; }
    public String getName() { return "Browser CPU"; }
    public int getLogicalProcessorCount() { return 1; }
    public int getPhysicalProcessorCount() { return 1; }
    public int getPhysicalPackageCount() { return 1; }
    public long getMaxFreq() { return 1000000000L; }
    public long[] getCurrentFreq() { return new long[]{1000000000L}; }
    public long getSystemCpuLoadBetweenTicks() { return 0; }
    public double getSystemCpuLoad() { return 0; }
    public double[] getProcessorCpuLoadBetweenTicks() { return new double[]{0}; }
    public double[] getProcessorCpuLoad() { return new double[]{0}; }
    public long getSystemUptime() { return 0; }
    public long getContextSwitches() { return 0; }
    public long[] getSystemCpuLoadTicks() { return new long[]{0,0,0,0}; }
    public long[][] getProcessorCpuLoadTicks() { return new long[][]{{0,0,0,0}}; }
    public double getSystemLoadAverage() { return 0; }
    public double[] getSystemLoadAverage(int nelem) { return new double[nelem]; }
    public long[][] getProcessorCpuLoadTicks(int cpu) { return new long[][]{{0,0,0,0}}; }
}
