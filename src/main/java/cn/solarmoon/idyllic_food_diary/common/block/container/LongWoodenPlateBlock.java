package cn.solarmoon.idyllic_food_diary.common.block.container;

import cn.solarmoon.idyllic_food_diary.common.block.base.container.AbstractLongContainerBlock;
import cn.solarmoon.idyllic_food_diary.util.VoxelShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LongWoodenPlateBlock extends AbstractLongContainerBlock {

    public LongWoodenPlateBlock() {
        super(SoundType.BAMBOO);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return state.getValue(PART) == BedPart.HEAD ?
                VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(1, 0, 1, 15, 1, 16))
                : VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(1, 0, 0, 15, 1, 15));
    }

}
