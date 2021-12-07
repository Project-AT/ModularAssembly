package ink.ikx.modularassembly.utils.assembly;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;
import java.util.stream.Collectors;

public class MachineAssemblyManager {

    private static final Set<MachineAssembly> MACHINE_ASSEMBLY_LIST = Sets.newHashSet();

    public static void addMachineAssembly(MachineAssembly machineAssembly) {
        MACHINE_ASSEMBLY_LIST.add(machineAssembly);
    }

    public static boolean checkMachineExist(MachineAssembly machineAssembly) {
        return MACHINE_ASSEMBLY_LIST.contains(machineAssembly);
    }

    public static Set<MachineAssembly> getMachineAssemblyListFromPlayer(EntityPlayer player) {
        return MACHINE_ASSEMBLY_LIST.stream().filter(a -> a.getPlayer().equals(player)).collect(Collectors.toSet());
    }

    public static void removeMachineAssembly(MachineAssembly machineAssembly) {
        MACHINE_ASSEMBLY_LIST.remove(machineAssembly);
    }

    public static Set<MachineAssembly> getMachineAssemblyList() {
        return MACHINE_ASSEMBLY_LIST;
    }

}
