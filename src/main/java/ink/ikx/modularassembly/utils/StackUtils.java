package ink.ikx.modularassembly.utils;

import hellfirepvp.modularmachinery.common.lib.BlocksMM;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.List;
import java.util.Objects;

public class StackUtils {

    public static boolean isNotEmpty(ItemStack stack) {
        return !stack.isEmpty();
    }

    public static boolean isStackFilter(ItemStack stack) {
        return isNotEmpty(stack) && stack.getItem() != Item.getItemFromBlock(BlocksMM.blockController);
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
        return new ItemStack(Item.getItemFromBlock(block), 1, block.getMetaFromState(state));
    }

    public static ItemStack hasStacks(List<ItemStack> inputStacks, List<ItemStack> outputStacks, boolean isRemove) {
        return outputStacks.stream().filter(stack -> isNotEmpty(hasStack(stack, inputStacks, isRemove))).findFirst().orElse(ItemStack.EMPTY);
    }

    public static ItemStack hasStack(ItemStack stack, List<ItemStack> stacks, boolean isRemove) {
        for (ItemStack stackInSlot : stacks) {
            ItemStack copy = stackInSlot.copy();
            if (stackInSlot.isEmpty()) continue;
            if (stackInSlot.isItemEqual(stack)) {
                if (stackInSlot.getCount() >= stack.getCount()) {
                    if (isRemove) stackInSlot.shrink(stack.getCount());
                    return copy;
                }
            } else if (FluidUtil.getFluidHandler(stack) != null && FluidUtil.getFluidHandler(stackInSlot) != null) {
                IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack);
                FluidStack fluidStack = FluidUtil.getFluidContained(stackInSlot);
                FluidStack fluidStack_ = FluidUtil.getFluidContained(stack);

                if (fluidStack != null && Objects.requireNonNull(fluidStack).containsFluid(fluidStack_)) {
                    if (isRemove) Objects.requireNonNull(handler).drain(fluidStack, true);
                    return copy;
                }
            }
        }
        return ItemStack.EMPTY;
    }

}
