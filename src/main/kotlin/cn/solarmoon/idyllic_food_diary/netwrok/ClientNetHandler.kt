package cn.solarmoon.idyllic_food_diary.netwrok

import cn.solarmoon.spark_core.api.network.CommonNetData
import cn.solarmoon.spark_core.api.network.ICommonNetHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

class ClientNetHandler: ICommonNetHandler {

    override fun handle(payload: CommonNetData, context: IPayloadContext) {
        val player = context.player() as LocalPlayer
        when (payload.message) {

        }
    }

    companion object {

    }

}