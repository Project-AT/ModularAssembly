package ink.ikx.modularassembly.utils.machine;

import com.google.common.collect.Maps;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;

public class MachineJsonFormatInstance {

    public static final Map<String, MachineJsonFormatInstance> MACHINES = Maps.newHashMap();
    private final String machineName;
    private final List<Parts> machineParts;

    public MachineJsonFormatInstance(String machineName, List<Parts> machineParts) {
        this.machineName = machineName;
        this.machineParts = machineParts;
    }

    public static MachineJsonFormatInstance getOrCreate(String machineName, List<Parts> machineParts) {
        if (MACHINES.containsKey(machineName)) {
            return MACHINES.get(machineName);
        }
        MachineJsonFormatInstance machineJsonFormatInstance = new MachineJsonFormatInstance(machineName, machineParts);
        MACHINES.put(machineName, machineJsonFormatInstance);
        return machineJsonFormatInstance;
    }

    public String getMachineName() {
        return machineName;
    }

    public List<Parts> getMachineParts() {
        return machineParts;
    }

    public static class Parts {

        public final int x;
        public final int y;
        public final int z;
        public final String itemName;
        public final String[] elements;

        public Parts(int x, int y, int z, String itemName, String[] elements) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.itemName = itemName;
            this.elements = elements;
        }

        public BlockPos getBlockPos() {
            return new BlockPos(x, y, z);
        }

    }

}
