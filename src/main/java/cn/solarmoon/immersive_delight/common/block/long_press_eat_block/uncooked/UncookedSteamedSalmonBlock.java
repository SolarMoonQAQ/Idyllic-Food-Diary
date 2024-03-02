package cn.solarmoon.immersive_delight.common.block.long_press_eat_block.uncooked;

import cn.solarmoon.immersive_delight.common.block.base.AbstractLongPressEatFoodDoubleBlock;
import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class UncookedSteamedSalmonBlock extends AbstractLongPressEatFoodDoubleBlock {

    @Override
    public Block getBlockLeft() {
        return IMBlocks.LONG_PORCELAIN_PLATE.get();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

}
