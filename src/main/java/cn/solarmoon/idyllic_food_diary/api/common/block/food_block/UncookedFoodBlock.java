package cn.solarmoon.idyllic_food_diary.api.common.block.food_block;

import net.minecraft.core.BlockPos;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 未烹饪的食物方块，没有任何多余交互，只是能吃
 */
public class UncookedFoodBlock extends AbstractLongPressEatFoodBlock {

    private final PropertyDispatch.QuadFunction<BlockState, BlockGetter, BlockPos, CollisionContext, VoxelShape> shapeConsumerFunction;

    public UncookedFoodBlock(VoxelShape shape, Properties properties) {
        super(properties);
        this.shapeConsumerFunction = (a, b, c, d) -> shape;
    }

    public UncookedFoodBlock(Block blockLeft, PropertyDispatch.QuadFunction<BlockState, BlockGetter, BlockPos, CollisionContext, VoxelShape> shapeConsumerFunction, Properties properties) {
        super(properties);
        this.shapeConsumerFunction = shapeConsumerFunction;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collision) {
        return shapeConsumerFunction.apply(state, getter, pos, collision);
    }

}
