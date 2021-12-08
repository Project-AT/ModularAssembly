package ink.ikx.modularassembly;

import ink.ikx.modularassembly.core.ModularMachineryEvent;
import net.minecraftforge.common.MinecraftForge;
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
    public static final String VERSION = "1.0.0";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2847,);required-after:modularmachinery";

    @Mod.Instance
    public static Main instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // I know it's a bit dumb
        // But if I don't do this, there nothing in the main class,
        // and it feels even weirder
        MinecraftForge.EVENT_BUS.register(ModularMachineryEvent.INSTANCE);
    }

    public boolean isMoCLoaded() {
        // Just to make sure it's loaded
        return Loader.isModLoaded("modularcontroller");
    }

}
