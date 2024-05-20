package cn.solarmoon.idyllic_food_diary.core.common.block.cookware;

import cn.solarmoon.idyllic_food_diary.api.common.block.cookware.AbstractCuttingBoardEntityBlock;
import cn.solarmoon.idyllic_food_diary.api.util.VoxelShapeUtil;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
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
        super(Block.Properties.copy(Blocks.CHEST).noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(0.0D, 0.0D, 1.0D, 16.0D, 1.0D, 15.0D));
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.CUTTING_BOARD.get();
    }

}
