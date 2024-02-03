package cn.solarmoon.immersive_delight.util;

import cn.solarmoon.immersive_delight.api.util.RecipeUtil;
import cn.solarmoon.immersive_delight.client.event.RollingPinClientEvent;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.common.recipes.RollingPinRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.solarmoon.immersive_delight.common.registry.IMItems.ROLLING_PIN;

/**
 * 用于处理擀面杖的一些识别等方法
 */
public class RollingPinHelper {


    /**
     * 如果目视方块与配方匹配，返回true
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean hitResultRecipeCheck() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return false;
        if (mc.hitResult instanceof BlockHitResult hitResult){
            BlockPos blockPos = hitResult.getBlockPos();
            Block block = mc.level.getBlockState(blockPos).getBlock();
            for (RollingPinRecipe recipe : RecipeUtil.getRecipes(mc.level, IMRecipes.ROLLING_RECIPE.get())) {
                Ingredient input = recipe.getInput();
                if (input.test(block.asItem().getDefaultInstance())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 如果拿着擀面杖，主副手任意一个为空，就返回true
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean holdRollingCheck() {
        boolean isHoldingRollingPin;
        boolean isAnyHandEmpty;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            isHoldingRollingPin = mc.player.getMainHandItem().is(ROLLING_PIN.get().asItem())
                    || mc.player.getOffhandItem().is(ROLLING_PIN.get().asItem());
            isAnyHandEmpty = mc.player.getMainHandItem().isEmpty()
                    || mc.player.getOffhandItem().isEmpty();
        } else {
            return false;
        }
        return isHoldingRollingPin && isAnyHandEmpty;
    }

    /**
     * 按照目视方块（hitResult）动态更新所匹配的配方的方法
     * 原理是把所有匹配的配方物品加入一个列表中
     */
    @OnlyIn(Dist.CLIENT)
    public static void updatePossibleOutputs(Block block) {
        Minecraft mc = Minecraft.getInstance();
        RollingPinClientEvent.possibleOutputs = new ArrayList<>();
        RollingPinClientEvent.actualResults = new ArrayList<>();
        List<RollingPinRecipe> matchingRecipes = new ArrayList<>();
        if(mc.level == null) return;
        for (RollingPinRecipe recipe : RecipeUtil.getRecipes(mc.level, IMRecipes.ROLLING_RECIPE.get())) {
            Ingredient input = recipe.getInput();
            if (input.test(block.asItem().getDefaultInstance())) {
                //方块->对应配方的随机后物品栈
                //获取幸运等级 和 随机源
                int fortuneLevel = 0;
                MobEffectInstance luckEffect;
                int luckPotionLevel = 0;
                if (mc.player != null) {
                    fortuneLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, mc.player.getItemInHand(InteractionHand.MAIN_HAND));
                    luckEffect = mc.player.getEffect(MobEffects.LUCK);
                    luckPotionLevel = (luckEffect != null) ? luckEffect.getAmplifier() + 1 : 0;
                }
                int totalLuckLevel = fortuneLevel + luckPotionLevel;
                RandomSource rand = null;
                if (Minecraft.getInstance().level != null) {
                    rand = mc.player.getRandom();
                }
                //获取所有配方
                matchingRecipes.add(recipe);
                //获取选择的配方
                RollingPinRecipe selectedRecipe = matchingRecipes.get(RollingPinClientEvent.currentRecipeIndex % matchingRecipes.size());
                //输出选择的配方的随机物品结果
                if (rand != null) {
                    RollingPinClientEvent.actualResults = selectedRecipe.rollResults(rand, totalLuckLevel);
                }
            }
        }
        //遍历所有配方
        for (RollingPinRecipe targetResult : matchingRecipes) {
            ItemStack put;
            if(targetResult.getOutput() != null) {
                Ingredient output = targetResult.getOutput();
                RollingPinClientEvent.possibleOutputs.addAll(Arrays.asList(output.getItems()));
            } else {
                put = targetResult.getResults().get(0);
                RollingPinClientEvent.possibleOutputs.add(put);
            }
        }
    }
}
