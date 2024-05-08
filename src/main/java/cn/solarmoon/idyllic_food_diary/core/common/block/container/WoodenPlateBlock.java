package cn.solarmoon.idyllic_food_diary.core.common.block.container;

import cn.solarmoon.idyllic_food_diary.api.common.block.container.AbstractContainerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WoodenPlateBlock extends AbstractContainerBlock {

    public WoodenPlateBlock() {
        super(SoundType.BAMBOO);
    }

    public final static VoxelShape SHAPE = Block.box(1, 0, 1, 15, 1, 15);
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

}
