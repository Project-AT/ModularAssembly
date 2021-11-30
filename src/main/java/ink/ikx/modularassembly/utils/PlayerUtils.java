package ink.ikx.modularassembly.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class PlayerUtils {

    public static boolean hasStacks(EntityPlayer player, List<ItemStack> stacks) {
        return stacks.stream().anyMatch(stack -> hasStack(player, stack));
    }

    public static boolean hasStack(EntityPlayer player, ItemStack stack) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stackInSlot = player.inventory.getStackInSlot(i);
            if (stackInSlot.isEmpty()) continue;
            if (stackInSlot.isItemEqual(stack)) {
                return stackInSlot.getCount() >= stack.getCount();
            }
        }
        return false;
    }

}
