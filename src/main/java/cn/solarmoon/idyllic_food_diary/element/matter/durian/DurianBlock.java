package cn.solarmoon.idyllic_food_diary.element.matter.durian;

import cn.solarmoon.idyllic_food_diary.registry.common.IMDamageTypes;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IWaterLoggedBlock;
import cn.solarmoon.solarmoon_core.api.phys.OrientedBox;
import cn.solarmoon.solarmoon_core.api.phys.VecUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;


public class DurianBlock extends FallingBlock implements IWaterLoggedBlock, IHorizontalFacingBlock {

    public DurianBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.STONE)
                .strength(1.0f)
        );
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return Block.box(3.0D, 0.0D, 3.0D, 13.0D, 12.5D, 13.0D);
    }

    @Override
    protected void falling(FallingBlockEntity blockEntity) {
        blockEntity.setHurtsEntities(2.0F, 40);
    }

    /**
     * 播放坠落音效
     */
    @Override
    public void onLand(Level level, BlockPos pos, BlockState state1, BlockState state2, FallingBlockEntity blockEntity) {
        level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 10F, 0.1F);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.hurt(IMDamageTypes.DURIAN_THORNS.get(level), 1);
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public DamageSource getFallDamageSource(Entity entity) {
        return IMDamageTypes.FALLING_DURIAN.get(entity.level());
    }

}
