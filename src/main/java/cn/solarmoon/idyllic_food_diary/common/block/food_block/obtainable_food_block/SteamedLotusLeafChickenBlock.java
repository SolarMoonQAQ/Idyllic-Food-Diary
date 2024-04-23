package cn.solarmoon.idyllic_food_diary.common.block.food_block.obtainable_food_block;

import cn.solarmoon.idyllic_food_diary.common.block.base.AbstractObtainableFoodBlock;
import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.idyllic_food_diary.common.registry.IMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteamedLotusLeafChickenBlock extends AbstractObtainableFoodBlock {

    public SteamedLotusLeafChickenBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(1)
                .sound(SoundType.BAMBOO)
                .noOcclusion());
    }

    @Override
    public ItemStack getFood() {
        return new ItemStack(IMItems.STEAMED_LOTUS_LEAF_CHICKEN_MEAT.get());
    }

    @Override
    public Item getContainer() {
        return Items.AIR;
    }

    @Override
    public Block getBlockLeft() {
        return IMBlocks.WOODEN_PLATE.get();
    }

    @Override
    public int getMaxRemain() {
        return 4;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }
}
