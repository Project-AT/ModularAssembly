package ink.ikx.modularassembly.mixins;

import hellfirepvp.modularmachinery.common.item.ItemBlueprint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = ItemBlueprint.class, remap = false)
public class MixinItemBlueprint {

    @Inject(method = "onItemUse", at = @At("HEAD"), cancellable = true)
    public void injectOnItemUse(EntityPlayer player, World worldIn, BlockPos pos,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ,
            CallbackInfoReturnable<EnumActionResult> cir) {
        if (player.isSneaking()) cir.setReturnValue(EnumActionResult.PASS);
    }

    @Inject(method = "onItemRightClick", at = @At("HEAD"), cancellable = true)
    public void injectOnItemRightClick(World worldIn, EntityPlayer player, EnumHand hand,
            CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
        if (player.isSneaking())
            cir.setReturnValue(new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand)));
    }

}