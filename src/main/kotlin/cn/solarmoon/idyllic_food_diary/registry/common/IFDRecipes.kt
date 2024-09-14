package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.recipe.BakingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.BrewingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.ChoppingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.DryingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.EvaporationRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.FermentationRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.FoodBoilingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.GrindingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.IngredientHandlingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.RollingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.SoupRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.SteamingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.StewRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.StirFryRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.WaterBoilingRecipe
import cn.solarmoon.spark_core.api.recipe.processor.EmptyRecipeType

object IFDRecipes {

    @JvmStatic
    fun register() {}

    @JvmStatic
    val BAKING = IdyllicFoodDiary.REGISTER.recipe<BakingRecipe>()
        .id("baking")
        .serializer { BakingRecipe.Serializer() }
        .build()

    @JvmStatic
    val BREWING = IdyllicFoodDiary.REGISTER.recipe<BrewingRecipe>()
        .id("brewing")
        .serializer { BrewingRecipe.Serializer() }
        .build()

    @JvmStatic
    val CHOPPING = IdyllicFoodDiary.REGISTER.recipe<ChoppingRecipe>()
        .id("chopping")
        .serializer { ChoppingRecipe.Serializer() }
        .build()

    @JvmStatic
    val DRYING = IdyllicFoodDiary.REGISTER.recipe<DryingRecipe>()
        .id("drying")
        .serializer { DryingRecipe.Serializer() }
        .build()

    @JvmStatic
    val FERMENTATION = IdyllicFoodDiary.REGISTER.recipe<FermentationRecipe>()
        .id("fermentation")
        .serializer { FermentationRecipe.Serializer() }
        .build()

    @JvmStatic
    val FOOD_BOILING = IdyllicFoodDiary.REGISTER.recipe<FoodBoilingRecipe>()
        .id("food_boiling")
        .serializer { FoodBoilingRecipe.Serializer() }
        .build()

    @JvmStatic
    val GRINDING = IdyllicFoodDiary.REGISTER.recipe<GrindingRecipe>()
        .id("grinding")
        .serializer { GrindingRecipe.Serializer() }
        .build()

    @JvmStatic
    val INGREDIENT_HANDLING = IdyllicFoodDiary.REGISTER.recipe<IngredientHandlingRecipe>()
        .id("ingredient_handling")
        .serializer { IngredientHandlingRecipe.Serializer() }
        .build()

    @JvmStatic
    val ROLLING = IdyllicFoodDiary.REGISTER.recipe<RollingRecipe>()
        .id("rolling")
        .serializer { RollingRecipe.Serializer() }
        .build()

    @JvmStatic
    val SOUP = IdyllicFoodDiary.REGISTER.recipe<SoupRecipe>()
        .id("soup")
        .serializer { SoupRecipe.Serializer() }
        .build()

    @JvmStatic
    val STEAMING = IdyllicFoodDiary.REGISTER.recipe<SteamingRecipe>()
        .id("steaming")
        .serializer { SteamingRecipe.Serializer() }
        .build()

    @JvmStatic
    val STEW = IdyllicFoodDiary.REGISTER.recipe<StewRecipe>()
        .id("stew")
        .serializer { StewRecipe.Serializer() }
        .build()

    @JvmStatic
    val STIR_FRY = IdyllicFoodDiary.REGISTER.recipe<StirFryRecipe>()
        .id("stir_fry")
        .serializer { StirFryRecipe.Serializer() }
        .build()

    @JvmStatic
    val WATER_BOILING = IdyllicFoodDiary.REGISTER.recipe<WaterBoilingRecipe>()
        .id("water_boiling")
        .serializer { WaterBoilingRecipe.Serializer() }
        .build()

    @JvmStatic
    val EVAPORATION = EmptyRecipeType(EvaporationRecipe.Processor::class.java)

}