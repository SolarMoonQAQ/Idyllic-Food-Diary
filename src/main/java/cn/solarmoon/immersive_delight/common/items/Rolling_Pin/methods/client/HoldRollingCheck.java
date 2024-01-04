package cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static cn.solarmoon.immersive_delight.common.RegisterItems.ROLLING_PIN;
import static cn.solarmoon.immersive_delight.util.Constants.mc;


@OnlyIn(Dist.CLIENT)
public class HoldRollingCheck {
    /**
     * 如果拿着擀面杖，主副手任意一个为空，就返回true
     */
    public static boolean holdRollingCheck() {
        boolean isHoldingRollingPin;
        boolean isAnyHandEmpty;
        if (mc.player != null) {
            isHoldingRollingPin = mc.player.getMainHandItem().is(ROLLING_PIN.get().asItem())
                    || mc.player.getOffhandItem().is(ROLLING_PIN.get().asItem());
            isAnyHandEmpty = mc.player.getMainHandItem().isEmpty()
                    || mc.player.getOffhandItem().isEmpty();
        } else {
            return false;
        }
        return isHoldingRollingPin && isAnyHandEmpty;
    }
}
