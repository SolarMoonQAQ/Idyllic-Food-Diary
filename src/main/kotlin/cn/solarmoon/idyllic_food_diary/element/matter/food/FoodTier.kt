package cn.solarmoon.idyllic_food_diary.element.matter.food

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary

enum class FoodTier {
    FEAST, GREAT_MEAL, TASTY_MEAL, SIMPLE_MEAL, BLAND_FOOD, NATURAL_FOOD, DISGUSTING_FOOD;

    val displayName = IdyllicFoodDiary.TRANSLATOR.set("tooltip", "tier.${name.lowercase()}")

}