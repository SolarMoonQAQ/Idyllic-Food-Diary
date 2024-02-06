package cn.solarmoon.immersive_delight.compat.appleskin;

import cn.solarmoon.immersive_delight.api.compat.BaseCompat;
import cn.solarmoon.immersive_delight.compat.appleskin.event.ShowCupFoodValueEvent;

/**
 * 苹果皮
 */
public class AppleSkin extends BaseCompat {

    public AppleSkin() {
        super("appleskin");
    }

    @Override
    public void addRegistry() {
        modEvents.add(new ShowCupFoodValueEvent());
    }


}
