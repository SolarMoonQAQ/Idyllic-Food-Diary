package cn.solarmoon.immersive_delight.common.block.longPressEatBlock;

import cn.solarmoon.immersive_delight.common.block.base.AbstractLongPressEatFoodBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * 面饼方块
 */
public class FlatbreadDoughBlock extends AbstractLongPressEatFoodBlock {

    public FlatbreadDoughBlock() {
        super(Block.Properties
                .copy(Blocks.CAKE)
                .destroyTime(1f));
    }

    /**
     * 碰撞箱
     */
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    }

}
