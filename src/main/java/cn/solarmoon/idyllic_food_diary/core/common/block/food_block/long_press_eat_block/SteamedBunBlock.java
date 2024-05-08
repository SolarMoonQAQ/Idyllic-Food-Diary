package cn.solarmoon.idyllic_food_diary.core.common.block.food_block.long_press_eat_block;

import cn.solarmoon.idyllic_food_diary.api.common.block.food_block.AbstractLongPressEatFoodBlock;
import cn.solarmoon.idyllic_food_diary.api.util.VoxelShapeUtil;
import cn.solarmoon.idyllic_food_diary.api.util.useful_data.BlockProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteamedBunBlock extends AbstractLongPressEatFoodBlock {

    public SteamedBunBlock() {
        super(BlockProperty.DOUGH);
    }

    @Override
    public Block getBlockLeft() {
        return Blocks.AIR;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return VoxelShapeUtil.rotateShape(state.getValue(FACING),
                Block.box(6.0D, 0.0D, 5.0D, 10.0D, 3.0D, 11.0D));
    }

}
