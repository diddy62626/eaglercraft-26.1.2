package oshi.hardware;

/**
 * TeaVM stub for oshi HardwareAbstractionLayer.
 */
public class HardwareAbstractionLayer {
    public oshi.hardware.ComputerSystem getComputerSystem() { return new ComputerSystem(); }
    public oshi.hardware.CentralProcessor getProcessor() { return new CentralProcessor(); }
    public oshi.hardware.GlobalMemory getMemory() { return new GlobalMemory(); }
    public java.util.List<oshi.hardware.HWDiskStore> getDiskStores() { return new java.util.ArrayList<>(); }
    public java.util.List<oshi.hardware.NetworkIF> getNetworkIFs() { return new java.util.ArrayList<>(); }
    public java.util.List<oshi.hardware.UsbDevice> getUsbDevices(boolean tree) { return new java.util.ArrayList<>(); }
    public oshi.hardware.Sensors getSensors() { return new Sensors(); }
    public oshi.hardware.PowerSource[] getPowerSources() { return new oshi.hardware.PowerSource[0]; }
}
