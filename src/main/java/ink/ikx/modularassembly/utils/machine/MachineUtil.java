package ink.ikx.modularassembly.utils.machine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import hellfirepvp.modularmachinery.common.CommonProxy;
import hellfirepvp.modularmachinery.common.machine.MachineLoader;
import youyihj.modularcontroller.ModularController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MachineUtil {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(MachineJsonFormatInstance.class, MachineJsonPreReader.INSTANCE).create();

    public static void initAllMachine() {
        for (File file : MachineLoader.discoverDirectory(CommonProxy.dataHolder.getMachineryDirectory()).get(MachineLoader.FileType.MACHINE)) {
            try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                GSON.fromJson(isr, MachineJsonFormatInstance.class);
            } catch (JsonParseException e) {
                ModularController.logger.error(file + " is not a valid machine json", e);
            } catch (IOException e) {
                ModularController.logger.error("failed to load custom controllers", e);
            }
        }
    }

}
