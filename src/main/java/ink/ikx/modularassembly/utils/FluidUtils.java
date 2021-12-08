package ink.ikx.modularassembly.utils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.util.Arrays;
import java.util.Objects;

public class FluidUtils {

    public static boolean isFluidHandler(ItemStack stack) {
        return StackUtils.isNotEmpty(stack) && Objects.nonNull(FluidUtil.getFluidHandler(stack));
    }

    public static boolean areFluidHandler(ItemStack... stacks) {
        return Arrays.stream(stacks).allMatch(FluidUtils::isFluidHandler);
    }

    public static FluidStack getFluidFromHandler(ItemStack stack) {
        if (isFluidHandler(stack)) {
            IFluidTankProperties[] tank = FluidUtil.getFluidHandler(stack).getTankProperties();
            return tank.length <= 0 ? null : tank[0].getContents();
        }
        return null;
    }

    public static boolean equalsFluidFromStack(ItemStack stackA, ItemStack stackB) {
        FluidStack fluidA = getFluidFromHandler(stackA);
        FluidStack fluidB = getFluidFromHandler(stackB);
        if (MiscUtils.areNull(fluidA, fluidB)) return false;
        return fluidA.containsFluid(fluidB);
    }

    public static void drainStackToHandler(ItemStack stack) {
        if (stack.isEmpty() || FluidUtil.getFluidHandler(stack) == null)
            return;
        IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
        fluidHandler.drain(1000, true);
    }

}
