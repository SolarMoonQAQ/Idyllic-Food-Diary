package cn.solarmoon.idyllic_food_diary.common.block.container;

import cn.solarmoon.idyllic_food_diary.common.block.base.container.AbstractContainerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BowlBlock extends AbstractContainerBlock {

    public BowlBlock() {
        super(SoundType.BAMBOO);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.or(Block.box(4, 0, 4, 12, 1, 12),
                Block.box(3, 1, 3, 4, 8, 13),
                Block.box(3, 1, 3, 13, 8, 4),
                Block.box(3, 1, 12, 13, 8, 13),
                Block.box(12, 1, 3, 13, 8, 13));
    }

    @Override
    public Item asItem() {
        return Items.BOWL;
    }

}
