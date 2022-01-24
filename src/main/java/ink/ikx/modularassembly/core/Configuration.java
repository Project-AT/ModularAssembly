package ink.ikx.modularassembly.core;

import ink.ikx.modularassembly.Main;
import net.minecraftforge.common.config.Config;

@Config(modid = Main.MOD_ID)
public class Configuration {

    @Config.RequiresMcRestart
    @Config.LangKey("config.modularassembly.autoAssemblyItem.comment")
    @Config.Comment("Set the Item using for auto-assembly. default: <minecraft:stick>")
    public static String AutoAssemblyItem = "<minecraft:stick>";

}
