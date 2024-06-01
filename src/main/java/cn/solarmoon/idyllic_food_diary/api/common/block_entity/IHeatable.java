package cn.solarmoon.idyllic_food_diary.api.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.util.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.api.common.block.ILitBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface IHeatable {

    default BlockEntity h() {
        return (BlockEntity) this;
    }

    default boolean isBelowHeating() {
        Level level = h().getLevel();
        BlockPos pos = h().getBlockPos();
        return level != null && FarmerUtil.isHeatSource(level.getBlockState(pos.below()));
    }

    default boolean isSelfHeating() {
        Level level = h().getLevel();
        BlockPos pos = h().getBlockPos();
        if (level != null) {
            BlockEntity eFixed = level.getBlockEntity(pos);
            if (eFixed != null) {
                BlockState state = eFixed.getBlockState();
                if (state.getValues().get(ILitBlock.LIT) != null) {
                    return eFixed.getBlockState().getValue(ILitBlock.LIT);
                }
            }
        }
        return false;
    }

    /**
     * @return 如果自身是炉灶，那么是否被加热就取决于是否lit，反之则还是看脚下方块
     */
    default boolean isHeatingConsiderStove() {
        BlockPos pos = h().getBlockPos();
        Level level = h().getLevel();
        if (level != null) {
            Block block = level.getBlockState(pos).getBlock();
            if (block == IMBlocks.STOVE.get()) return isSelfHeating();
            else return isBelowHeating();
        }
        return false;
    }

}
