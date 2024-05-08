package cn.solarmoon.idyllic_food_diary.core.common.block.entity_block;

import cn.solarmoon.idyllic_food_diary.api.common.block.entity_block.AbstractGrillEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrillEntityBlock extends AbstractGrillEntityBlock {

    public GrillEntityBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.LANTERN).noOcclusion());
    }

    protected static final VoxelShape[] SHAPE = new VoxelShape[]{
            Block.box(0, 0, 0, 0, 0, 0),

            Block.box(0D, 0.0D, 0D, 2D, 12D, 2D),
            Block.box(14D, 0.0D, 0D, 16D, 12D, 2D),
            Block.box(0D, 0.0D, 14D, 2D, 12D, 16D),
            Block.box(14D, 0.0D, 14D, 16D, 12D, 16D),

            Block.box(2D, 9D, 1D, 14D, 10D, 2D),
            Block.box(1D, 9D, 2D, 2D, 10D, 14D),
            Block.box(2D, 9D, 14D, 14D, 10D, 15D),
            Block.box(14D, 9D, 2D, 15D, 10D, 14D),

            Block.box(0D, 12D, 0D, 16D, 15D, 16D),

            Block.box(0D, 15D, 0D, 1D, 16D, 16D),
            Block.box(0D, 15D, 0D, 16D, 16D, 1D),
            Block.box(15D, 15D, 0D, 16D, 16D, 16D),
            Block.box(0D, 15D, 15D, 16D, 16D, 16D)
    };
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.or(SHAPE[0], SHAPE);
    }

}
