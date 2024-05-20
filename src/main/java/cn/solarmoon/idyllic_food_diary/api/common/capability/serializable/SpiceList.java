package cn.solarmoon.idyllic_food_diary.api.common.capability.serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 调料专用表，add后会找到相同的添加数量，没找到才会调用原来的add
 */
public class SpiceList extends ArrayList<Spice> {

    @Override
    public boolean add(Spice spice) {
        for (Spice origin : this) {
            if (spice.isSame(origin)) {
                origin.add(spice.getAmount());
                return true;
            }
        }
        return super.add(spice);
    }

    @Override
    public boolean addAll(Collection<? extends Spice> spices) {
        boolean isModified = false;
        for (var spice : spices) {
            isModified |= add(spice);
        }
        return isModified;
    }

    public static SpiceList copyOf(SpiceList spices) {
        SpiceList copy = new SpiceList();
        copy.addAll(spices);
        return copy;
    }

}
