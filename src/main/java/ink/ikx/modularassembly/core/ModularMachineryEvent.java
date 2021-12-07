package ink.ikx.modularassembly.core;

import hellfirepvp.modularmachinery.common.lib.ItemsMM;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.tiles.TileMachineController;
import ink.ikx.modularassembly.utils.CollUtils;
import ink.ikx.modularassembly.utils.assembly.MachineAssembly;
import ink.ikx.modularassembly.utils.assembly.MachineAssemblyManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Set;

public class ModularMachineryEvent {

    public static ModularMachineryEvent INSTANCE = new ModularMachineryEvent();

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos blockPos = event.getPos();
        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (world.isRemote) return;

        if (tileEntity instanceof TileMachineController && !player.isSneaking()) {
            TileMachineController controller = (TileMachineController) tileEntity;
            if (stack.getItem().equals(ItemsMM.blueprint)) {
                if (getBlueprint(controller).isEmpty()) {
                    ItemStack copy = stack.copy();
                    copy.setCount(1);
                    if (isPlayerNotCreative(player)) stack.setCount(stack.getCount() - 1);
                    controller.getInventory().setStackInSlot(TileMachineController.BLUEPRINT_SLOT, copy);
                }
                event.setCanceled(true);
            } else if (stack.getItem().equals(Items.STICK)) {
                DynamicMachine blueprintMachine = controller.getBlueprintMachine();
                if (blueprintMachine == null) {
                    player.sendMessage(new TextComponentString("No blueprint machine found!"));
                    return;
                }
                MachineAssembly Machine = new MachineAssembly(blockPos, player, blueprintMachine.getPattern().getPattern());
                if (MachineAssemblyManager.checkMachineExist(Machine)) {
                    player.sendMessage(new TextComponentString("Machine assembly added!"));
                    return;
                }
                if (!Machine.isAllItemsContains()) {
                    player.sendMessage(new TextComponentString("Not all items are in the inventory!"));
                } else {
                    MachineAssemblyManager.addMachineAssembly(Machine);
                    player.sendMessage(new TextComponentString("Machine assembly add!"));
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.world;
        if (event.phase == TickEvent.Phase.START || event.side.isClient() ||
                player instanceof FakePlayer || world.getWorldTime() % 5 != 0) return;

        Set<MachineAssembly> machineAssemblyListFromPlayer = MachineAssemblyManager.getMachineAssemblyListFromPlayer(player);

        if (CollUtils.isNotEmpty(machineAssemblyListFromPlayer)) {
            machineAssemblyListFromPlayer.stream().filter(MachineAssembly::isNotAir).forEach(MachineAssembly::build);
        }
    }

    private ItemStack getBlueprint(TileMachineController controller) {
        return controller.getInventory().getStackInSlot(TileMachineController.BLUEPRINT_SLOT);
    }

    private boolean isPlayerNotCreative(EntityPlayer player) {
        return !player.isCreative();
    }

}
