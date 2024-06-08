package cn.solarmoon.idyllic_food_diary.element.matter.food.long_press_eat_block;

import cn.solarmoon.idyllic_food_diary.element.matter.container.BowlBlock;
import cn.solarmoon.idyllic_food_diary.util.useful_data.BlockProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BowlFoodShapedBlock extends AbstractLongPressEatFoodBlock {

    public BowlFoodShapedBlock() {
        super(BlockProperty.FOOD_ON_SMALL_CONTAINER);
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return BowlBlock.SHAPE;
    }

}
