package cn.solarmoon.immersive_delight.common.block;

import cn.solarmoon.solarmoon_core.common.block.BaseStackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class GarlicBlock extends BaseStackBlock {


    public GarlicBlock() {
        super(5, BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).instabreak());
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D);
    }

}
