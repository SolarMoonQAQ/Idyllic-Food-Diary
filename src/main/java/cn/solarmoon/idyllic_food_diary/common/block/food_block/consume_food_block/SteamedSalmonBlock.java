package cn.solarmoon.idyllic_food_diary.common.block.food_block.consume_food_block;

import cn.solarmoon.idyllic_food_diary.common.block.base.AbstractConsumeFoodBlock;
import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SteamedSalmonBlock extends AbstractConsumeFoodBlock implements IBedPartBlock {

    public SteamedSalmonBlock() {
        super(3, 6, 0.5f, BlockBehaviour.Properties.of()
                .strength(1)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY));
    }

    @Override
    public Block getBlockLeft() {
        return IMBlocks.LONG_PORCELAIN_PLATE.get();
    }

    @Nullable
    @Override
    public MobEffectInstance getEffect() {
        return new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public int getMaxRemain() {
        return 5;
    }

}
