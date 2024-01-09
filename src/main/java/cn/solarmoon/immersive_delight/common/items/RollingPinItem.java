package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.client.events.RollingPinClientEvent;
import cn.solarmoon.immersive_delight.common.recipes.RollingPinRecipe;
import cn.solarmoon.immersive_delight.network.serializer.ServerPackSerializer;
import cn.solarmoon.immersive_delight.util.AnimController;
import cn.solarmoon.immersive_delight.util.RecipeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.solarmoon.immersive_delight.client.particles.vanilla.Rolling.rolling;
import static cn.solarmoon.immersive_delight.common.IMItems.ROLLING_PIN;


public class RollingPinItem extends SwordItem {

    private int time;
    private Block equalBlock;
    private BlockPos equalBlockPos;

    /**
     * 属性与木剑类似
     */
    public RollingPinItem() {
        super(Tiers.WOOD,3, -2.4f, new Item.Properties().durability(512));
    }

    /**
     * 动态的决定使用时间
     */
    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return time;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.NONE;
    }

    /**
     * 擀面杖只能用于擀面配方中已有的方块
     * 这一步用于决定擀面杖是否开始使用并激活动作
     * 并从配方中读取相应的time值
     */
    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        equalBlock = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
        equalBlockPos = context.getClickedPos();
        //限制擀面空间
        BlockPos exceptPos;
        exceptPos = new BlockPos(Objects.requireNonNull(context.getPlayer()).getOnPos().getX() + context.getPlayer().getDirection().getStepX(), context.getPlayer().getOnPos().getY() + 1, context.getPlayer().getOnPos().getZ() + context.getPlayer().getDirection().getStepZ());
        if (equalBlockPos.equals(exceptPos) || equalBlockPos.equals(exceptPos.above())) {
            //先更新配方，再检查当前使用的方块是否是一个配方的输入物
            if(context.getLevel().isClientSide) {
                updatePossibleOutputs(equalBlock);
            }
            List<RollingPinRecipe> recipes = RecipeHelper.GetRecipes.rollingRecipes(context.getLevel());
            for (RollingPinRecipe recipe : recipes) {
                Ingredient input = recipe.getInput();
                if (input.test(equalBlock.asItem().getDefaultInstance())) {
                    time = recipe.getTime();
                    context.getPlayer().startUsingItem(context.getHand());
                    if(context.getLevel().isClientSide) {
                        new AnimController().playAnim(20, "waving");
                    }
                    break;
                } else {
                    time = 0;
                }
            }
        }
        return InteractionResult.FAIL;
    }

    /**
     * 这一步用于在使用期间，如果发生视角转移（也就是目视方块配方不匹配擀面配方了）等情况，就停止使用
     * 同时在使用期间，每3tick就播放一次擀面声音（擀面+被擀方块的混合声）
     */
    private int tickCounter = 0;
    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int i) {
        Minecraft mc = Minecraft.getInstance();
        if(level.isClientSide) {
            rolling(equalBlockPos);
            if(!hitResultRecipeCheck() || !(mc.hitResult instanceof BlockHitResult) || !((BlockHitResult) mc.hitResult).getBlockPos().equals(equalBlockPos)) {
                new AnimController().stopAnim(10);
                ServerPackSerializer.sendPacket(equalBlockPos, equalBlock, stack, "stopUse");
            }
        } else tickCounter++;
        if(tickCounter >= 3) {
            level.playSound(null, equalBlockPos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.playSound(null, equalBlockPos, equalBlock.getSoundType(equalBlock.defaultBlockState(), level, equalBlockPos, null).getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            tickCounter = 0;
        }
    }

    /**
     * 使用成功结束后，输出对应配方的所有输出，并减少耐久度
     */
    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving) {
        if(level.isClientSide) new AnimController().stopAnim(10);
        if (getUseDuration(entityLiving.getUseItem()) - entityLiving.getUseItemRemainingTicks() >= time && time > 0) {
            ItemStack output;
            Block selectedBlock = Blocks.AIR;

            //输出方块
            if(level.isClientSide) {
                //保底更新
                updatePossibleOutputs(equalBlock);
                if (RollingPinClientEvent.possibleOutputs != null && !RollingPinClientEvent.possibleOutputs.isEmpty()) {
                    output = RollingPinClientEvent.possibleOutputs.get(RollingPinClientEvent.currentRecipeIndex);
                    selectedBlock = Block.byItem(output.getItem());
                    ServerPackSerializer.sendPacket(equalBlockPos, selectedBlock, stack, "rollingOutput");
                }
            }

            //输出RESULTS的随机的输出(掉落物高度根据碰撞箱决定)
            if(level.isClientSide) {
                if (!RollingPinClientEvent.actualResults.isEmpty()) {
                    for (ItemStack item : RollingPinClientEvent.actualResults) {
                        ServerPackSerializer.sendPacket(equalBlockPos, selectedBlock, item, "rollingResults");
                    }
                }
            }

            //减少耐久度
            stack.hurtAndBreak(1, entityLiving, (entity) -> entity.broadcastBreakEvent(entityLiving.getUsedItemHand()));

            entityLiving.stopUsingItem();

        }
        return stack;
    }

    /**
     * 主动松开右键也能结束使用
     */
    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity, int p_41415_) {
        if(level.isClientSide) new AnimController().stopAnim(10);
        livingEntity.stopUsingItem();
    }

    /**
     * 设置擀面杖的修复物为任意原木类
     */
    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        Ingredient logs = Ingredient.of(ItemTags.LOGS);
        return logs.test(repair);
    }

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
            for (RollingPinRecipe recipe : RecipeHelper.GetRecipes.rollingRecipes(mc.level)) {
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
        for (RollingPinRecipe recipe : RecipeHelper.GetRecipes.rollingRecipes(mc.level)) {
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

