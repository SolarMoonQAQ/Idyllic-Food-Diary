package cn.solarmoon.immersive_delight.common.blocks;

import cn.solarmoon.immersive_delight.common.IMDamageTypes;
import cn.solarmoon.immersive_delight.common.IMItems;
import cn.solarmoon.immersive_delight.util.LevelSummonHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * 掉落方块
 * 砸人很痛的哦！
 */
public class DurianBlock extends FallingBlock implements Fallable {

    public DurianBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(1.0f));
    }

    @Override
    protected void falling(FallingBlockEntity blockEntity) {
        blockEntity. setHurtsEntities(2.0F, 40);
    }

    /**
     * 用斧子右键可以剥下榴莲肉和榴莲壳
     */
    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(ItemTags.AXES)) {
            level.destroyBlock(pos, false);
            LevelSummonHelper.summonDrop(IMItems.DURIAN_FLESH.get(), level, pos, 2, 4);
            LevelSummonHelper.summonDrop(IMItems.DURIAN_SHELL.get(), level, pos, 2);
            //减少耐久
            heldItem.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(player.getUsedItemHand()));
            if (!level.isClientSide) level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1F, 1F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    /**
     * 播放坠落音效
     */
    @Override
    public void onLand(Level level, BlockPos pos, BlockState state1, BlockState state2, FallingBlockEntity blockEntity) {
        if (!level.isClientSide) level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 10F, 0.1F);
    }

    @Override
    public DamageSource getFallDamageSource(Entity entity) {
        return IMDamageTypes.getSimpleDamageSource(entity.level(), IMDamageTypes.falling_durian);
    }

}
