package ink.ikx.modularassembly.mixins;

import hellfirepvp.modularmachinery.common.block.BlockController;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
@Mixin(value = BlockController.class, remap = false)
public class MixinBlockController {

    @Inject(method = "onBlockActivated", at = @At("HEAD"), cancellable = true)
    public void injectOnBlockActivated(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ,
            CallbackInfoReturnable<Boolean> cir) {
        if (playerIn.isSneaking()) cir.setReturnValue(false);
    }

}
