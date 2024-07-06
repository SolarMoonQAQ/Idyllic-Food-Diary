package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.idyllic_food_diary.element.matter.stove.IBuiltInStove;
import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface IHeatable {

    default BlockEntity h() {
        return (BlockEntity) this;
    }

    default boolean isOnHeatSource() {
        Level level = h().getLevel();
        BlockPos pos = h().getBlockPos();
        BlockState state = h().getBlockState();
        if (state.getBlock() instanceof IBuiltInStove bis && bis.isNestedInStove(state)) {
            return state.getValue(ILitBlock.LIT);
        }
        return level != null && FarmerUtil.isHeatSource(level.getBlockState(pos.below()));
    }

}
