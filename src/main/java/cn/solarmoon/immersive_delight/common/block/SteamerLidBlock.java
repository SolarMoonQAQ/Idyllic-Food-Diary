package cn.solarmoon.immersive_delight.common.block;

import cn.solarmoon.solarmoon_core.common.block.BaseWaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteamerLidBlock extends BaseWaterBlock {

    public SteamerLidBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.BAMBOO).strength(0.7F));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

}
