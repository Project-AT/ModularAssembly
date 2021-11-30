package ink.ikx.modularassembly;

import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = Main.MOD_ID,
        name = Main.MOD_NAME,
        version = Main.VERSION
)
public class Main {

    public static final String MOD_ID = "modularassembly";
    public static final String MOD_NAME = "ModularAssembly";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MOD_ID)
    public static Main INSTANCE;

}
