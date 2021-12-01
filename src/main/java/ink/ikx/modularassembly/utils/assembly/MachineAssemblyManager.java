package ink.ikx.modularassembly.utils.assembly;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.stream.Collectors;

public class MachineAssemblyManager {

    private static final List<MachineAssembly> MACHINE_ASSEMBLY_LIST = Lists.newArrayList();

    public static boolean addMachineAssembly(MachineAssembly machineAssembly) {
        if (!MACHINE_ASSEMBLY_LIST.contains(machineAssembly)) {
            MACHINE_ASSEMBLY_LIST.add(machineAssembly);
            return true;
        }
        return false;
    }

    public static MachineAssembly getMachineAssemblyFromBlockPos(BlockPos pos) {
        return MACHINE_ASSEMBLY_LIST.stream().filter(a -> a.getPos().equals(pos)).findFirst().orElse(null);
    }

    public static List<MachineAssembly> getMachineAssemblyListFromPlayer(EntityPlayer player) {
        return MACHINE_ASSEMBLY_LIST.stream().filter(a -> a.getPlayer().equals(player)).collect(Collectors.toList());
    }

    public static List<MachineAssembly> getMachineAssemblyList() {
        return MACHINE_ASSEMBLY_LIST;
    }

}
