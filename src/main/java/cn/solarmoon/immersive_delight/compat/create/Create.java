package cn.solarmoon.immersive_delight.compat.create;

import cn.solarmoon.immersive_delight.compat.create.registry.CItems;
import cn.solarmoon.solarmoon_core.compat.BaseCompat;

/**
 * 机械动力
 */
public class Create extends BaseCompat {

    public Create() {
        super("create");
    }

    @Override
    public void addRegistry() {
        add(CItems.C_ITEMS);
    }

}
