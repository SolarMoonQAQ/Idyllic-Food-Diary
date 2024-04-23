package cn.solarmoon.idyllic_food_diary.compat.create;

import cn.solarmoon.idyllic_food_diary.compat.create.registry.CItems;
import cn.solarmoon.solarmoon_core.api.compat.BaseCompat;

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
