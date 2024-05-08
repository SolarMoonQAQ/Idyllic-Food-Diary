package cn.solarmoon.idyllic_food_diary.core.common.block.product;

import cn.solarmoon.idyllic_food_diary.core.common.registry.IMDamageTypes;
import cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.common.block.IWaterLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;


public class DurianBlock extends FallingBlock implements IWaterLoggedBlock, IHorizontalFacingBlock {

    public DurianBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(1.0f));
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
        if (!level.isClientSide) level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 10F, 0.1F);
    }

    @Override
    public DamageSource getFallDamageSource(Entity entity) {
        return IMDamageTypes.FALLING_DURIAN.get(entity.level());
    }

}
