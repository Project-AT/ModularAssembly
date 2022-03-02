package ink.ikx.modularassembly.core.event;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import hellfirepvp.modularmachinery.common.block.BlockController;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.tiles.TileMachineController;
import ink.ikx.modularassembly.Main;
import ink.ikx.modularassembly.utils.MiscUtil;
import ink.ikx.modularassembly.utils.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import youyihj.modularcontroller.block.BlockMMController;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class ModularMachineryEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos blockPos = event.getPos();
        EntityPlayer entityPlayer = event.getEntityPlayer();
        IBlockState blockState = world.getBlockState(blockPos);
        IItemStack stack = CraftTweakerMC.getIItemStack(event.getItemStack());
        TileMachineController controller = world.getTileEntity(blockPos) instanceof TileMachineController ?
                (TileMachineController) world.getTileEntity(blockPos) : null;
        BlockController block = blockState.getBlock() instanceof BlockController ? (BlockController) blockState.getBlock() : null;

        if (world.isRemote || blockState.getBlock().isAir(blockState, world, blockPos) || block == null
                || controller == null || StackUtil.AUTO_ASSEMBLY_ITEM.isEmpty() || stack.isEmpty()) {
            return;
        }

        if (CraftTweakerMC.getIItemStack(StackUtil.AUTO_ASSEMBLY_ITEM).matches(stack)) {
            if (MiscUtil.isMoCLoaded() && blockState.getBlock() instanceof BlockMMController) {
                List<String> machineNameList = ((BlockMMController) block).getAssociatedMachines().stream()
                        .map(DynamicMachine::getRegistryName)
                        .map(ResourceLocation::getPath)
                        .collect(Collectors.toList());
                // TODO: Process these machines
            } else {
                DynamicMachine machine = controller.getBlueprintMachine();
                if (machine != null) {
                    // TODO: Process this machine
                } else {
                    MiscUtil.sendTranslateToLocalToPlayer(entityPlayer, "message.modularassembly.machine.required_blueprint");
                }
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player.world.isRemote) return;

        if (StackUtil.AUTO_ASSEMBLY_ITEM.isEmpty()) {
            MiscUtil.sendTranslateToLocalToPlayer(player, "message.modularassembly.config.no_converted");
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Main.MOD_ID)) {
            ConfigManager.sync(Main.MOD_ID, Config.Type.INSTANCE);
        }
    }

}
