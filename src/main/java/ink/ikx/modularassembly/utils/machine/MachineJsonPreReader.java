package ink.ikx.modularassembly.utils.machine;

import com.google.common.collect.Lists;
import com.google.gson.*;
import ink.ikx.modularassembly.utils.MiscUtil;
import ink.ikx.modularassembly.utils.StackUtil;
import ink.ikx.modularassembly.utils.machine.MachineJsonFormatInstance.Parts;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public enum MachineJsonPreReader implements JsonDeserializer<MachineJsonFormatInstance> {

    INSTANCE;

    @Override
    public MachineJsonFormatInstance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String machineName = JsonUtils.getString(jsonObject, "registryname");
        List<Parts> machineParts = Lists.newArrayList();

        for (JsonElement parts : JsonUtils.getJsonArray(jsonObject, "parts")) {
            JsonObject partsObject = parts.getAsJsonObject();
            int x = JsonUtils.getInt(partsObject, "x");
            int y = JsonUtils.getInt(partsObject, "y");
            int z = JsonUtils.getInt(partsObject, "z");
            String[] elements = getJsonMaybeArray(partsObject, "elements");
            String[] itemStacks = getJsonMaybeArray(partsObject, "itemStacks");
            String[] blockstates = getJsonMaybeArray(partsObject, "blockstates");
            if ((StringUtils.isNotBlank(itemStacks[0]) || StringUtils.isNotBlank(blockstates[0])) && itemStacks.length != blockstates.length) {
                throw new JsonParseException("itemStacks and elements must have the same length");
            }
            if (!validationItemStack(itemStacks) && !validationIBlockState(blockstates)) {
                throw new JsonParseException("itemStacks and elements must be valid");
            }
            machineParts.add(new Parts(x, y, z, itemStacks, blockstates, elements));
        }
        return MachineJsonFormatInstance.getOrCreate(machineName, machineParts);
    }

    private boolean validationIBlockState(String[] toValidate) {
        return Arrays.stream(toValidate).noneMatch(s -> MiscUtil.strToState(s) != null);
    }

    private boolean validationItemStack(String[] toValidate) {
        return Arrays.stream(toValidate).noneMatch(s -> StackUtil.strToStack(s).isEmpty());
    }

    private String[] getJsonMaybeArray(JsonObject partsObject, String memberName) {
        List<String> toReturn = Lists.newLinkedList();
        if (JsonUtils.isJsonArray(partsObject, memberName)) {
            JsonUtils.getJsonArray(partsObject, memberName).forEach(e -> toReturn.add(e.getAsString()));
        } else {
            toReturn.add(JsonUtils.getString(partsObject, memberName, ""));
        }
        return toReturn.toArray(new String[0]);
    }

}
