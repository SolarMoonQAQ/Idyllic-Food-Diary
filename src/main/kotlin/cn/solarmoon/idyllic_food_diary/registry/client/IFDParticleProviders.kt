package cn.solarmoon.idyllic_food_diary.registry.client

import cn.solarmoon.idyllic_food_diary.element.particle.CrashlessCloudParticle
import cn.solarmoon.idyllic_food_diary.element.particle.CrashlessCloudParticle.Provider
import cn.solarmoon.idyllic_food_diary.registry.common.IFDParticles
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent

object IFDParticleProviders {

    fun reg(event: RegisterParticleProvidersEvent) {
        event.registerSpriteSet(IFDParticles.CRASHLESS_CLOUD.get(), ::Provider)
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::reg)
    }

}