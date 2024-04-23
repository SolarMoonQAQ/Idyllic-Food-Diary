package cn.solarmoon.idyllic_food_diary.compat.appleskin;

import cn.solarmoon.idyllic_food_diary.compat.appleskin.event.ShowCupFoodValueEvent;
import cn.solarmoon.solarmoon_core.api.compat.BaseCompat;

/**
 * 苹果皮
 */
public class AppleSkin extends BaseCompat {

    public AppleSkin() {
        super("appleskin");
    }

    @Override
    public void addRegistry() {
        add(new ShowCupFoodValueEvent());
    }


}
