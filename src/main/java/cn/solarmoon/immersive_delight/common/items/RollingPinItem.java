package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.client.events.RollingPinClientEvent;
import cn.solarmoon.immersive_delight.common.recipes.RollingPinRecipe;
import cn.solarmoon.immersive_delight.network.serializer.ServerPackSerializer;
import cn.solarmoon.immersive_delight.util.AnimController;
import cn.solarmoon.immersive_delight.util.RecipeHelper;
import cn.solarmoon.immersive_delight.util.RollingPinHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static cn.solarmoon.immersive_delight.client.particles.vanilla.Rolling.rolling;


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
                RollingPinHelper.updatePossibleOutputs(equalBlock);
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
            if(!RollingPinHelper.hitResultRecipeCheck() || !(mc.hitResult instanceof BlockHitResult) || !((BlockHitResult) mc.hitResult).getBlockPos().equals(equalBlockPos)) {
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
                RollingPinHelper.updatePossibleOutputs(equalBlock);
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

}

