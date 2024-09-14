package cn.solarmoon.idyllic_food_diary.feature.hug_item

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.RenderArmEvent
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

class Prevent1stPArmRenderWhenHug {

    @SubscribeEvent
    fun render(event: RenderArmEvent) {
        val player = event.player
        val f1 = IClientItemExtensions.of(player.mainHandItem) is IHuggableItemExtensions && player.offhandItem.isEmpty
        val f2 = IClientItemExtensions.of(player.offhandItem) is IHuggableItemExtensions && player.mainHandItem.isEmpty
        if (f1 || f2) {
            event.isCanceled = true
        }
    }

}