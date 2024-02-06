package cn.solarmoon.immersive_delight.common.block.crop;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.immersive_delight.api.common.block.crop.BaseFruitCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
        return IMItems.DURIAN.get();
    }

    /**
     * 当成熟时被破坏将会变为掉落的榴莲方块
     */
    @Override
    public BlockState updateShape(BlockState state1, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos1, BlockPos pos2) {
        BlockState newState = Blocks.AIR.defaultBlockState();
        if (state1.getValue(BaseFruitCrop.AGE).equals(3)) newState = IMBlocks.DURIAN.get().defaultBlockState();
        return !state1.canSurvive(level, pos1) ? newState : super.updateShape(state1, direction, state2, level, pos1, pos2);
    }



}
