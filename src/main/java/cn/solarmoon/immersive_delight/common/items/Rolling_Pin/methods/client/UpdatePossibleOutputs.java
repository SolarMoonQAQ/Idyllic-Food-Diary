package cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client;

import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.events.client.ChooseOutput;
import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.GetRecipes;
import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.recipe.RollingPinRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.solarmoon.immersive_delight.util.Constants.mc;


public class UpdatePossibleOutputs {
    /**
     * 按照目视方块（hitResult）动态更新所匹配的配方的方法
     * 原理是把所有匹配的配方物品加入一个列表中
     */
    public static void updatePossibleOutputs(Block block) {
        ChooseOutput.possibleOutputs = new ArrayList<>();
        ChooseOutput.actualResults = new ArrayList<>();
        List<RollingPinRecipe> matchingRecipes = new ArrayList<>();
        if(mc.level == null) return;
        for (RollingPinRecipe recipe : GetRecipes.getRollingRecipes(mc.level)) {
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
                RollingPinRecipe selectedRecipe = matchingRecipes.get(ChooseOutput.currentRecipeIndex % matchingRecipes.size());
                //输出选择的配方的随机物品结果
                if (rand != null) {
                    ChooseOutput.actualResults = selectedRecipe.rollResults(rand, totalLuckLevel);
                }
            }
        }
        //遍历所有配方
        for (RollingPinRecipe targetResult : matchingRecipes) {
            ItemStack put;
            if(targetResult.getOutput() != null) {
                Ingredient output = targetResult.getOutput();
                ChooseOutput.possibleOutputs.addAll(Arrays.asList(output.getItems()));
            } else {
                put = targetResult.getResults().get(0);
                ChooseOutput.possibleOutputs.add(put);
            }
        }
    }
}
