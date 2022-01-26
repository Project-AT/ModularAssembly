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
            int x = JsonUtils.getInt(parts, "x");
            int y = JsonUtils.getInt(parts, "y");
            int z = JsonUtils.getInt(parts, "z");
            List<String> elements = Lists.newArrayList();
            for (JsonElement element : JsonUtils.getJsonArray(parts, "elements")) {
                elements.add(element.getAsString());
            }
            machineParts.add(new Parts(x, y, z, elements.toArray(new String[0])));
        }
        return MachineJsonFormatInstance.getOrCreate(machineName, machineParts);
    }

}
