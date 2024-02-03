package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.api.registry.BaseParticleRegistry;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.SplashParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;

public class IMParticles extends BaseParticleRegistry {

    //粒子
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MOD_ID);

    public IMParticles() {
        super(PARTICLE_TYPES);
    }

    public static final RegistryObject<SimpleParticleType> HOT_MILK_BUBBLE = PARTICLE_TYPES.register("hot_milk_bubble", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> HOT_MILK_SPLASH = PARTICLE_TYPES.register("hot_milk_splash", () -> new SimpleParticleType(true));

    @Override
    public void register(RegisterParticleProvidersEvent event) {
        //继承原版的
        engine.register(HOT_MILK_BUBBLE.get(), BubbleParticle.Provider::new);
        engine.register(HOT_MILK_SPLASH.get(), SplashParticle.Provider::new);
    }

}
