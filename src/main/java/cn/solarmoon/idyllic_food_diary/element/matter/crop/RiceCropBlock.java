package cn.solarmoon.idyllic_food_diary.element.matter.crop;

import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.block_base.BaseMultilayerCropBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IWaterLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RiceCropBlock extends BaseMultilayerCropBlock implements IWaterLoggedBlock {

    @Override
    protected ItemLike getBaseSeedId() {
        return IMItems.RICE.get();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public int getMaxLayer() {
        return 1;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    public boolean canSurviveEachLayer(BlockState state, BlockGetter levelReader, BlockPos pos, int layer) {
        return (layer == 0) == state.getValue(WATERLOGGED);
    }


    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return super.mayPlaceOn(state, getter, pos) || state.is(BlockTags.DIRT);
    }

}
