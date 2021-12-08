package ink.ikx.modularassembly.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class MiscUtils {

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static ITextComponent translate(int mark) {
        return new TextComponentTranslation("tips.tip" + mark);
    }

    public static boolean areNull(Object... objs) {
        return Arrays.stream(objs).anyMatch(Objects::isNull);
    }
}
