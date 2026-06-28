package net.lax1dude.eaglercraft.v2_6.teavm_plugin;

import org.teavm.vm.spi.TeaVMHost;
import org.teavm.vm.spi.TeaVMPlugin;

/**
 * EaglerCraft TeaVM Plugin.
 *
 * Adds missing methods to existing java.base classes that TeaVM's classlib
 * doesn't provide. This is necessary because patches via --patch-module are
 * ignored by TeaVM for classes that already exist in its classlib.
 */
public class EaglerCraftTeaVMPlugin implements TeaVMPlugin {

    @Override
    public void install(TeaVMHost host) {
        host.add(new MissingMethodTransformer());
        host.add(new FileSystemProviderTransformer());
    }
}
