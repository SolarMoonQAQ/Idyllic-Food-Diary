package cn.solarmoon.immersive_delight.compat.appleskin;

import cn.solarmoon.immersive_delight.compat.appleskin.event.ShowCupFoodValueEvent;
import cn.solarmoon.solarmoon_core.compat.BaseCompat;

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
