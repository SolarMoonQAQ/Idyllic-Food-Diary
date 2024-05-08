package cn.solarmoon.idyllic_food_diary.core.common.block.crop;

import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlocks;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.api.common.block.crop.BaseHangingBushCropBlock;
import cn.solarmoon.solarmoon_core.api.util.device.BlockMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DurianCropBlock extends BaseHangingBushCropBlock {

    public DurianCropBlock() {}

    @Override
    public int getMaxAge() {
        return 3;
    }

    /**
     * 可存在于丛林木下方
     */
    @Override
    public BlockMatcher canSurviveBlock() {
        return BlockMatcher.create(BlockTags.JUNGLE_LOGS);
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
        if (state1.getValue(BaseHangingBushCropBlock.AGE).equals(3)) newState = IMBlocks.DURIAN.get().defaultBlockState();
        return !state1.canSurvive(level, pos1) ? newState : super.updateShape(state1, direction, state2, level, pos1, pos2);
    }

}
