package ink.ikx.modularassembly.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;

import java.util.Collection;

public class MiscUtils {

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    @SuppressWarnings("deprecation")
    public static ITextComponent i18nMessage(int mark) {
        return new TextComponentString(I18n.translateToLocal("tips.tip" + mark));
    }

}
