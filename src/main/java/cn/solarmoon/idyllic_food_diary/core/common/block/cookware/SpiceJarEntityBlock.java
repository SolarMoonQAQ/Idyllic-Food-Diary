package cn.solarmoon.idyllic_food_diary.core.common.block.cookware;

import cn.solarmoon.idyllic_food_diary.api.common.block.cookware.AbstractSpiceJarEntityBlock;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SpiceJarEntityBlock extends AbstractSpiceJarEntityBlock {

    public SpiceJarEntityBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.SPICE_JAR.get();
    }

}
