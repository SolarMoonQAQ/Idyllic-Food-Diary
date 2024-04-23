package cn.solarmoon.idyllic_food_diary.common.block.food_block.obtainable_food_block;

import cn.solarmoon.idyllic_food_diary.common.block.base.AbstractObtainableFoodBlock;
import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.idyllic_food_diary.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;

public class RoastedSucklingPigBlock extends AbstractObtainableFoodBlock implements IBedPartBlock {

    public RoastedSucklingPigBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(1)
                .sound(SoundType.BAMBOO)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY));
    }

    @Override
    public HashMap<Integer, ItemStack> setSpecialTakenMap() {
        HashMap<Integer, ItemStack> map = new HashMap<>();
        map.put(1, new ItemStack(IMItems.ROASTED_SUCKLING_PIG_HEAD.get()));
        return map;
    }

    @Override
    public int getMaxRemain() {
        return 5;
    }

    @Override
    public ItemStack getFood() {
        return new ItemStack(IMItems.ROASTED_SUCKLING_PORK.get());
    }

    @Override
    public Item getContainer() {
        return Items.AIR;
    }

    @Override
    public Block getBlockLeft() {
        return IMBlocks.LONG_WOODEN_PLATE.get();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(0, 0, 0, 16, 16, 16);
    }
}
