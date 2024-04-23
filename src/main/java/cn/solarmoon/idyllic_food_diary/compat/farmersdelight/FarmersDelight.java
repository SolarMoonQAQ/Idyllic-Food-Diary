package cn.solarmoon.idyllic_food_diary.compat.farmersdelight;

import cn.solarmoon.idyllic_food_diary.compat.farmersdelight.registry.FDItems;
import cn.solarmoon.solarmoon_core.api.compat.BaseCompat;

/**
 * 农夫乐事
 */
public class FarmersDelight extends BaseCompat {

    public FarmersDelight() {
        super("farmersdelight");
    }

    @Override
    public void addRegistry() {
        add(FDItems.FD_ITEMS);
    }

}
