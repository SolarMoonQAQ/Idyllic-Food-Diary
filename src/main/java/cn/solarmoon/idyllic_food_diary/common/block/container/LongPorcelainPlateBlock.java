package cn.solarmoon.idyllic_food_diary.common.block.container;

import cn.solarmoon.idyllic_food_diary.common.block.base.container.AbstractLongContainerBlock;
import cn.solarmoon.idyllic_food_diary.util.VoxelShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LongPorcelainPlateBlock extends AbstractLongContainerBlock {

    public LongPorcelainPlateBlock() {
        super(SoundType.GLASS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape2O = state.getValue(PART) == BedPart.HEAD ?
                VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(1, 1, 1, 15, 2, 16))
                : VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(1, 1, 0, 15, 2, 15));
        VoxelShape shapeToSubtract = state.getValue(PART) == BedPart.HEAD ?
                VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(4, 1, 4, 12, 2, 16))
                : VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(4, 1, 0, 12, 2, 12));
        VoxelShape shape1 = state.getValue(PART) == BedPart.HEAD ?
                VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(4, 0, 4, 12, 1, 16))
                : VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(4, 0, 0, 12, 1, 12));
        VoxelShape shape2 = Shapes.joinUnoptimized(shape2O, shapeToSubtract, BooleanOp.ONLY_FIRST);
        return Shapes.or(shape1, shape2);
    }

}
