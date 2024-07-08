package cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.plate;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.AbstractContainerBlock;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlateBlock extends AbstractContainerBlock {

    public PlateBlock(SoundType soundType) {
        super(soundType);
    }

    public static final VoxelShape SHAPE = Shapes.or(
            box(5, 0 , 5, 13, 1, 13),
            box(1, 1, 1, 15, 2, 15)
    );

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.CONTAINER.get();
    }

}
