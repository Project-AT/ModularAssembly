package ink.ikx.modularassembly.utils.machine;

import com.google.common.collect.Lists;
import com.google.gson.*;
import ink.ikx.modularassembly.utils.machine.MachineJsonFormatInstance.Parts;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;
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
            machineParts.add(new Parts(x, y, z,
                    getJsonMaybeArray(partsObject, "itemStacks"),
                    getJsonMaybeArray(partsObject, "elements"))
            );
        }
        return MachineJsonFormatInstance.getOrCreate(machineName, machineParts);
    }

    private String[] getJsonMaybeArray(JsonObject partsObject, String memberName) {
        List<String> toReturn = Lists.newArrayList();
        if (JsonUtils.isJsonArray(partsObject, memberName)) {
            JsonUtils.getJsonArray(partsObject, memberName).forEach(e -> toReturn.add(e.getAsString()));
        } else {
            toReturn.add(JsonUtils.getString(partsObject, memberName, ""));
        }
        return toReturn.toArray(new String[0]);
    }

}
