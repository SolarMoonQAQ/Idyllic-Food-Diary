package cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client;

import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.GetRecipes;
import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.recipe.RollingPinRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static cn.solarmoon.immersive_delight.util.Constants.mc;


@OnlyIn(Dist.CLIENT)
public class HitResultRecipeCheck {
    /**
     * 如果目视方块与配方匹配，返回true
     */
    public static boolean hitResultRecipeCheck() {
        if (mc.level == null) return false;
        if (mc.hitResult instanceof BlockHitResult hitResult){
            BlockPos blockPos = hitResult.getBlockPos();
            Block block = mc.level.getBlockState(blockPos).getBlock();
            for (RollingPinRecipe recipe : GetRecipes.getRollingRecipes(mc.level)) {
                Ingredient input = recipe.getInput();
                if (input.test(block.asItem().getDefaultInstance())) {
                    return true;
                }
            }
        }
        return false;
    }
}
