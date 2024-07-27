package cn.solarmoon.idyllic_food_diary.element.matter.food.long_press_eat_block;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.useful_data.BlockProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BaiJiBunBlock extends AbstractLongPressEatFoodBlock {

    public BaiJiBunBlock() {
        super(BlockProperty.DOUGH);
    }

    @Override
    public VoxelShape getOriginShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(5.0D, 0.0D, 5.0D, 11.0D, 2.0D, 11.0D);
    }

}
