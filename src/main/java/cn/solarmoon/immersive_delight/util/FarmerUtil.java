package cn.solarmoon.immersive_delight.util;

import cn.solarmoon.immersive_delight.compat.farmersdelight.util.FarmersUtil;
import cn.solarmoon.immersive_delight.data.tags.IMBlockTags;
import net.minecraft.world.level.block.state.BlockState;

public class FarmerUtil {

    /**
     * 检查是否为热源
     */
    public static boolean isHeatSource(BlockState state) {
        return state.is(IMBlockTags.HEAT_SOURCE) ||
                FarmersUtil.isHeatSource(state);
    }

}
