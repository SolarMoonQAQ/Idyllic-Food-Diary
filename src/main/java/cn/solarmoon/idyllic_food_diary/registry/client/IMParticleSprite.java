package cn.solarmoon.idyllic_food_diary.registry.client;

import cn.solarmoon.idyllic_food_diary.element.particle.CrashlessCloudParticle;
import cn.solarmoon.idyllic_food_diary.registry.common.IMParticles;
import net.minecraft.client.particle.BubbleParticle;

public class IMParticleSprite {
    public static void register() {
        IMParticles.CRASHLESS_CLOUD.provider(CrashlessCloudParticle.Provider::new);
    }
}
