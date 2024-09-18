package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.particle.CrashlessCloudParticle
import cn.solarmoon.idyllic_food_diary.netwrok.ClientNetHandler
import cn.solarmoon.idyllic_food_diary.netwrok.ServerNetHandler
import cn.solarmoon.spark_core.api.network.CommonNetRegister
import net.minecraft.core.particles.SimpleParticleType

object IFDParticles {

    @JvmStatic
    fun register() {
    }

    @JvmStatic
    val CRASHLESS_CLOUD = IdyllicFoodDiary.REGISTER.particle<SimpleParticleType>()
        .id("crashless_cloud")
        .bound { SimpleParticleType(false) }
        .build()

}