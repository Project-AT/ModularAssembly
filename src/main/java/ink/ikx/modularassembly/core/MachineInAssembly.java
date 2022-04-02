package ink.ikx.modularassembly.core;

import com.google.common.collect.Sets;
import ink.ikx.modularassembly.utils.MiscUtil;
import ink.ikx.modularassembly.utils.StackUtil;
import ink.ikx.modularassembly.utils.machine.MachineJsonFormatInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Set;

public class MachineInAssembly {

    public static final Set<MachineInAssembly> WORKING_MACHINE = Sets.newHashSet();

    private final BlockPos pos;
    private final EntityPlayer player;
    private final MachineJsonFormatInstance machineInstance;

    public MachineInAssembly(BlockPos pos, EntityPlayer player, MachineJsonFormatInstance machineInstance) {
        this.pos = pos;
        this.player = player;
        this.machineInstance = machineInstance;
    }

    public static MachineInAssembly of(BlockPos pos, EntityPlayer player, MachineJsonFormatInstance machineInstance) {
        return new MachineInAssembly(pos, player, machineInstance);
    }

    public static boolean create(BlockPos pos, EntityPlayer player, String machineName) {
        MachineJsonFormatInstance machineInstance = MachineJsonFormatInstance.MACHINES.get(machineName);
        if (machineInstance == null) {
            // in theory this shouldn't be executed
            MiscUtil.sendTranslateToLocalToPlayer(player, "");
            return false;
        }
        MachineInAssembly instance = MachineInAssembly.of(pos, player, machineInstance);
        outer:
        for (MachineJsonFormatInstance.Parts machinePart : machineInstance.getMachineParts()) {
            if (machinePart.matches(player.world.getBlockState(machinePart.getBlockPos(pos)))) continue;
            List<List<ItemStack>> stackList = machinePart.getStackList();
            for (List<ItemStack> stacks : stackList) {
                if (StackUtil.areStacksInInventory(stacks, player.inventory.mainInventory)) continue outer;
            }
            // items not enough in inventory
            MiscUtil.sendTranslateToLocalToPlayer(player, "");
            return false;
        }
        return WORKING_MACHINE.add(instance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MachineInAssembly that = (MachineInAssembly) o;

        if (!pos.equals(that.pos)) return false;
        return player.equals(that.player);
    }

    @Override
    public int hashCode() {
        int result = pos.hashCode();
        result = 31 * result + player.hashCode();
        return result;
    }

}
