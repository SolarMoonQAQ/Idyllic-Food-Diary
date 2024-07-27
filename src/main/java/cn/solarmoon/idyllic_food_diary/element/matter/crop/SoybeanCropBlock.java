package cn.solarmoon.idyllic_food_diary.element.matter.crop;

import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.block_base.BaseCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoybeanCropBlock extends BaseCropBlock {

    @Override
    protected ItemLike getBaseSeedId() {
        return IMItems.SOYBEANS.get();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

}
