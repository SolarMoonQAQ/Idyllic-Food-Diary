package cn.solarmoon.immersive_delight.common.blocks.longPressEatBlock;

import cn.solarmoon.immersive_delight.api.common.block.food.BaseLongPressEatFoodBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class CangshuMuttonSoupBlock extends BaseLongPressEatFoodBlock {

    public CangshuMuttonSoupBlock() {
        super(Blocks.BAMBOO, Block.Properties.of()
                .sound(SoundType.BAMBOO)
                .strength(0.5f)
        );
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

}
