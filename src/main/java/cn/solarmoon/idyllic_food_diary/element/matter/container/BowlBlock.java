package cn.solarmoon.idyllic_food_diary.element.matter.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BowlBlock extends AbstractContainerBlock {

    public BowlBlock() {
        super(SoundType.BAMBOO);
    }

    public static final VoxelShape SHAPE = Shapes.or(box(5, 0, 5, 11, 1, 11),
            Shapes.joinUnoptimized(box(4, 1, 4, 12, 5, 12),
                    box(5, 2, 5, 11, 5, 11), BooleanOp.ONLY_FIRST));
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public Item asItem() {
        return Items.BOWL;
    }

}
