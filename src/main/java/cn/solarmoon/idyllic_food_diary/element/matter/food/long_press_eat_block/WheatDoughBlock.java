package cn.solarmoon.idyllic_food_diary.element.matter.food.long_press_eat_block;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.idyllic_food_diary.util.useful_data.BlockProperty;
import cn.solarmoon.solarmoon_core.api.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.solarmoon.idyllic_food_diary.util.ParticleSpawner.Wave.wave;

/**
 * 面团方块
 */
public class WheatDoughBlock extends AbstractLongPressEatFoodBlock {

    public static final IntegerProperty FALL_COUNT = IntegerProperty.create("fall_count", 0, 10);

    public WheatDoughBlock() {
        super(BlockProperty.DOUGH);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FALL_COUNT, 0));
    }

    /**
     * 踩三下或从大于五格处坠落到上面会压成面饼
     * 减免百分之三十坠落伤害
     */
    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        //减免30%掉落伤害
        float damageMultiplier = 0.7F;
        entity.causeFallDamage(fallDistance, damageMultiplier, entity.damageSources().fall());

        //粒子
        ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
        if(level.isClientSide) {
            wave(particle, pos, level, 2, 5, 0.5, 0);
        }

        //音效
        level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 2.0F, 1.0F);

        int fallCount = state.getValue(getFallCount());

        //压面
        if (fallDistance > 5 || fallCount >= 2) {
            if (entity instanceof LivingEntity) {
                level.destroyBlock(pos, false);
                BlockState toState = IMBlocks.FLATBREAD_DOUGH.get().defaultBlockState();
                BlockUtil.replaceBlockWithAllState(state, toState, level, pos); //保持朝向
                level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 3.0F, 0.5F);
                wave(particle, pos, level, 5, 10, Math.max(fallDistance / 5, 2), 0.8);
            }
        } else level.setBlock(pos, state.setValue(getFallCount(), fallCount + 1), 3);

    }

    public  IntegerProperty getFallCount() {
        return FALL_COUNT;
    }

    /**
     * 碰撞箱
     */
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(4.0D, 0.0D, 4.0D, 12.0D, 4.0D, 12.0D);
    }

    /**
     * 保证多模组联动下掉落的只有自己的面团
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return List.of(IMItems.WHEAT_DOUGH.get().getDefaultInstance());
    }

    /**
     * 保证多模组联动下指向自己的面团
     */
    @Override
    public Item asItem() {
        return IMItems.WHEAT_DOUGH.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(getFallCount());
    }

}
