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
            List<String> elements = Lists.newArrayList();
            if (partsObject.get("elements").isJsonArray()) {
                JsonUtils.getJsonArray(partsObject, "elements").forEach(e -> elements.add(e.getAsString()));
            } else {
                elements.add(JsonUtils.getString(partsObject, "elements"));
            }
            String itemStack = JsonUtils.getString(parts.getAsJsonObject(), "itemStack", "");
            machineParts.add(new Parts(x, y, z, itemStack, elements.toArray(new String[0])));
        }
        return MachineJsonFormatInstance.getOrCreate(machineName, machineParts);
    }

}
