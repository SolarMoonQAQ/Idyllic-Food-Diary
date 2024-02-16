package cn.solarmoon.immersive_delight.compat.farmersdelight;

import cn.solarmoon.immersive_delight.compat.farmersdelight.registry.FDItems;
import cn.solarmoon.solarmoon_core.compat.BaseCompat;

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
