package cn.solarmoon.immersive_delight.common.blocks;

import cn.solarmoon.immersive_delight.api.common.block.food.BaseTakenFoodBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class test extends BaseTakenFoodBlock {
    public test() {
        super(Block.Properties.copy(Blocks.CAKE), 4, Items.APPLE, Items.BOWL);
    }

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
