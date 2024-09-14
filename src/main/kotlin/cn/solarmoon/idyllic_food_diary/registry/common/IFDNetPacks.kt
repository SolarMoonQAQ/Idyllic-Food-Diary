package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.netwrok.ClientNetHandler
import cn.solarmoon.idyllic_food_diary.netwrok.ServerNetHandler
import cn.solarmoon.spark_core.api.network.CommonNetRegister

object IFDNetPacks {

    @JvmStatic
    fun register() {
        CommonNetRegister.register(Pair(ClientNetHandler(), ServerNetHandler()))
    }

}