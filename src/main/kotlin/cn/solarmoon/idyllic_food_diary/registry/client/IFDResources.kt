package cn.solarmoon.idyllic_food_diary.registry.client

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import net.minecraft.resources.ResourceLocation

object IFDResources {

    @JvmStatic
    val RECIPE_SELECTION = create("textures/gui/recipe_selection.png")

    private fun create(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(IdyllicFoodDiary.MOD_ID, path)

}