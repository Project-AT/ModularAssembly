package ink.ikx.modularassembly.core;

import com.google.common.collect.Sets;
import ink.ikx.modularassembly.utils.MiscUtil;
import ink.ikx.modularassembly.utils.StackUtil;
import ink.ikx.modularassembly.utils.machine.MachineJsonFormatInstance;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MachineInAssembly {

    public static final Set<MachineInAssembly> WORKING_MACHINE = Sets.newHashSet();

    private final BlockPos pos;
    private final EntityPlayer player;
    private final MachineJsonFormatInstance machineInstance;

    private MachineInAssembly(BlockPos pos, EntityPlayer player, MachineJsonFormatInstance machineInstance) {
        this.pos = pos;
        this.player = player;
        this.machineInstance = machineInstance;
    }

    public static MachineInAssembly of(BlockPos pos, EntityPlayer player, MachineJsonFormatInstance machineInstance) {
        return new MachineInAssembly(pos, player, machineInstance);
    }

    public static Set<MachineInAssembly> getWorkingMachineFromPlayer(EntityPlayer player) {
        return WORKING_MACHINE.stream().filter(m -> m.getPlayer().equals(player)).collect(Collectors.toSet());
    }

    public static boolean create(BlockPos pos, EntityPlayer player, String machineName, boolean isLast) {
        MachineJsonFormatInstance machineInstance = MachineJsonFormatInstance.MACHINES.get(machineName);
        if (machineInstance == null) {
            // in theory this shouldn't be executed
            if (isLast) MiscUtil.sendTranslateToLocalToPlayer(player, "message.modularassembly.machine.error");
            return false;
        }
        MachineInAssembly instance = MachineInAssembly.of(pos, player, machineInstance.copy());
        outer:
        for (MachineJsonFormatInstance.Parts machinePart : machineInstance.getMachineParts()) {
            IBlockState blockState = player.world.getBlockState(machinePart.getBlockPos(pos));
            if (machinePart.matches(blockState)) continue;
            if (!blockState.getMaterial().isReplaceable() || blockState.getMaterial() != Material.AIR) {
                if (isLast)
                    MiscUtil.sendTranslateToLocalToPlayer(player, "message.modularassembly.machine.block_exist");
                return false;
            }
            List<List<ItemStack>> stackList = machinePart.getStackList();
            for (List<ItemStack> stacks : stackList) {
                if (StackUtil.areStacksInInventory(stacks, player.inventory.mainInventory)) continue outer;
            }
            // items not enough in inventory
            if (isLast)
                MiscUtil.sendTranslateToLocalToPlayer(player, "message.modularassembly.machine.inventory_not_enough");
            return false;
        }
        if (WORKING_MACHINE.add(instance)) {
            MiscUtil.sendTranslateToLocalToPlayer(player, "message.modularassembly.machine.start");
        } else {
            MiscUtil.sendTranslateToLocalToPlayer(player, "message.modularassembly.machine.starting");
        }
        return true;
    }

    public void assembly() {
        List<MachineJsonFormatInstance.Parts> machineParts = this.machineInstance.getMachineParts();
        if (machineParts.get(0).assembly(getPlayer(), getPos())) {
            machineParts.remove(0);
        } else {
            WORKING_MACHINE.remove(this);
        }
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

        MachineInAssembly that = (MachineInAssembly) o;

        if (!Objects.equals(pos, that.pos)) return false;
        if (!Objects.equals(player, that.player)) return false;
        return Objects.equals(machineInstance, that.machineInstance);
    }

    @Override
    public int hashCode() {
        int result = pos != null ? pos.hashCode() : 0;
        result = 31 * result + (player != null ? player.hashCode() : 0);
        result = 31 * result + (machineInstance != null ? machineInstance.hashCode() : 0);
        return result;
    }

}
