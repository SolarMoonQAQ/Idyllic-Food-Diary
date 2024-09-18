package cn.solarmoon.idyllic_food_diary.netwrok

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.feature.optinal_recipe.RecipeSelectData
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.spark_core.api.network.CommonNetData
import cn.solarmoon.spark_core.api.network.ICommonNetHandler
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.MenuProvider
import net.neoforged.neoforge.network.handling.IPayloadContext

class ServerNetHandler: ICommonNetHandler {
    override fun handle(payload: CommonNetData, context: IPayloadContext) {
        val player = context.player() as ServerPlayer
        when (payload.message) {
            RECIPE_SELECT -> {
                player.mainHandItem.update(IFDDataComponents.RECIPE_SELECTION, RecipeSelectData.EMPTY) { it.putIndexAndCopy(payload.block, payload.intValue) }
            }
            KETTLE_SELECT -> {
                player.mainHandItem.update(IFDDataComponents.FLUID_INTERACT_VALUE, 1000) { payload.intValue }
                player.sendSystemMessage(IdyllicFoodDiary.TRANSLATOR.set("message", "dumping_volume", payload.intValue), true)
            }
        }
    }

    companion object {
        const val RECIPE_SELECT = "SelectRecipe"
        const val KETTLE_SELECT = "SelectKettleValue"
    }
}