package cn.solarmoon.immersive_delight.compat.farmersdelight;

import cn.solarmoon.immersive_delight.api.compat.BaseCompat;
import cn.solarmoon.immersive_delight.compat.farmersdelight.registry.FDItems;

/**
 * 农夫乐事
 */
public class FarmersDelight extends BaseCompat {

    public FarmersDelight() {
        super("farmersdelight");
    }

    @Override
    public void addRegistry() {
        modObjects.add(FDItems.FD_ITEMS);
    }

}
