package cn.solarmoon.idyllic_food_diary.data

import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.data.event.GatherDataEvent


object DataGenerater {

    @SubscribeEvent
    private fun gather(event: GatherDataEvent) {
        val generator = event.generator
        val output = generator.packOutput
        val lookupProvider = event.lookupProvider
        val helper = event.existingFileHelper

        generator.addProvider(event.includeServer(), IFDBlockTags(output, lookupProvider, helper))
        generator.addProvider(event.includeServer(), RecipeDataProvider(output, lookupProvider))
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::gather)
    }

}