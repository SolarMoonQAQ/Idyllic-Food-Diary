package cn.solarmoon.idyllic_food_diary.common.registry;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.solarmoon_core.api.registry.IRegister;
import cn.solarmoon.solarmoon_core.api.registry.object.ParticleEntry;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.SplashParticle;
import net.minecraft.core.particles.SimpleParticleType;

public enum IMParticles implements IRegister {
    INSTANCE;

    public static final ParticleEntry<SimpleParticleType> HOT_MILK_BUBBLE = IdyllicFoodDiary.REGISTRY.particle()
            .id("hot_milk_bubble")
            .bound(() -> new SimpleParticleType(true))
            .provider(BubbleParticle.Provider::new)
            .build();

    public static final ParticleEntry<SimpleParticleType> HOT_MILK_SPLASH = IdyllicFoodDiary.REGISTRY.particle()
            .id("hot_milk_splash")
            .bound(() -> new SimpleParticleType(true))
            .provider(SplashParticle.Provider::new)
            .build();

    public static final ParticleEntry<SimpleParticleType> GREEN_TEA_BUBBLE = IdyllicFoodDiary.REGISTRY.particle()
            .id("green_tea_bubble")
            .bound(() -> new SimpleParticleType(true))
            .provider(BubbleParticle.Provider::new)
            .build();

    public static final ParticleEntry<SimpleParticleType> GREEN_TEA_SPLASH = IdyllicFoodDiary.REGISTRY.particle()
            .id("green_tea_splash")
            .bound(() -> new SimpleParticleType(true))
            .provider(SplashParticle.Provider::new)
            .build();

    public static final ParticleEntry<SimpleParticleType> BLACK_TEA_BUBBLE = IdyllicFoodDiary.REGISTRY.particle()
            .id("black_tea_bubble")
            .bound(() -> new SimpleParticleType(true))
            .provider(BubbleParticle.Provider::new)
            .build();

    public static final ParticleEntry<SimpleParticleType> BLACK_TEA_SPLASH = IdyllicFoodDiary.REGISTRY.particle()
            .id("black_tea_splash")
            .bound(() -> new SimpleParticleType(true))
            .provider(SplashParticle.Provider::new)
            .build();

}
