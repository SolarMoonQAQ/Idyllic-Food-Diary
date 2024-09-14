package cn.solarmoon.idyllic_food_diary.registry.client

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneBlockRenderer
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokBlockRenderer
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockRenderer
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.long_plate.LongPlateBlockRenderer
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.plate.PlateBlockRenderer
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.event.EntityRenderersEvent

object IFDBlockEntityRenderers {

    private fun regRenderer(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerBlockEntityRenderer(IFDBlockEntities.MILLSTONE.get(), ::MillstoneBlockRenderer)
        event.registerBlockEntityRenderer(IFDBlockEntities.WOK.get(), ::WokBlockRenderer)
        event.registerBlockEntityRenderer(IFDBlockEntities.PLATE.get(), ::PlateBlockRenderer)
        event.registerBlockEntityRenderer(IFDBlockEntities.LONG_PLATE.get(), ::LongPlateBlockRenderer)
        event.registerBlockEntityRenderer(IFDBlockEntities.FOOD.get(), ::FoodBlockRenderer)
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::regRenderer)
    }

}