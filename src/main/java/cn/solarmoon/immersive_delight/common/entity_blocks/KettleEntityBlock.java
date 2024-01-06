package cn.solarmoon.immersive_delight.common.entity_blocks;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.WaterKettleEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * 水壶方块
 */
public class KettleEntityBlock extends WaterKettleEntityBlock {

    public KettleEntityBlock() {
        super(Block.Properties.of()
                .sound(SoundType.LANTERN)
                .strength(1.0f, 6.0F)
                .mapColor(MapColor.METAL)
        );
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMEntityBlocks.KETTLE_ENTITY.get();
    }

    /**
     * 碰撞箱
     */
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

}
