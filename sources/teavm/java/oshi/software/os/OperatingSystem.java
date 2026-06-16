package oshi.software.os;

/**
 * TeaVM stub for oshi OperatingSystem.
 */
public class OperatingSystem {
    public String getManufacturer() { return "Browser"; }
    public String getFamily() { return "Web"; }
    public oshi.software.os.OSVersionInfo getVersionInfo() { return new OSVersionInfo(); }
    public String getVersion() { return "1.0"; }
    public String getBuildNumber() { return ""; }
    public String getCodeName() { return ""; }
    public int getProcessId() { return 0; }
    public int getProcessCount() { return 0; }
    public int getThreadCount() { return 0; }
    public int getBitness() { return 32; }
    public boolean isElevated() { return false; }
    public java.util.List<OSProcess> getProcesses() { return new java.util.ArrayList<>(); }
    public java.util.List<OSProcess> getProcesses(java.util.function.Predicate<OSProcess> filter, java.util.Comparator<OSProcess> sort, int limit) { return new java.util.ArrayList<>(); }
    public OSProcess getProcess(int pid) { return null; }
    public java.util.List<OSService> getServices() { return new java.util.ArrayList<>(); }
    public OSDesktopEnvironment getDesktopEnvironment() { return null; }
    public FileSystem getFileSystem() { return new FileSystem(); }
    public InternetProtocolStats getInternetProtocolStats() { return new InternetProtocolStats(); }
    public NetworkParams getNetworkParams() { return new NetworkParams(); }
    public java.util.List<OSNetworkInterface> getNetworkInterfaces() { return new java.util.ArrayList<>(); }
    public java.util.List<OSScreen> getScreens() { return new java.util.ArrayList<>(); }
    public java.util.List<OSUser> getUsers() { return new java.util.ArrayList<>(); }
    public OSUser getUser(String userName) { return null; }
    public long getSystemUptime() { return 0; }
    public long getSystemBootTime() { return 0; }
}
