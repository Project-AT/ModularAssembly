package ink.ikx.modularassembly.utils;

import java.util.Collection;

public class CollUtils {

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

}
