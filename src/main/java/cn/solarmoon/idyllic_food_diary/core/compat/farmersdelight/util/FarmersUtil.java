package cn.solarmoon.idyllic_food_diary.core.compat.farmersdelight.util;

import cn.solarmoon.idyllic_food_diary.core.compat.farmersdelight.FarmersDelight;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.tag.ModTags;

public class FarmersUtil {

    /**
     * @return 该方块是否为农夫乐事的热源方块
     */
    public static boolean isHeatSource(BlockState state) {
        return FarmersDelight.isLoaded() && state.is(ModTags.HEAT_SOURCES);
    }

}
