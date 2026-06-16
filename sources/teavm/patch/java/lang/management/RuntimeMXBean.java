package java.lang.management;
public class RuntimeMXBean {
    public String getVmName() { return "TeaVM"; }
    public String getVmVersion() { return "0.15"; }
    public java.util.List<String> getInputArguments() { return java.util.Collections.emptyList(); }
    public long getStartTime() { return 0; }
    public long getUptime() { return 0; }
}