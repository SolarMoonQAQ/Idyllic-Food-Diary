package cn.solarmoon.immersive_delight.common.block.long_press_eat_block;

import cn.solarmoon.immersive_delight.common.block.base.AbstractLongPressEatFoodBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteamedBunBlock extends AbstractLongPressEatFoodBlock {

    public SteamedBunBlock() {
        super(Block.Properties
                .copy(Blocks.CAKE)
                .destroyTime(1f));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

}
