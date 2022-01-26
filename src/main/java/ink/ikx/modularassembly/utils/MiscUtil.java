package ink.ikx.modularassembly.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Loader;

public enum MiscUtil {

    INSTANCE;

    public boolean isMoCLoaded() {
        return Loader.isModLoaded("modularcontroller");
    }

    @SuppressWarnings("deprecation")
    public void sendTranslateToLocalToPlayer(EntityPlayer player, String langKey) {
        player.sendMessage(new TextComponentString(I18n.translateToLocal(langKey)));
    }

}
