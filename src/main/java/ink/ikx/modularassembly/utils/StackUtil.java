package ink.ikx.modularassembly.utils;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.brackets.BracketHandlerItem;
import crafttweaker.mc1120.data.StringIDataParser;
import ink.ikx.modularassembly.core.config.Configuration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class StackUtil {

    public static final ItemStack AUTO_ASSEMBLY_ITEM = StackUtil.strToStack(Configuration.AutoAssemblyItem);

    public static ItemStack strToStack(String str) {
        String itemStr = StringUtils.substringBetween(str, "<", ">");
        if (itemStr == null) return ItemStack.EMPTY;
        String[] split = itemStr.split(":");
        if (split.length == 0) return ItemStack.EMPTY;

        Pair<String, Integer> stack = Pair.of(split[0] + ":" + split[1], split.length > 2 ? Integer.parseInt(split[2]) : 0);

        String iData = StringUtils.substringBetween(Configuration.AutoAssemblyItem, ".withTag(", ")");
        IItemStack toReturn = BracketHandlerItem.getItem(stack.getLeft(), stack.getValue());

        if (iData != null) {
            toReturn.mutable().withTag(StringIDataParser.parse(iData), true);
        }
        return CraftTweakerMC.getItemStack(toReturn);
    }

    @SuppressWarnings("deprecation")
    public static ItemStack strToStack2(String str) {
        String[] split = str.split("@");
        if (split.length != 2) return ItemStack.EMPTY;

        ResourceLocation res = new ResourceLocation(split[0].split(":")[0], split[0].split(":")[1]);
        Block block = ForgeRegistries.BLOCKS.getValue(res);

        assert block != null;
        IBlockState state = block.getStateFromMeta(Integer.parseInt(split[1]));

        if (block instanceof BlockFluidBase) {
            return FluidUtil.getFilledBucket(new FluidStack(((BlockFluidBase) block).getFluid(), 1000));
        } else if (block instanceof BlockLiquid) {
            Material m = state.getMaterial();
            if (m == Material.LAVA) {
                return new ItemStack(Items.LAVA_BUCKET);
            } else if (m == Material.WATER) {
                return new ItemStack(Items.WATER_BUCKET);
            }
        }
        return new ItemStack(Item.getItemFromBlock(block), 1, block.damageDropped(state));

    }

    public static boolean isStackInInventory(ItemStack stack, List<ItemStack> inventory) {
        IItemStack input = CraftTweakerMC.getIItemStack(stack);
        List<IItemStack> inventory_ = CraftTweakerMC.getIItemStackList(inventory);

        for (IItemStack iItemStack : inventory_) {
            if (input.matches(iItemStack)) return true;
        }

        return false;
    }

}
