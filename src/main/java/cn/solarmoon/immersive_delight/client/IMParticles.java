package cn.solarmoon.immersive_delight.client;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.client.particles.PouringParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

import static cn.solarmoon.immersive_delight.init.ObjectRegister.PARTICLE_TYPES;
import static cn.solarmoon.immersive_delight.util.Constants.mc;


@Mod.EventBusSubscriber(modid = ImmersiveDelight.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMParticles {

    public static final RegistryObject<SimpleParticleType> POURING = PARTICLE_TYPES.register("pouring", () -> new SimpleParticleType(false));

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {

    }



}
