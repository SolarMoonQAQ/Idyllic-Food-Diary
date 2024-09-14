package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.feature.food_container.FoodContainer
import net.minecraft.world.item.ItemStack

object IFDAttachments {

    @JvmStatic
    fun register() {}

    /**
     * 和[IFDDataComponents.FOOD_CONTAINER]基本一致，但这个是用在blockentity上的
     */
    @JvmStatic
    val FOOD_CONTAINER = IdyllicFoodDiary.REGISTER.attachment<FoodContainer>()
        .id("food_container")
        .defaultValue{ FoodContainer.EMPTY }
        .serializer { it.serialize(FoodContainer.CODEC) }
        .build()

}