package ink.ikx.modularassembly.utils.machine;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ikx.modularassembly.utils.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        public final String itemStack;
        public final String[] elements;

        public Parts(int x, int y, int z, String itemStack, String[] elements) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.itemStack = itemStack;
            this.elements = elements;
        }

        public BlockPos getBlockPos() {
            return new BlockPos(x, y, z);
        }

        public Pair<List<ItemStack>, Integer> getStackList() {
            if (!StringUtils.isBlank(itemStack)) {
                return Pair.of(Lists.newArrayList(StackUtil.strToStack(itemStack)), 1);
            }

            List<ItemStack> toReturn = Lists.newArrayList(StackUtil.strToStack(this.itemStack));

            if (toReturn.isEmpty()) {
                toReturn = Arrays.stream(elements).map(StackUtil::strToStack2).collect(Collectors.toList());
            }
            return Pair.of(toReturn, toReturn.size());
        }

    }

}
