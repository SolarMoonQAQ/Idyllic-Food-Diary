package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.idyllic_food_diary.compat.farmersdelight.FarmersUtil;
import cn.solarmoon.idyllic_food_diary.data.IMBlockTags;
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

}
