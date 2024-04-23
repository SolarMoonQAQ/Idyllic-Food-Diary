package cn.solarmoon.idyllic_food_diary.common.block.entity_block;

import cn.solarmoon.idyllic_food_diary.common.block.base.entity_block.AbstractSteamerEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteamerEntityBlock extends AbstractSteamerEntityBlock {

    public SteamerEntityBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.BAMBOO).strength(2).noOcclusion());
    }

    protected static final VoxelShape[] SHAPE_BY_STACK = new VoxelShape[]{
            Block.box(1D, 0.0D, 1D, 15D, 8D, 15D),
            Block.box(1D, 0.0D, 1D, 15.0D, 16D, 15.0D),
    };
    protected static final VoxelShape[] SHAPE_ADD = new VoxelShape[]{
            Block.box(0.5D, 0D, 0.5D, 15.5D, 4D, 15.5D),
            Block.box(0.5D, 8D, 0.5D, 15.5D, 12D, 15.5D)
    };
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape = Shapes.block();
        if (state.getValue(STACK) == 1) {
            shape = Shapes.or(SHAPE_BY_STACK[0], SHAPE_ADD[0]);
        } else if (state.getValue(STACK) == 2) {
            shape = Shapes.or(SHAPE_BY_STACK[1], SHAPE_ADD);
        }
        return shape;
    }

}
