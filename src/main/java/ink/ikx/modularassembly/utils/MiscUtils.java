package ink.ikx.modularassembly.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidBase;

import java.util.*;

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

    public static boolean isNotAirOrCanReplaced(BlockPos pos, World world) {
        return !world.isAirBlock(pos) && !(world.getBlockState(pos).getBlock() instanceof IPlantable
                && world.getTileEntity(pos) == null);
    }

    public static boolean isLiquid(IBlockState state) {
        Block block = state.getBlock();
        return block instanceof BlockFluidBase || block == Blocks.WATER || block == Blocks.LAVA;
    }

    public static boolean isNotLiquid(IBlockState state) {
        return !isLiquid(state);
    }

    public static Map<BlockPos, BlockArray.BlockInformation> sortedAndCopy(Map<BlockPos, BlockArray.BlockInformation> map) {
        List<BlockPos> liquidList = Lists.newArrayList();
        List<BlockPos> noFullBlockList = Lists.newArrayList();
        Map<BlockPos, BlockArray.BlockInformation> toReturn = Maps.newLinkedHashMap();

        for (Map.Entry<BlockPos, BlockArray.BlockInformation> entry : map.entrySet()) {
            if (entry.getValue().getSampleState().isFullBlock()) {
                noFullBlockList.add(entry.getKey());
                continue;
            }
            if (isLiquid(entry.getValue().getSampleState())) {
                liquidList.add(entry.getKey());
                continue;
            }
            toReturn.put(new BlockPos(entry.getKey()), entry.getValue().copy());
        }

        noFullBlockList.forEach(pos -> toReturn.put(new BlockPos(pos), map.get(pos).copy()));
        liquidList.forEach(pos -> toReturn.put(new BlockPos(pos), map.get(pos).copy()));

        return toReturn;
    }

}
