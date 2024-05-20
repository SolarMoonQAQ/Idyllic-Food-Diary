package cn.solarmoon.idyllic_food_diary.core.common.registry;


import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.*;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.serializer.*;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;

public class IMRecipes {
    public static void register() {}

    //擀面杖
    public static final RecipeEntry<RollingRecipe> ROLLING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("rolling")
            .serializer(RollingRecipeSerializer::new)
            .build();

    //水壶
    public static final RecipeEntry<KettleRecipe> KETTLE = IdyllicFoodDiary.REGISTRY.recipe()
            .id("kettle")
            .serializer(KettleRecipeSerializer::new)
            .build();

    //水杯
    public static final RecipeEntry<CupRecipe> CUP = IdyllicFoodDiary.REGISTRY.recipe()
            .id("cup")
            .serializer(CupRecipeSerializer::new)
            .build();

    //菜刀
    public static final RecipeEntry<CleaverRecipe> CLEAVER = IdyllicFoodDiary.REGISTRY.recipe()
            .id("cleaver")
            .serializer(CleaverRecipeSerializer::new)
            .build();

    //汤锅
    public static final RecipeEntry<CookingPotRecipe> COOKING_POT = IdyllicFoodDiary.REGISTRY.recipe()
            .id("cooking_pot")
            .serializer(CookingPotRecipeSerializer::new)
            .build();

    //蒸笼
    public static final RecipeEntry<SteamerRecipe> STEAMER = IdyllicFoodDiary.REGISTRY.recipe()
            .id("steamer")
            .serializer(SteamerRecipeSerializer::new)
            .build();

    //盛汤
    public static final RecipeEntry<SoupServingRecipe> SOUP_SERVING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("soup_serving")
            .serializer(SoupServingRecipeSerializer::new)
            .build();

    //食材处理
    public static final RecipeEntry<IngredientHandlingRecipe> INGREDIENT_HANDLING = IdyllicFoodDiary.REGISTRY.recipe()
            .id("ingredient_handling")
            .serializer(IngredientHandlingRecipeSerializer::new)
            .build();

}
