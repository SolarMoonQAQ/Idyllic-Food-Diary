package cn.solarmoon.idyllic_food_diary.compat.jei;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.compat.jei.category.*;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.*;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMItems;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.core.data.tags.IMItemTags;
import cn.solarmoon.solarmoon_core.api.compat.jei.BaseJEI;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

@JeiPlugin
public class JEI extends BaseJEI {

    @Override
    public void register() {
        add(
                builder()
                        .boundCategory(new CleaverCategory(guiHelper))
                        .recipeType(IMRecipes.CHOPPING.get())
                        .addRecipeCatalyst(IMItems.CHINESE_CLEAVER.get())
                        .emptyBackground(117, 57)
                        .title(IdyllicFoodDiary.TRANSLATOR.set("jei", "cleaver.title"))
                        .build("cleaver", ChoppingRecipe.class),
                builder()
                        .recipeType(RecipeTypes.CAMPFIRE_COOKING)
                        .addRecipeCatalyst(IMItems.GRILL.get()),
                builder()
                        .boundCategory(new SteamerCategory(guiHelper))
                        .recipeType(IMRecipes.STEAMING.get())
                        .addRecipeCatalyst(IMItems.STEAMER.get())
                        .emptyBackground(117, 57)
                        .title(IdyllicFoodDiary.TRANSLATOR.set("jei", "steamer.title"))
                        .build("steamer", SteamingRecipe.class),
                builder()
                        .boundCategory(new KettleCategory(guiHelper))
                        .recipeType(IMRecipes.WATER_BOILING.get())
                        .addRecipeCatalyst(IMItems.KETTLE.get())
                        .addRecipeCatalyst(IMItems.COOKING_POT.get())
                        .addRecipeCatalyst(IMItems.STEAMER_BASE.get())
                        .emptyBackground(117, 57)
                        .title(IdyllicFoodDiary.TRANSLATOR.set("jei", "kettle.title"))
                        .build("kettle", WaterBoilingRecipe.class),
                builder()
                        .boundCategory(new CookingPotCategory(guiHelper))
                        .recipeType(IMRecipes.STEW.get())
                        .addRecipeCatalyst(IMItems.COOKING_POT.get())
                        .emptyBackground(161, 95)
                        .title(IdyllicFoodDiary.TRANSLATOR.set("jei", "cooking_pot.title"))
                        .build("cooking_pot", StewRecipe.class),
                builder()
                        .boundCategory(new SoupServingCategory(guiHelper))
                        .recipeType(IMRecipes.SOUP_SERVING.get())
                        .addRecipeCatalyst(Ingredient.of(IMItemTags.SOUP_CONTAINER))
                        .icon(Items.BOWL)
                        .emptyBackground(117, 37)
                        .title(IdyllicFoodDiary.TRANSLATOR.set("jei", "soup_serving.title"))
                        .build("soup_serving", SoupServingRecipe.class),
                builder()
                        .boundCategory(new CupCategory(guiHelper))
                        .recipeType(IMRecipes.BREWING.get())
                        .addRecipeCatalyst(Ingredient.of(IMItemTags.CUP))
                        .emptyBackground(90, 41)
                        .title(IdyllicFoodDiary.TRANSLATOR.set("jei", "cup.title"))
                        .build("cup", BrewingRecipe.class),
                builder()
                        .boundCategory(new RollingCategory(guiHelper))
                        .recipeType(IMRecipes.ROLLING.get())
                        .addRecipeCatalyst(IMItems.ROLLING_PIN.get())
                        .emptyBackground(70, 19)
                        .title(IdyllicFoodDiary.TRANSLATOR.set("jei", "rolling.title"))
                        .build("rolling", RollingRecipe.class)
        );
    }

    @Override
    public String getModId() {
        return IdyllicFoodDiary.MOD_ID;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(getModId());
    }
}
