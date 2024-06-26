package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.idyllic_food_diary.compat.farmersdelight.FarmersUtil;
import cn.solarmoon.idyllic_food_diary.data.IMBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;

/**
 * 杂项实用方法
 */
public class FarmerUtil {

    /**
     * 检查是否为热源
     */
    public static boolean isHeatSource(BlockState state) {
        boolean commonFlag = state.is(IMBlockTags.HEAT_SOURCE) || FarmersUtil.isHeatSource(state);
        if (state.hasProperty(LIT)) {
            return commonFlag && state.getValue(LIT);
        }
        return commonFlag;
    }

    public static boolean isOnHeatSource(BlockEntity be) {
        Level level = be.getLevel();
        if (level == null) return false;
        BlockPos pos = be.getBlockPos();
        BlockState below = level.getBlockState(pos.below());
        return isHeatSource(below);
    }

}
