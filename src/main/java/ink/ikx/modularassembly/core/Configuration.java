package ink.ikx.modularassembly.core;

import ink.ikx.modularassembly.Main;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MOD_ID)
public class Configuration {

    @Config.Comment("Set the Item auto-assembly, e.g. minecraft:stick")
    public static String itemName = "minecraft:stick";

    @Config.Comment("Set the Item's meta, e.g. 0")
    public static int itemMeta = 0;

}
