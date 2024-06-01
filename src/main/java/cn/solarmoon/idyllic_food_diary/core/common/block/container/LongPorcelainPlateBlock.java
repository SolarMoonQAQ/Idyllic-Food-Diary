package cn.solarmoon.idyllic_food_diary.core.common.block.container;

import cn.solarmoon.idyllic_food_diary.api.common.block.container.AbstractLongContainerBlock;
import cn.solarmoon.idyllic_food_diary.api.util.VoxelShapeUtil;
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
        VoxelShape combine1 = Shapes.or(Block.box(1, 1, 1, 15, 2, 16),
                Block.box(3, 0, 3, 13, 1, 16));
        VoxelShape combine2 = Shapes.or(Block.box(1, 1, 0, 15, 2, 15),
                Block.box(3, 0, 0, 13, 1, 13));
        return state.getValue(PART) == BedPart.HEAD ? VoxelShapeUtil.rotateShape(state.getValue(FACING), combine1) :
                VoxelShapeUtil.rotateShape(state.getValue(FACING), combine2);
    }

}
