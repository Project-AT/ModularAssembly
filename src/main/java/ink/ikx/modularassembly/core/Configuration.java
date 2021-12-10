package ink.ikx.modularassembly.core;

import ink.ikx.modularassembly.Main;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MOD_ID)
public class Configuration {

    @Config.Comment("Set the Item auto-assembly, default: minecraft:stick")
    public static String itemName = "minecraft:stick";

    @Config.Comment("Set the Item's meta, e.g. 0")
    public static int itemMeta = 0;

    @Config.Comment("Set how many ticks to assemble the block once, default: 5")
    public static int tickBlock = 5;

    @Config.Comment("Set the auto-assembly need all blocks must be in the inventory, default: true")
    public static boolean needAllBlocks = true;

    @Config.Comment("Set whether to skip blocks containing NBTs, default: false")
    public static boolean skipBlockContainNBT = false;

}
