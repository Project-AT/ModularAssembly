package ink.ikx.modularassembly.utils;

import hellfirepvp.modularmachinery.common.block.BlockController;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;

import java.util.List;

public class StackUtils {

    public static boolean isNotEmpty(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() != Items.MILK_BUCKET;
    }

    public static boolean isStackFilter(ItemStack stack) {
        return isNotEmpty(stack) && !(Block.getBlockFromItem(stack.getItem()) instanceof BlockController);
    }

    public static ItemStack getStackFromBlockState(IBlockState state) {
        Block block = state.getBlock();
        if (block instanceof BlockFluidBase) {
            return FluidUtil.getFilledBucket(new FluidStack(((BlockFluidBase) block).getFluid(), 1000));
        } else if (block instanceof BlockLiquid) {
            Material m = state.getMaterial();
            if (m == Material.LAVA) {
                return new ItemStack(Items.LAVA_BUCKET);
            } else if (m == Material.WATER) {
                return new ItemStack(Items.WATER_BUCKET);
            }
        }
        return new ItemStack(Item.getItemFromBlock(block), 1, block.damageDropped(state));
    }

    public static ItemStack hasStacks(List<ItemStack> inputStacks, List<ItemStack> outputStacks, boolean isRemove) {
        return outputStacks.stream().filter(stack -> isNotEmpty(hasStack(stack, inputStacks, isRemove))).findFirst().orElse(ItemStack.EMPTY);
    }

    public static int getIndex(List<ItemStack> stacks, ItemStack stack) {
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).isItemEqual(stack))
                return i;
        }
        return -1;
    }

    public static ItemStack hasStack(ItemStack stack, List<ItemStack> stacks, boolean isRemove) {
        for (ItemStack stackInSlot : stacks) {
            ItemStack copy = stackInSlot.copy();
            if (stackInSlot.isEmpty()) continue;
            if (FluidUtils.areFluidHandler(stack, stackInSlot)) {
                if (FluidUtils.equalsFluidFromStack(stackInSlot, stack)) {
                    if (stackInSlot.getItem() instanceof ItemBucket || stackInSlot.getItem() instanceof UniversalBucket) {
                        if (isRemove) stackInSlot.shrink(stack.getCount());
                        return copy;
                    }
                    return ItemStack.EMPTY;
                }
            } else if (stackInSlot.isItemEqual(stack)) {
                if (stackInSlot.getCount() >= stack.getCount()) {
                    if (isRemove) stackInSlot.shrink(stack.getCount());
                    return copy;
                }
            }
        }
        return ItemStack.EMPTY;
    }

}
