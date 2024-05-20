package cn.solarmoon.idyllic_food_diary.core.common.block.food_block.long_press_eat_block;

import cn.solarmoon.idyllic_food_diary.api.common.block.food_block.AbstractLongPressEatFoodBlock;
import cn.solarmoon.idyllic_food_diary.api.util.useful_data.BlockProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PieCrustBlock extends AbstractLongPressEatFoodBlock {

    public PieCrustBlock() {
        super(BlockProperty.DOUGH);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.joinUnoptimized(
                Block.box(2, 0, 2, 14, 4, 14),
                Block.box(3, 1, 3, 13, 4, 13),
                BooleanOp.ONLY_FIRST
                );
    }

}
