package cn.solarmoon.idyllic_food_diary.compat.jei;

import cn.solarmoon.idyllic_food_diary.feature.logic.generic_recipe.rolling.RollingRecipe;
import cn.solarmoon.solarmoon_core.api.compat.jei.category.BaseJEICategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class RollingCategory extends BaseJEICategory<RollingRecipe> {

    private final int x1 = 1, y1 = 1;
    private final int x2 = 53, y2 = 1;

    public RollingCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RollingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, x1, y1).addIngredients(recipe.input());
        builder.addSlot(RecipeIngredientRole.OUTPUT, x2, y2).addItemStack(new ItemStack(recipe.output()));
    }

    @Override
    public void draw(RollingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        EMPTY_ARROW.draw(guiGraphics, 25, 2);
        DEFAULT_SLOT.draw(guiGraphics, x1 - 1, y1 - 1);
        DEFAULT_SLOT.draw(guiGraphics, x2 - 1, y2 - 1);
    }

}
