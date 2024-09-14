package cn.solarmoon.idyllic_food_diary.registry.client

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneBlockRenderer

object IFDLayers {

    @JvmStatic
    fun register() {}

    @JvmStatic
    val MILLSTONE = IdyllicFoodDiary.REGISTER.layer()
        .id("millstone")
        .bound(MillstoneBlockRenderer::createBodyLayer)
        .build()

}