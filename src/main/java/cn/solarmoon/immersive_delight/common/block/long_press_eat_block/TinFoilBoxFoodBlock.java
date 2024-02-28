package cn.solarmoon.immersive_delight.common.block.long_press_eat_block;

import cn.solarmoon.immersive_delight.common.block.base.AbstractLongPressEatFoodBlock;
import cn.solarmoon.immersive_delight.common.block.entity_block.TinFoilBoxEntityBlock;
import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TinFoilBoxFoodBlock extends AbstractLongPressEatFoodBlock {

    public TinFoilBoxFoodBlock() {
        super(TinFoilBoxEntityBlock.DEFAULT_PROPERTIES);
    }

    @Override
    public Block getBlockLeft() {
        return IMBlocks.TIN_FOIL_BOX.get();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return TinFoilBoxEntityBlock.DEFAULT_SHAPE;
    }

}
