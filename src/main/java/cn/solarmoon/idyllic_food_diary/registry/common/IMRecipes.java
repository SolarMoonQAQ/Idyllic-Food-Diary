package cn.solarmoon.idyllic_food_diary.registry.common;


import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.baking.BakingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.chopping.ChoppingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.food_boiling.FoodBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.ingredient_handling.IngredientHandlingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.rolling.RollingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.soup.SoupRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.steaming.SteamingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stew.StewRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.StirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.tea_production.TeaProductionRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.water_boiling.WaterBoilingRecipe;
import cn.solarmoon.solarmoon_core.api.entry.common.RecipeEntry;

public class IMRecipes {
    public static void register() {}

    // 烘烤
    public static final RecipeEntry<BakingRecipe> BAKING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("baking")
            .serializer(BakingRecipe.Serializer::new)
            .build();

    // 制茶
    public static final RecipeEntry<TeaProductionRecipe> TEA_PRODUCTION = IdyllicFoodDiary.REGISTRY.recipe()
            .id("tea_production")
            .serializer(TeaProductionRecipe.Serializer::new)
            .build();

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

    //食材处理
    public static final RecipeEntry<IngredientHandlingRecipe> INGREDIENT_HANDLING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("ingredient_handling")
            .serializer(IngredientHandlingRecipe.Serializer::new)
            .build();

}
