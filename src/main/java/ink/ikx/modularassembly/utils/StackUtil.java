package ink.ikx.modularassembly.utils;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.brackets.BracketHandlerItem;
import crafttweaker.mc1120.data.StringIDataParser;
import ink.ikx.modularassembly.core.config.Configuration;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public enum StackUtil {

    INSTANCE;

    public static final ItemStack AUTO_ASSEMBLY_ITEM = StackUtil.INSTANCE.strToStack(Configuration.AutoAssemblyItem);

    public ItemStack strToStack(String str) {
        String itemStr = StringUtils.substringBetween(str, "<", ">");
        if (itemStr == null) return ItemStack.EMPTY;
        String[] split = itemStr.split(":");
        if (split.length == 0) return ItemStack.EMPTY;

        Pair<String, Integer> stack = Pair.of(split[0] + ":" + split[1], split.length > 2 ? Integer.parseInt(split[2]) : 0);

        String iData = StringUtils.substringBetween(Configuration.AutoAssemblyItem, ".withTag(", ")");
        IItemStack toReturn = BracketHandlerItem.getItem(stack.getLeft(), stack.getValue());

        if (iData != null) {
            toReturn.mutable().withTag(StringIDataParser.parse(iData), true);
        }
        return CraftTweakerMC.getItemStack(toReturn);
    }

}
