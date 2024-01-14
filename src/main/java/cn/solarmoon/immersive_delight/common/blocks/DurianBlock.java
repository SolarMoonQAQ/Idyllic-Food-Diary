package cn.solarmoon.immersive_delight.common.blocks;

import cn.solarmoon.immersive_delight.common.IMDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

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
