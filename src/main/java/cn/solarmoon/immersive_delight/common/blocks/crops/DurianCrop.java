package cn.solarmoon.immersive_delight.common.blocks.crops;

import cn.solarmoon.immersive_delight.common.IMBlocks;
import cn.solarmoon.immersive_delight.common.IMItems;
import cn.solarmoon.immersive_delight.common.blocks.crops.abstract_crops.BaseFruitCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DurianCrop extends BaseFruitCrop {

    public DurianCrop() {}

    /**
     * 可存在于丛林木下方
     */
    @Override
    public TagKey<Block> canSurviveTag() {
        return BlockTags.JUNGLE_LOGS;
    }

    /**
     * 收割物为榴莲（就是方块形式的那个）
     */
    @Override
    public Item getHarvestItem() {
        return IMItems.DURIAN_BLOCK.get();
    }

    /**
     * 当成熟时被破坏将会变为掉落的榴莲方块
     */
    @Override
    public BlockState updateShape(BlockState state1, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos1, BlockPos pos2) {
        BlockState newState = Blocks.AIR.defaultBlockState();
        if (state1.getValue(BaseFruitCrop.AGE).equals(3)) newState = IMBlocks.DURIAN_BLOCK.get().defaultBlockState();
        return !state1.canSurvive(level, pos1) ? newState : super.updateShape(state1, direction, state2, level, pos1, pos2);
    }



}
