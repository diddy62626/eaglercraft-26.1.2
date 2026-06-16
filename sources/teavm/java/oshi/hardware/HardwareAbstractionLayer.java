package oshi.hardware;
/**
 * TeaVM stub for oshi HardwareAbstractionLayer.
 */
public class HardwareAbstractionLayer {
    public ComputerSystem getComputerSystem() { return new ComputerSystem(); }
    public CentralProcessor getProcessor() { return new CentralProcessor(); }
    public GlobalMemory getMemory() { return new GlobalMemory(); }
    public oshi.software.os.OperatingSystem getOperatingSystem() { return null; }
    public java.util.List<PowerSource> getPowerSources() { return new java.util.ArrayList<>(); }
    public java.util.List<HWDiskStore> getDiskStores() { return new java.util.ArrayList<>(); }
    public java.util.List<NetworkIF> getNetworkIFs() { return new java.util.ArrayList<>(); }
    public java.util.List<NetworkIF> getNetworkIFs(boolean includeLocalInterfaces) { return new java.util.ArrayList<>(); }
    public Displays getDisplays() { return null; }
    public Sensors getSensors() { return new Sensors(); }
    public java.util.List<UsbDevice> getUsbDevices(boolean tree) { return new java.util.ArrayList<>(); }
    public java.util.List<SoundCard> getSoundCards() { return new java.util.ArrayList<>(); }
    public java.util.List<GraphicsCard> getGraphicsCards() { return new java.util.ArrayList<>(); }
}

class Displays {}

class SoundCard {
    public String getName() { return "Web Audio"; }
    public String getCodec() { return "Browser"; }
}

class GraphicsCard {
    private final String name;
    private final String deviceId;
    private final String vendor;
    private final String versionInfo;
    private final long vRam;

    public GraphicsCard() {
        this.name = "WebGL2";
        this.deviceId = "webgl2";
        this.vendor = "Browser";
        this.versionInfo = "WebGL 2.0";
        this.vRam = 0L;
    }

    public GraphicsCard(String name, String deviceId, String vendor, String versionInfo, long vRam) {
        this.name = name;
        this.deviceId = deviceId;
        this.vendor = vendor;
        this.versionInfo = versionInfo;
        this.vRam = vRam;
    }

    public String getName() { return name; }
    public String getDeviceId() { return deviceId; }
    public String getVendor() { return vendor; }
    public String getVersionInfo() { return versionInfo; }
    public long getVRam() { return vRam; }
}
