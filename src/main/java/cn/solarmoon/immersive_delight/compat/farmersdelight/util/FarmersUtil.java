package cn.solarmoon.immersive_delight.compat.farmersdelight.util;

import cn.solarmoon.immersive_delight.compat.farmersdelight.FarmersDelight;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;

public class FarmersUtil {

    public static Item getDough() {
        if(new FarmersDelight().isLoaded()) {
            return ModItems.WHEAT_DOUGH.get();
        }
        return null;
    }

    public static boolean isHeatSource(BlockState state) {
        return new FarmersDelight().isLoaded() && state.is(ModTags.HEAT_SOURCES);
    }

}
