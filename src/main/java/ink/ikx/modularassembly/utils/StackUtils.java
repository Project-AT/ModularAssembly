package ink.ikx.modularassembly.utils;

import hellfirepvp.modularmachinery.common.lib.BlocksMM;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class StackUtils {

    public static boolean isStackFilter(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() != Item.getItemFromBlock(BlocksMM.blockController);
    }

    public static ItemStack getStackFromBlockState(IBlockState state) {
        Block block = state.getBlock();
        return new ItemStack(Item.getItemFromBlock(block), 1, block.getMetaFromState(state));
    }

    public static boolean isAllStacksEqual(List<ItemStack> stacks, List<ItemStack> stacks2) {
        return getAllStacksEqual(stacks, stacks2) != null;
    }

    public static List<ItemStack> getAllStacksEqual(List<ItemStack> stacks, List<ItemStack> stacks2) {
        if (stacks.size() != stacks2.size()) {
            return null;
        }
        for (int i = 0; i < stacks.size(); i++) {
            if (!stacks.get(i).isItemEqual(stacks2.get(i))) {
                return null;
            }
        }
        return stacks;
    }

}
