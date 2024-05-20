package cn.solarmoon.idyllic_food_diary.core.common.block.cookware;

import cn.solarmoon.idyllic_food_diary.api.common.block.cookware.AbstractCookingPotEntityBlock;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoupPotEntityBlock extends AbstractCookingPotEntityBlock {

    public SoupPotEntityBlock() {
        super(Block.Properties.of()
                .sound(SoundType.LANTERN)
                .strength(2f, 6.0F)
                .noOcclusion()
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape1 = Block.box(1.0D, 0.0D, 1.0D, 15D, 16D, 15D);
        VoxelShape shape2 = Block.box(3, 1, 3, 13, 16, 13);
        return Shapes.joinUnoptimized(shape1, shape2, BooleanOp.ONLY_FIRST);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.SOUP_POT.get();
    }

}
