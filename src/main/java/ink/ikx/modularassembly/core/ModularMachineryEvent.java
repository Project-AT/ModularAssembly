package ink.ikx.modularassembly.core;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import ink.ikx.modularassembly.Main;
import ink.ikx.modularassembly.utils.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber
@SuppressWarnings("deprecation")
public class ModularMachineryEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos blockPos = event.getPos();
        TileEntity tileEntity = world.getTileEntity(blockPos);
        IBlockState blockState = world.getBlockState(blockPos);
        IItemStack stack = CraftTweakerMC.getIItemStack(event.getItemStack());

        if (world.isRemote || blockState.getBlock().isAir(blockState, world, blockPos) || tileEntity == null || StackUtil.AUTO_ASSEMBLY_ITEM.isEmpty() || stack.isEmpty())
            return;

        if (CraftTweakerMC.getIItemStack(StackUtil.AUTO_ASSEMBLY_ITEM).matches(stack)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player.world.isRemote) return;

        if (StackUtil.AUTO_ASSEMBLY_ITEM.isEmpty()) {
            player.sendMessage(new TextComponentString(I18n.translateToLocal("message.modularassembly.config.no_converted")));
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Main.MOD_ID)) {
            ConfigManager.sync(Main.MOD_ID, Config.Type.INSTANCE);
        }
    }

}
