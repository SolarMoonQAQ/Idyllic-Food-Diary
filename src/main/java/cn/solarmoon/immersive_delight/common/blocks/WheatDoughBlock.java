package cn.solarmoon.immersive_delight.common.blocks;

import cn.solarmoon.immersive_delight.common.IMBlocks;
import cn.solarmoon.immersive_delight.api.common.block.BaseLongPressEatBlock;
import cn.solarmoon.immersive_delight.api.network.serializer.ClientPackSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static cn.solarmoon.immersive_delight.client.particles.vanilla.Wave.wave;

/**
 * 面团方块
 */
public class WheatDoughBlock extends BaseLongPressEatBlock {

    public WheatDoughBlock() {
        super(Block.Properties
                .copy(Blocks.CAKE)
                .destroyTime(1f));
    }

    /**
     * 踩三下或从大于五格处坠落到上面会压成面饼
     * 减免百分之三十坠落伤害
     */
    Map<BlockPos, Integer> fallCounts = new HashMap<>();
    @Override
    public void fallOn(@NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
        //减免30%掉落伤害
        float damageMultiplier = 0.7F;
        entity.causeFallDamage(fallDistance, damageMultiplier, entity.damageSources().fall());

        //粒子
        ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
        if(level.isClientSide) {
            wave(particle, pos, 2, 5, 0.5, 0);
        }

        //音效
        level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 2.0F, 1.0F);

        int fallCount = fallCounts.getOrDefault(pos, 0);
        if(!level.isClientSide) {
            fallCount++;
            //压面
            if (fallDistance > 5 || fallCount >= 3) {
                if (entity instanceof LivingEntity) {
                    level.destroyBlock(pos, false);
                    ClientPackSerializer.sendPacket(pos, fallDistance, "wave");
                    level.setBlock(pos, IMBlocks.FLATBREAD_DOUGH.get().defaultBlockState(), 3);
                    level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 3.0F, 0.5F);
                    fallCount = 0;
                }
            }
        }

        fallCounts.put(pos, fallCount);
    }

    /**
     * 破坏时刷一下蹦跳次数
     */
    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        fallCounts.put(pos, 0);
    }

    /**
     * 碰撞箱
     */
    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 4.0D, 12.0D);
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

}
