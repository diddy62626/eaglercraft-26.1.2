package oshi.hardware;
/**
 * TeaVM stub for oshi HardwareAbstractionLayer.
 */
public class HardwareAbstractionLayer {
    public ComputerSystem getComputerSystem() { return new ComputerSystem(); }
    public CentralProcessor getProcessor() { return new CentralProcessor(); }
    public GlobalMemory getMemory() { return new GlobalMemory(); }
    public oshi.software.os.OperatingSystem getOperatingSystem() { return null; }
    public PowerSource[] getPowerSources() { return new PowerSource[0]; }
    public HWDiskStore[] getDiskStores() { return new HWDiskStore[0]; }
    public NetworkIF[] getNetworkIFs() { return new NetworkIF[0]; }
    public NetworkIF[] getNetworkIFs(boolean includeLocalInterfaces) { return new NetworkIF[0]; }
    public Displays getDisplays() { return null; }
    public Sensors getSensors() { return new Sensors(); }
    public UsbDevice[] getUsbDevices(boolean tree) { return new UsbDevice[0]; }
    public SoundCard[] getSoundCards() { return new SoundCard[0]; }
    public GraphicsCard[] getGraphicsCards() { return new GraphicsCard[0]; }
}

// Stub GraphicsCard class
class GraphicsCard {
    public String getName() { return "WebGL2"; }
    public String getDeviceId() { return "webgl2"; }
    public String getVendor() { return "Browser"; }
    public String getVersionInfo() { return "WebGL 2.0"; }
    public long getVRam() { return 0L; }
}

class Displays {}
class SoundCard {}
