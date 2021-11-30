package ink.ikx.modularassembly.utils;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockUtils {

    public static boolean isNotAir(BlockPos pos, World world) {
        return world.getBlockState(pos).getMaterial().equals(Material.AIR);
    }

}
