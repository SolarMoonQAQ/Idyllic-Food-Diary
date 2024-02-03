package cn.solarmoon.immersive_delight.common.event;

import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.common.recipes.CleaverRecipe;
import cn.solarmoon.immersive_delight.data.tags.IMItemTags;
import cn.solarmoon.immersive_delight.api.util.LevelSummonUtil;
import cn.solarmoon.immersive_delight.api.util.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class CleaverEvent {

    @SubscribeEvent
    public void RightClickRecipe(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Player player = event.getEntity();
        ItemStack useItem = event.getItemStack();

        //斧子或刀或剑都可用
        if (useItem.is(ItemTags.AXES) || useItem.is(ItemTags.SWORDS)
                || useItem.is(IMItemTags.FORGE_KNIVES) || useItem.is(IMItemTags.FORGE_AXES) || useItem.is(IMItemTags.FORGE_SWORDS)) {
            List<CleaverRecipe> recipes = RecipeUtil.getRecipes(level, IMRecipes.CLEAVER_RECIPE.get());
            for (var recipe : recipes) {
                if (recipe.getInput().test(state.getBlock().asItem().getDefaultInstance())) {
                    //获取幸运源
                    int fortuneLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, useItem);
                    MobEffectInstance luckEffect = player.getEffect(MobEffects.LUCK);
                    int luckPotionLevel = (luckEffect != null) ? luckEffect.getAmplifier() + 1 : 0;
                    int totalLuckLevel = fortuneLevel + luckPotionLevel;
                    RandomSource rand = player.getRandom();

                    //输出（生成掉落物）
                    List<ItemStack> results = recipe.rollResults(rand, totalLuckLevel);
                    for (var stack : results) {
                        LevelSummonUtil.summonDrop(stack, level, pos);
                    }
                    level.destroyBlock(pos, false);
                    useItem.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(player.getUsedItemHand()));
                    if (!level.isClientSide) {
                        level.playSound(null, pos, SoundEvents.VILLAGER_WORK_TOOLSMITH, SoundSource.BLOCKS, 1F, 1.5F);
                        level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 10F, 1F);
                    }
                    player.swing(event.getHand());
                }
            }
        }
    }

}
