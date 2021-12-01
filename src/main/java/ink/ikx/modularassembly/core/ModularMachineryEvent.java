package ink.ikx.modularassembly.core;

import hellfirepvp.modularmachinery.common.lib.ItemsMM;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.tiles.TileMachineController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModularMachineryEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
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
                if (Machine.isAllItemsContains()) {
                    player.sendMessage(new TextComponentString("Machine assembly success!"));
                } else {
                    player.sendMessage(new TextComponentString("Machine assembly failed!"));
                }
                event.setCanceled(true);
            }
        }
    }

    private static ItemStack getBlueprint(TileMachineController controller) {
        return controller.getInventory().getStackInSlot(TileMachineController.BLUEPRINT_SLOT);
    }

    private static boolean isPlayerNotCreative(EntityPlayer player) {
        return !player.isCreative();
    }

}
