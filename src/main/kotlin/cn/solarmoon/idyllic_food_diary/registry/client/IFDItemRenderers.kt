package cn.solarmoon.idyllic_food_diary.registry.client

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneItemRenderer
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokItemClientExtensions
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokItemRenderer
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.long_plate.LongPlateItemRenderer
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.plate.PlateItemRenderer
import cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin.RollingPinClientExtensions
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.common.NeoForge

object IFDItemRenderers {

    private fun regRenderers(event: RegisterClientExtensionsEvent) {
        event.registerItem(customRenderer(MillstoneItemRenderer()), IFDItems.MILLSTONE)
        event.registerItem(WokItemClientExtensions(), IFDItems.WOK)
        event.registerItem(RollingPinClientExtensions(), IFDItems.ROLLING_PIN)
        event.registerItem(customRenderer(PlateItemRenderer()), IFDItems.WOODEN_PLATE, IFDItems.PORCELAIN_PLATE)
        event.registerItem(customRenderer(LongPlateItemRenderer()), IFDItems.LONG_WOODEN_PLATE, IFDItems.LONG_PORCELAIN_PLATE)
    }

    private fun customRenderer(renderer: BlockEntityWithoutLevelRenderer): IClientItemExtensions {
        return object : IClientItemExtensions {
            override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
                return renderer
            }
        }
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::regRenderers)
    }

}