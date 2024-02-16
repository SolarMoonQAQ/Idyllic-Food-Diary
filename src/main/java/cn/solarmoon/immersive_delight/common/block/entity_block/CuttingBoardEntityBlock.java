package cn.solarmoon.immersive_delight.common.block.entity_block;

import cn.solarmoon.immersive_delight.common.block.base.AbstractCuttingBoardEntityBlock;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CuttingBoardEntityBlock extends AbstractCuttingBoardEntityBlock {

    public CuttingBoardEntityBlock() {
        super(Block.Properties.copy(Blocks.CHEST));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.CUTTING_BOARD.get();
    }

}
