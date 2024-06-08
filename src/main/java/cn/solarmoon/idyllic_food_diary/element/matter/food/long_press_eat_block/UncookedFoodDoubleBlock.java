package cn.solarmoon.idyllic_food_diary.element.matter.food.long_press_eat_block;

import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 未烹饪的食物双方块，没有任何多余交互，只是能吃
 */
public class UncookedFoodDoubleBlock extends UncookedFoodBlock implements IBedPartBlock {

    public UncookedFoodDoubleBlock(VoxelShape shape, Properties properties) {
        super(shape, properties);
    }

    public UncookedFoodDoubleBlock(PropertyDispatch.QuadFunction<BlockState, BlockGetter, BlockPos, CollisionContext, VoxelShape> shapeConsumerFunction, Properties properties) {
        super(shapeConsumerFunction, properties);
    }

}
