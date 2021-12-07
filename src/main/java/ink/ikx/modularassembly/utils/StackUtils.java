package ink.ikx.modularassembly.utils;

import hellfirepvp.modularmachinery.common.lib.BlocksMM;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class StackUtils {

    public static boolean isNotEmpty(ItemStack stack) {
        return !stack.isEmpty();
    }

    public static boolean isStackFilter(ItemStack stack) {
        return isNotEmpty(stack) && stack.getItem() != Item.getItemFromBlock(BlocksMM.blockController);
    }

    public static ItemStack getStackFromBlockState(IBlockState state) {
        Block block = state.getBlock();
        return new ItemStack(Item.getItemFromBlock(block), 1, block.getMetaFromState(state));
    }

    public static ItemStack hasStacks(List<ItemStack> inputStacks, List<ItemStack> outputStacks, boolean isRemove) {
        return outputStacks.stream().filter(stack -> isNotEmpty(hasStack(stack, inputStacks, isRemove))).findFirst().orElse(ItemStack.EMPTY);
    }

    public static ItemStack hasStack(ItemStack stack, List<ItemStack> stacks, boolean isRemove) {
        for (ItemStack stackInSlot : stacks) {
            if (stackInSlot.isEmpty()) continue;
            if (stackInSlot.isItemEqual(stack)) {
                if (stackInSlot.getCount() >= stack.getCount()) {
                    ItemStack copy = stackInSlot.copy();
                    if (isRemove) stackInSlot.shrink(stack.getCount());
                    return copy;
                }
            }
        }
        return ItemStack.EMPTY;
    }

}
