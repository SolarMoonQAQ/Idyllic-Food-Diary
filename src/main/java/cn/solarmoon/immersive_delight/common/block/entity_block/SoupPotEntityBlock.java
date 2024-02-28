package cn.solarmoon.immersive_delight.common.block.entity_block;

import cn.solarmoon.immersive_delight.common.block.base.entity_block.AbstractSoupPotEntityBlock;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoupPotEntityBlock extends AbstractSoupPotEntityBlock {

    public SoupPotEntityBlock() {
        super(Block.Properties.of()
                .sound(SoundType.LANTERN)
                .strength(2f, 6.0F)
                .mapColor(MapColor.METAL));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.SOUP_POT.get();
    }
}
