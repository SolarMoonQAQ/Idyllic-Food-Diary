package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle

import cn.solarmoon.idyllic_food_diary.netwrok.ServerNetHandler
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import cn.solarmoon.spark_core.api.network.CommonNetData
import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.InputEvent
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.max

class KettleFluidInteractValueSelector {

    @SubscribeEvent
    fun select(event: InputEvent.MouseScrollingEvent) {
        val ins = Minecraft.getInstance()
        val player = ins.player ?: return
        val kettle = player.mainHandItem
        if (player.isCrouching && kettle.item is KettleItem) {
            val scroll = event.scrollDeltaY
            val baseValue = kettle.get(IFDDataComponents.FLUID_INTERACT_VALUE)!!
            val increment = (scroll * 50).toInt()
            val newValue = max(baseValue + increment, 0)
            PacketDistributor.sendToServer(CommonNetData(intValue = newValue, message = ServerNetHandler.KETTLE_SELECT))
            event.isCanceled = true
        }
    }

}