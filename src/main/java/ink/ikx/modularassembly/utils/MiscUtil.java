package ink.ikx.modularassembly.utils;

import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.brackets.BracketHandlerBlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.lang3.StringUtils;

public class MiscUtil {

    public static boolean isMoCLoaded() {
        return Loader.isModLoaded("modularcontroller");
    }

    public static IBlockState strToState(String str) {
        String stateStr = StringUtils.substringBetween(str, "<", ">");
        if (stateStr == null) return null;
        String[] split = stateStr.split(":");
        if (split.length < 3) return null;

        return CraftTweakerMC.getBlockState(BracketHandlerBlockState.getBlockState(split[1] + ":" + split[2], split.length > 3 ? split[3] : ""));
    }

    @SuppressWarnings("deprecation")
    public static void sendTranslateToLocalToPlayer(EntityPlayer player, String langKey) {
        player.sendMessage(new TextComponentString(I18n.translateToLocal(langKey)));
    }

}
