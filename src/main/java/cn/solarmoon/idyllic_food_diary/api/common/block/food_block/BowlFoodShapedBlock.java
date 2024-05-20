package cn.solarmoon.idyllic_food_diary.api.common.block.food_block;

import cn.solarmoon.idyllic_food_diary.api.util.useful_data.BlockProperty;
import cn.solarmoon.idyllic_food_diary.core.common.block.container.BowlBlock;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BowlFoodShapedBlock extends AbstractLongPressEatFoodBlock {

    public BowlFoodShapedBlock() {
        super(BlockProperty.FOOD_IN_BOWL);
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return BowlBlock.SHAPE;
    }

}
