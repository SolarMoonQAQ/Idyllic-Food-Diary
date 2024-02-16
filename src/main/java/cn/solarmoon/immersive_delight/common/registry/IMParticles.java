package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.ParticleEntry;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.SplashParticle;
import net.minecraft.core.particles.SimpleParticleType;

public enum IMParticles implements IRegister {
    INSTANCE;

    public static final ParticleEntry<SimpleParticleType> HOT_MILK_BUBBLE = ImmersiveDelight.REGISTRY.particle()
            .id("hot_milk_bubble")
            .bound(() -> new SimpleParticleType(true))
            .provider(BubbleParticle.Provider::new)
            .build();

    public static final ParticleEntry<SimpleParticleType> HOT_MILK_SPLASH = ImmersiveDelight.REGISTRY.particle()
            .id("hot_milk_splash")
            .bound(() -> new SimpleParticleType(true))
            .provider(SplashParticle.Provider::new)
            .build();

}
