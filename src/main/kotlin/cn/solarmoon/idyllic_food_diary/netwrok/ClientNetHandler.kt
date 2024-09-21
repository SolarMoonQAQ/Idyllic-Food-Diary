package cn.solarmoon.idyllic_food_diary.netwrok

import cn.solarmoon.spark_core.api.network.CommonNetData
import cn.solarmoon.spark_core.api.network.ICommonNetHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

class ClientNetHandler: ICommonNetHandler {

    override fun handle(payload: CommonNetData, context: IPayloadContext) {
        val player = context.player() as LocalPlayer
        val level = player.level() as ClientLevel
        when (payload.message) {
            EAT_PARTICLE -> {
                level.getPlayerByUUID(payload.uuid)?.spawnItemParticles(payload.itemStack, 5)
            }
        }
    }

    companion object {
        const val EAT_PARTICLE = "EatParticle"
    }

}