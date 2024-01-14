package cn.solarmoon.immersive_delight.client;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SplashParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.init.ObjectRegister.PARTICLE_TYPES;


@Mod.EventBusSubscriber(modid = ImmersiveDelight.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMParticles {

    public static final RegistryObject<SimpleParticleType> HOT_MILK_BUBBLE = PARTICLE_TYPES.register("hot_milk_bubble", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> HOT_MILK_SPLASH = PARTICLE_TYPES.register("hot_milk_splash", () -> new SimpleParticleType(true));

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;
        //继承原版的
        engine.register(HOT_MILK_BUBBLE.get(), BubbleParticle.Provider::new);
        engine.register(HOT_MILK_SPLASH.get(), SplashParticle.Provider::new);
    }



}
