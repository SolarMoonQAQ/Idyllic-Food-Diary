package cn.solarmoon.immersive_delight.common.block.entity_block;

import cn.solarmoon.immersive_delight.common.block.base.entity_block.AbstractTinFoilBoxEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TinFoilBoxEntityBlock extends AbstractTinFoilBoxEntityBlock {

    public static final Properties DEFAULT_PROPERTIES = BlockBehaviour.Properties.of().sound(SoundType.BAMBOO).strength(1f);
    public static final VoxelShape DEFAULT_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    public TinFoilBoxEntityBlock() {
        super(DEFAULT_PROPERTIES);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return DEFAULT_SHAPE;
    }

}
