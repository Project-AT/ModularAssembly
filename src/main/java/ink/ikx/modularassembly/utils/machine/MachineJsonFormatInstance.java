package ink.ikx.modularassembly.utils.machine;

import com.google.common.collect.Maps;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import ink.ikx.modularassembly.utils.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
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
        public final String[] elements;
        public final String[] itemStacks;

        public Parts(int x, int y, int z, String[] itemStacks, String[] elements) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.itemStacks = itemStacks;
            this.elements = elements;
        }

        public BlockPos getBlockPos() {
            return new BlockPos(x, y, z);
        }

        public BlockPos getBlockPos(BlockPos pos) {
            return pos.add(getBlockPos());
        }

        public boolean matches(IBlockState state) {
            return getStateList().stream().anyMatch(l -> l.stream().anyMatch(state::equals));
        }

        public List<List<IBlockState>> getStateList() {
            return Arrays.stream(elements).map(BlockArray.BlockInformation::getDescriptor).map(d -> d.applicable).collect(Collectors.toList());
        }

        public List<List<ItemStack>> getStackList() {
            List<List<ItemStack>> toReturn = Arrays.stream(itemStacks).filter(i -> !StringUtils.isBlank(i) && !StackUtil.strToStack(i).isEmpty())
                    .map(StackUtil::strToStack).map(Collections::singletonList).collect(Collectors.toList());
            return toReturn.isEmpty() ? Arrays.stream(elements).map(StackUtil::strToStack2).collect(Collectors.toList()) : toReturn;
        }

    }

}
