package cn.solarmoon.idyllic_food_diary.registry.client

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.cleaver.CleaverGui
import cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin.RollingGui
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers

object IFDGuis {

    @JvmStatic
    val ROLLING = RollingGui()
    @JvmStatic
    val CLEAVER = CleaverGui()

    fun reg(event: RegisterGuiLayersEvent) {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, id("rolling"), ROLLING)
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, id("cleaver"), CLEAVER)
    }

    private fun id(name: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(IdyllicFoodDiary.MOD_ID, name)
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::reg)
    }

}