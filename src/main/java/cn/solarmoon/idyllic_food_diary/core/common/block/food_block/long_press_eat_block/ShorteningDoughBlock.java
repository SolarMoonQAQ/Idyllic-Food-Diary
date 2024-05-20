package cn.solarmoon.idyllic_food_diary.core.common.block.food_block.long_press_eat_block;

import cn.solarmoon.idyllic_food_diary.api.common.block.food_block.AbstractLongPressEatFoodBlock;
import cn.solarmoon.idyllic_food_diary.api.util.useful_data.BlockProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShorteningDoughBlock extends AbstractLongPressEatFoodBlock {

    public ShorteningDoughBlock() {
        super(BlockProperty.DOUGH);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(4.0D, 0.0D, 4.0D, 12.0D, 4.0D, 12.0D);
    }

}
