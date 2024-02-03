package cn.solarmoon.immersive_delight.api.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.registries.DeferredRegister;

/**
 * 基本的粒子注册表
 */
public abstract class BaseParticleRegistry extends BaseObjectRegistry<ParticleType<?>> {

    protected ParticleEngine engine;

    public BaseParticleRegistry(DeferredRegister<ParticleType<?>> objects) {
        super(objects);
        this.engine = Minecraft.getInstance().particleEngine;
    }

    public abstract void register(RegisterParticleProvidersEvent event);

}
