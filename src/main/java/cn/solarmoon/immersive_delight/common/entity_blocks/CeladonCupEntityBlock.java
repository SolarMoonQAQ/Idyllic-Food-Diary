package cn.solarmoon.immersive_delight.common.entity_blocks;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.DrinkableEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * 青瓷杯方块
 */
public class CeladonCupEntityBlock extends DrinkableEntityBlock {

    public CeladonCupEntityBlock() {
        super(Block.Properties.of()
                .sound(SoundType.GLASS)
        );
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMEntityBlocks.CELADON_CUP_ENTITY.get();
    }

    /**
     * 碰撞箱
     */
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

}
