package ink.ikx.modularassembly.utils.assembly;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import hellfirepvp.modularmachinery.common.block.BlockController;
import hellfirepvp.modularmachinery.common.util.BlockArray.BlockInformation;
import hellfirepvp.modularmachinery.common.util.MiscUtils;
import ink.ikx.modularassembly.utils.CollUtils;
import ink.ikx.modularassembly.utils.StackUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MachineAssembly {

    private final BlockPos pos;
    private final EntityPlayer player;
    private final Map<BlockPos, BlockInformation> pattern;
    private final Map<BlockPos, List<ItemStack>> needFindStacks = Maps.newHashMap();

    public MachineAssembly(BlockPos pos, EntityPlayer player, Map<BlockPos, BlockInformation> pattern) {
        this.pos = pos;
        this.player = player;
        this.pattern = pattern;
    }

    public boolean isAllItemsContains() {
        for (Map.Entry<BlockPos, BlockInformation> entry : pattern.entrySet()) {
            List<ItemStack> listSamplesFromInfo = this.getListSamplesFromInfo(entry);
            if (listSamplesFromInfo.size() == 1 && listSamplesFromInfo.get(0).getItem() == Items.APPLE) {
                return false;
            }
            if (CollUtils.isNotEmpty(listSamplesFromInfo)) {
                this.needFindStacks.put(entry.getKey(), listSamplesFromInfo);
            }
        }
        List<ItemStack> mainInventoryCopy =
                player.inventory.mainInventory.stream().map(ItemStack::copy).collect(Collectors.toList());

        return needFindStacks.values().stream()
                .allMatch(s -> StackUtils.hasStacks(mainInventoryCopy, s, true));
    }

    private List<ItemStack> getListSamplesFromInfo(Map.Entry<BlockPos, BlockInformation> entry) {
        IBlockState state = player.world.getBlockState(getOffsetByFacing(entry.getKey()));
        List<ItemStack> toReturn = Lists.newArrayList(new ItemStack(Items.APPLE));
        if (!entry.getValue().matchesState(state) && state.getMaterial() != Material.AIR) return toReturn;
        try {
            Field fileSamples = entry.getValue().getClass().getDeclaredField("samples");
            fileSamples.setAccessible(true);
            //noinspection unchecked
            List<IBlockState> samples = (List<IBlockState>) fileSamples.get(entry.getValue());
            toReturn = samples.stream()
                    .map(StackUtils::getStackFromBlockState)
                    .filter(StackUtils::isStackFilter)
                    .collect(Collectors.toList());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private BlockPos getOffsetByFacing(BlockPos blockPos) {
        EnumFacing facing = EnumFacing.NORTH;
        EnumFacing controllerFacing = player.world.getBlockState(pos).getValue(BlockController.FACING);
        while (facing != controllerFacing) {
            blockPos = MiscUtils.rotateYCCW(blockPos);
            facing = facing.rotateYCCW();
        }
        return pos.add(blockPos);
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MachineAssembly that = (MachineAssembly) o;

        return Objects.equals(pos, that.pos);
    }

    @Override
    public int hashCode() {
        return pos != null ? pos.hashCode() : 0;
    }

}
