package cn.solarmoon.immersive_delight.common.block.crop;

import cn.solarmoon.immersive_delight.common.block.base.crop.AbstractTeaPlantCropBlock;
import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.common.block.IHorizontalFacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class GreenTeaPlantCropBlock extends AbstractTeaPlantCropBlock {

    protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(4D, 0.0D, 4D, 12D, 6.5D, 12D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16D, 16.0D)
    };

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    /**
     * 产物为绿茶叶
     */
    @Override
    public Item getHarvestItem() {
        return IMItems.GREEN_TEA_LEAVES.get();
    }

}
