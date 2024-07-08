package cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.long_plate;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.AbstractLongContainerBlock;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.util.VoxelShapeUtil;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IBedPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LongPlateBlock extends AbstractLongContainerBlock implements IBedPartBlock {

    public LongPlateBlock(SoundType soundType) {
        super(soundType);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape combine1 = Shapes.or(box(1, 1, 1, 15, 2, 16),
                box(3, 0, 3, 13, 1, 16));
        VoxelShape combine2 = Shapes.or(box(1, 1, 0, 15, 2, 15),
                box(3, 0, 0, 13, 1, 13));
        return state.getValue(PART) == BedPart.HEAD ? VoxelShapeUtil.rotateShape(state.getValue(FACING), combine1) :
                VoxelShapeUtil.rotateShape(state.getValue(FACING), combine2);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.LONG_CONTAINER.get();
    }

}
