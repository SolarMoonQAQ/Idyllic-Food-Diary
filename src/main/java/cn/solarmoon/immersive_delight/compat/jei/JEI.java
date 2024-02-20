package cn.solarmoon.immersive_delight.compat.jei;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.recipe.CleaverRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.compat.jei.category.Cleaver;
import cn.solarmoon.solarmoon_core.compat.jei.BaseJEI;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEI extends BaseJEI {

    @Override
    public void register() {
        add(
                builder()
                        .boundCategory(new Cleaver())
                        .recipeType(IMRecipes.CLEAVER.get())
                        .addRecipeCatalyst(IMItems.CHINESE_CLEAVER.get())
                        .emptyBackground(117, 57)
                        .build("cleaver", CleaverRecipe.class),
                builder()
                        .recipeType(RecipeTypes.CAMPFIRE_COOKING)
                        .addRecipeCatalyst(IMItems.GRILL.get())
        );
    }

    @Override
    public String getModId() {
        return ImmersiveDelight.MOD_ID;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(getModId() + "jei_plugin");
    }

}
