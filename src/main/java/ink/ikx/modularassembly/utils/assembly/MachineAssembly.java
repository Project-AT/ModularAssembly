package ink.ikx.modularassembly.utils.assembly;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import hellfirepvp.modularmachinery.common.util.BlockArray.BlockInformation;
import ink.ikx.modularassembly.core.Configuration;
import ink.ikx.modularassembly.utils.FluidUtils;
import ink.ikx.modularassembly.utils.MiscUtils;
import ink.ikx.modularassembly.utils.StackUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MachineAssembly {

    private final BlockPos pos;
    private final EntityPlayer player;
    private final Map<BlockPos, BlockInformation> pattern;

    public MachineAssembly(BlockPos pos, EntityPlayer player, Map<BlockPos, BlockInformation> pattern) {
        this.pos = pos;
        this.player = player;
        this.pattern = MiscUtils.sortedAndCopy(pattern);
    }

    public boolean isAllItemsContains() {
        Map<BlockPos, List<ItemStack>> needFindStacks = Maps.newHashMap();

        for (Map.Entry<BlockPos, BlockInformation> entry : pattern.entrySet()) {
            if (entry.getValue().matchesState(getWorld().getBlockState(pos.add(entry.getKey())))) continue;

            Pair<List<IBlockState>, List<ItemStack>> listSamplesFromInfo = this.getListSamplesFromInfo(entry);
            if (listSamplesFromInfo.getLeft() == null) {
                return false;
            }
            if (MiscUtils.isNotEmpty(listSamplesFromInfo.getRight())) {
                needFindStacks.put(entry.getKey(), listSamplesFromInfo.getRight());
            }
        }
        List<ItemStack> mainInventoryCopy =
                player.inventory.mainInventory.stream().map(ItemStack::copy).collect(Collectors.toList());

        return needFindStacks.values().stream()
                .allMatch(s -> StackUtils.isNotEmpty(StackUtils.hasStacks(mainInventoryCopy, s, true)));
    }

    public void assembly() {
        Iterator<Map.Entry<BlockPos, BlockInformation>> iterator = pattern.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, BlockInformation> next = iterator.next();
            BlockPos offsetByFacing = pos.add(next.getKey());
            if (next.getValue().matchesState(getWorld().getBlockState(offsetByFacing))) {
                iterator.remove();
                continue;
            }

            if (next.getValue().matchingTag != null && Configuration.skipBlockContainNBT) {
                iterator.remove();
                continue;
            }

            Pair<List<IBlockState>, List<ItemStack>> listSamplesFromInfo = this.getListSamplesFromInfo(next);
            if (listSamplesFromInfo.getLeft() == null) {
                return;
            }

            if (MiscUtils.isNotEmpty(listSamplesFromInfo.getRight()) || !Configuration.needAllBlocks) {
                ItemStack stack = StackUtils.hasStacks(player.inventory.mainInventory, listSamplesFromInfo.getRight(), true);
                if (StackUtils.isNotEmpty(stack)) {
                    int index = StackUtils.getIndex(listSamplesFromInfo.getRight(), stack);
                    if (index == -1) {
                        player.sendMessage(MiscUtils.translate(3));
                        MachineAssemblyManager.removeMachineAssembly(this);
                        return;
                    }

                    IBlockState state = listSamplesFromInfo.getLeft().get(index);
                    if (FluidUtils.isFluidHandler(stack)) {
                        player.addItemStackToInventory(new ItemStack(Items.BUCKET));
                        state = Objects.requireNonNull(FluidUtil.getFluidContained(stack)).getFluid().getBlock().getDefaultState();
                    }
                    getWorld().setBlockState(offsetByFacing, state);
                    getWorld().playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    iterator.remove();
                } else if (!Configuration.needAllBlocks) {
                    iterator.remove();
                    continue;
                } else {
                    player.sendMessage(MiscUtils.translate(4));
                    MachineAssemblyManager.removeMachineAssembly(this);
                }
                break;
            }
        }

        if (pattern.isEmpty()) {
            player.sendMessage(MiscUtils.translate(5));
            MachineAssemblyManager.removeMachineAssembly(this);
        }

    }

    public void buildWithCreative() {
        pattern.entrySet().stream()
                .filter(e -> !e.getValue().matchesState(getWorld().getBlockState(pos.add(e.getKey()))))
                .forEach(e -> getWorld().setBlockState(pos.add(e.getKey()), e.getValue().getSampleState()));
        getWorld().playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        player.sendMessage(MiscUtils.translate(5));
    }

    private Pair<List<IBlockState>, List<ItemStack>> getListSamplesFromInfo(Map.Entry<BlockPos, BlockInformation> entry) {
        IBlockState state = player.world.getBlockState(pos.add(entry.getKey()));
        List<ItemStack> toReturn = Lists.newArrayList();
        if (!entry.getValue().matchesState(state) && MiscUtils.isNotAirOrCanReplaced(pos.add(entry.getKey()), getWorld()) && MiscUtils.isNotLiquid(state))
            return Pair.of(null, toReturn);
        try {
            Field fileSamples = entry.getValue().getClass().getDeclaredField("samples");
            fileSamples.setAccessible(true);
            //noinspection unchecked
            List<IBlockState> samples = (List<IBlockState>) fileSamples.get(entry.getValue());
            for (IBlockState sample : samples) {
                ItemStack stack = StackUtils.getStackFromBlockState(sample);
                if (StackUtils.isStackFilter(stack)) toReturn.add(stack);
            }
            return Pair.of(samples, toReturn);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return Pair.of(null, toReturn);
    }

    public boolean isFilter() {
        boolean toReturn = getWorld().isBlockLoaded(pos) && MiscUtils.isNotAirOrCanReplaced(pos, getWorld()) && Objects.nonNull(player);
        if (!toReturn) MachineAssemblyManager.removeMachineAssembly(this);
        return toReturn;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public World getWorld() {
        return player.world;
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
