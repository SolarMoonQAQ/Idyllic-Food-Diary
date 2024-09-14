package cn.solarmoon.idyllic_food_diary.registry.client

import cn.solarmoon.idyllic_food_diary.element.matter.cleaver.CleaverSelector
import cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin.RollingSelector
import cn.solarmoon.idyllic_food_diary.feature.hug_item.Prevent1stPArmRenderWhenHug
import net.neoforged.neoforge.common.NeoForge

object IFDClientEvents {

    @JvmStatic
    fun register() {
        RollingSelector().register()
        CleaverSelector().register()
        add(Prevent1stPArmRenderWhenHug())
    }

    private fun add(event: Any) {
        NeoForge.EVENT_BUS.register(event)
    }

}