package ink.ikx.modularassembly.utils;

import net.minecraftforge.fml.common.Loader;

public class MiscUtil {

    public static final MiscUtil INSTANCE = new MiscUtil();

    public boolean isMoCLoaded() {
        return Loader.isModLoaded("modularcontroller");
    }

}
