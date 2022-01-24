package ink.ikx.modularassembly;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = Main.MOD_ID,
        name = Main.MOD_NAME,
        version = Main.VERSION,
        dependencies = Main.DEPENDENCIES
)
public class Main {

    public static final String MOD_ID = "modularassembly";
    public static final String MOD_NAME = "ModularAssembly";
    public static final String VERSION = "1.0.2";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2847,);" +
            "required-after:crafttweaker;" +
            "required-after:modularmachinery@[1.11.0,);" +
            "after:modularcontroller@[1.2.1,)";

    @Mod.Instance
    public static Main instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    public boolean isMoCLoaded() {
        return Loader.isModLoaded("modularcontroller");
    }

}
