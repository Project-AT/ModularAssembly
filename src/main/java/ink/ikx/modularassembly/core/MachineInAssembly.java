package ink.ikx.modularassembly.core;

import com.google.common.collect.Lists;
import ink.ikx.modularassembly.utils.StackUtil;
import ink.ikx.modularassembly.utils.machine.MachineJsonFormatInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class MachineInAssembly {

    public static final List<MachineInAssembly> WORKING_MACHINE = Lists.newArrayList();

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

    public static void createAfterCheck(BlockPos pos, EntityPlayer player, MachineJsonFormatInstance machineInstance) {
        outer:
        for (MachineJsonFormatInstance.Parts machinePart : machineInstance.getMachineParts()) {
            Pair<List<ItemStack>, Integer> stackList = machinePart.getStackList();
            if (stackList.getRight() == 1) continue;
            for (ItemStack stack : stackList.getLeft()) {
                if (StackUtil.isStackInInventory(stack, player.inventory.mainInventory)) continue outer;
            }
            return;
        }

        MachineInAssembly machine = MachineInAssembly.of(pos, player, machineInstance);

        if (WORKING_MACHINE.contains(machine)) {
            return;
        }

        WORKING_MACHINE.add(machine);
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
