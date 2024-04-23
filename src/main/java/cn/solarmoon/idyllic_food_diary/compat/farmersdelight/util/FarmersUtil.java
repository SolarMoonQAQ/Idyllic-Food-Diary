package cn.solarmoon.idyllic_food_diary.compat.farmersdelight.util;

import cn.solarmoon.idyllic_food_diary.compat.farmersdelight.FarmersDelight;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.tag.ModTags;

public class FarmersUtil {

    public static boolean isHeatSource(BlockState state) {
        return new FarmersDelight().isLoaded() && state.is(ModTags.HEAT_SOURCES);
    }

}
