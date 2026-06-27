package net.minecraft.util.filefix;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.util.worldupdate.UpgradeProgress;
public class FileFixerUpper {
    public static boolean requiresFileFixing(int version) { return false; }
    public static com.mojang.serialization.Dynamic fix(
            LevelStorageSource.LevelStorageAccess access,
            com.mojang.serialization.Dynamic dynamic,
            UpgradeProgress progress) { return dynamic; }
}
