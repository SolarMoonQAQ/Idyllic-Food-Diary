package cn.solarmoon.idyllic_food_diary.registry.common;


import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.logic.chopping.ChoppingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.food_boiling.FoodBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.ingredient_handling.IngredientHandlingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.rolling.RollingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.soup.SoupRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.soup_serving.SoupServingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.steaming.SteamingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.stew.StewRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.stir_fry.StirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.water_boiling.WaterBoilingRecipe;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;

public class IMRecipes {
    public static void register() {}

    //擀面杖
    public static final RecipeEntry<StirFryRecipe> STIR_FRY = IdyllicFoodDiary.REGISTRY.recipe()
            .id("stir_fry")
            .serializer(StirFryRecipe.Serializer::new)
            .build();


    //擀面杖
    public static final RecipeEntry<RollingRecipe> ROLLING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("rolling")
            .serializer(RollingRecipe.Serializer::new)
            .build();

    //菜刀
    public static final RecipeEntry<ChoppingRecipe> CHOPPING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("chopping")
            .serializer(ChoppingRecipe.Serializer::new)
            .build();

    //烧水
    public static final RecipeEntry<WaterBoilingRecipe> WATER_BOILING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("water_boiling")
            .serializer(WaterBoilingRecipe.Serializer::new)
            .build();

    //煮
    public static final RecipeEntry<FoodBoilingRecipe> FOOD_BOILING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("food_boiling")
            .serializer(FoodBoilingRecipe.Serializer::new)
            .build();

    //熬汤
    public static final RecipeEntry<SoupRecipe> SOUP = IdyllicFoodDiary.REGISTRY.recipe()
            .id("soup")
            .serializer(SoupRecipe.Serializer::new)
            .build();

    //炖
    public static final RecipeEntry<StewRecipe> STEW = IdyllicFoodDiary.REGISTRY.recipe()
            .id("stew")
            .serializer(StewRecipe.Serializer::new)
            .build();

    //炒


    //蒸
    public static final RecipeEntry<SteamingRecipe> STEAMING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("steaming")
            .serializer(SteamingRecipe.Serializer::new)
            .build();

    //盛汤
    public static final RecipeEntry<SoupServingRecipe> SOUP_SERVING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("soup_serving")
            .serializer(SoupServingRecipe.Serializer::new)
            .build();

    //食材处理
    public static final RecipeEntry<IngredientHandlingRecipe> INGREDIENT_HANDLING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("ingredient_handling")
            .serializer(IngredientHandlingRecipe.Serializer::new)
            .build();

}
