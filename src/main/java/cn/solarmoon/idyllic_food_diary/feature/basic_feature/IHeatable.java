package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IHeatable {

    default BlockEntity h() {
        return (BlockEntity) this;
    }

    default boolean isOnHeatSource() {
        Level level = h().getLevel();
        BlockPos pos = h().getBlockPos();
        return level != null && FarmerUtil.isHeatSource(level.getBlockState(pos.below()));
    }

}
