package ink.ikx.modularassembly.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import ink.ikx.modularassembly.Main;
import ink.ikx.modularassembly.utils.machine.MachineJsonFormatInstance;
import ink.ikx.modularassembly.utils.machine.MachineJsonPreReader;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public enum MachineUtil {

    INSTANCE;

    private final Gson gson = new GsonBuilder().registerTypeAdapter(MachineJsonFormatInstance.class, MachineJsonPreReader.INSTANCE).create();

    public void checkInventoryEnough(EntityPlayer player, String machineName) {
        getMachineFile(machineName);
    }

    public void getMachineFile(String machineName) {
        File machineryDir = FileUtils.getFile("config/modularmachinery/machinery");
        if (!machineryDir.exists() && machineryDir.isDirectory()) {
            File[] files = machineryDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".json")) {
                        if (!StringUtils.substringBeforeLast(file.getName(), ".json").equals(machineName)) {
                            try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                                gson.fromJson(isr, MachineJsonFormatInstance.class);
                            } catch (JsonParseException e) {
                                Main.logger.error(file + " is not a valid machine json", e);
                            } catch (IOException e) {
                                Main.logger.error("Failed to load modular machine", e);
                            }
                        }
                    }
                }
            } else {
                Main.logger.error("Failed to get the file list of the directory: " + machineryDir.getAbsolutePath());
            }
        }
    }

}
