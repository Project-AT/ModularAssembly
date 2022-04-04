package ink.ikx.modularassembly.core.config;

import ink.ikx.modularassembly.Main;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MOD_ID)
public class Configuration {

    @Config.RequiresMcRestart
    @Config.Comment("Set the Item using for auto-assembly. default: <minecraft:stick>")
    public static String AutoAssemblyItem = "<minecraft:stick>";

    @Config.Comment("Set how many ticks to assemble the block once, default: 5")
    public static int TickBlock = 5;

}
