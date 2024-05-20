package cn.solarmoon.idyllic_food_diary.core.common.block.cookware;

import cn.solarmoon.idyllic_food_diary.api.common.block.cookware.AbstractSteamerBaseEntityBlock;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteamerBaseEntityBlock extends AbstractSteamerBaseEntityBlock {

    public SteamerBaseEntityBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.BAMBOO)
                .strength(2)
                .noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape[] shapes = new VoxelShape[] {
                Block.box(0.5, 0, 0.5, 15.5, 2, 15.5),
                Block.box(1, 2, 1, 15, 7, 15),
                Block.box(0.5, 7, 0.5, 15.5, 9, 15.5),
                Block.box(1, 9, 1, 15, 14, 15),
        };
        VoxelShape top = Block.box(0.5, 14, 0.5, 15.5, 16, 15.5);
        VoxelShape mix = Shapes.joinUnoptimized(top, Block.box(2, 15, 2, 14, 16, 14), BooleanOp.ONLY_FIRST);
        return Shapes.joinUnoptimized(Shapes.or(mix, shapes), Block.box(2, 0, 2, 14, 1, 14), BooleanOp.ONLY_FIRST);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.STEAMER_BASE.get();
    }

}
