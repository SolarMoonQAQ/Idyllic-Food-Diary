package cn.solarmoon.idyllic_food_diary.registry.common;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.solarmoon_core.api.entry.common.ParticleEntry;
import net.minecraft.core.particles.SimpleParticleType;

public class IMParticles {
    public static void register() {}

    public static final ParticleEntry<SimpleParticleType> CRASHLESS_CLOUD = IdyllicFoodDiary.REGISTRY.particle()
            .id("crashless_cloud")
            .bound(() -> new SimpleParticleType(false))
            .build();

}
