package oshi.hardware;
/**
 * TeaVM stub for oshi CentralProcessor.
 */
public class CentralProcessor {
    public ProcessorIdentifier getProcessorIdentifier() { return new ProcessorIdentifier(); }
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

    public static class ProcessorIdentifier {
        public String getName() { return "EaglerCraft Browser CPU"; }
        public String getIdentifier() { return "eaglercraft"; }
        public String getMicroarchitecture() { return "browser"; }
        public boolean isCpu64bit() { return true; }
        public long getProcessorId() { return 0L; }
        public String getVendor() { return "EaglerCraft"; }
        public long getVendorFreq() { return 0L; }
        public int getLogicalProcessorCount() { return 1; }
        public int getPhysicalProcessorCount() { return 1; }
        public int getPhysicalPackageCount() { return 1; }
        public long getFamily() { return 0L; }
        public long getModel() { return 0L; }
        public long getStepping() { return 0L; }
    }

    public static class LogicalProcessor {
        public int getPhysicalProcessorNumber() { return 0; }
        public int getLogicalProcessorNumber() { return 0; }
        public int getPhysicalPackageNumber() { return 0; }
        public int getNumaNode() { return 0; }
    }

    public static class Thread {
        public long getThreadId() { return 0L; }
        public String getName() { return "main"; }
        public java.util.Map<java.lang.String, java.lang.Double> getGroup() {
            return new java.util.HashMap<>();
        }
    }
}
