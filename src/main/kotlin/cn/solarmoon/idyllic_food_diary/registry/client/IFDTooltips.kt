package cn.solarmoon.idyllic_food_diary.registry.client

import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockItem
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockItemTooltip
import cn.solarmoon.idyllic_food_diary.feature.tea_set_tooltip.TeaSetTooltip
import cn.solarmoon.spark_core.api.tooltip.TooltipOperator
import cn.solarmoon.spark_core.api.util.TooltipGatherUtil
import cn.solarmoon.spark_core.feature.inlay.InlayTooltip
import cn.solarmoon.spark_core.registry.client.SparkTooltips
import com.mojang.datafixers.util.Either
import net.minecraft.network.chat.FormattedText
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent
import net.neoforged.neoforge.client.event.RenderTooltipEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import vectorwing.farmersdelight.client.event.TooltipEvents

object IFDTooltips {

    @JvmStatic
    private fun addTooltips(event: RegisterClientTooltipComponentFactoriesEvent) {
        event.register(FoodBlockItemTooltip.Component::class.java, ::FoodBlockItemTooltip)
        event.register(TeaSetTooltip.Component::class.java, ::TeaSetTooltip)
    }

    @SubscribeEvent
    @JvmStatic
    private fun gatherTooltips(event: RenderTooltipEvent.GatherComponents) {
        event.tooltipElements.add(1, Either.right(FoodBlockItemTooltip.Component(event.itemStack)))
        event.tooltipElements.add(1, Either.right(TeaSetTooltip.Component(event.itemStack)))
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        NeoForge.EVENT_BUS.register(IFDTooltips::class.java)
        bus.addListener(this::addTooltips)
    }

}