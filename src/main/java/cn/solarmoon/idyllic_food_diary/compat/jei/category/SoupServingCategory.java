package cn.solarmoon.idyllic_food_diary.compat.jei.category;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.SoupServingRecipe;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.ResList;
import cn.solarmoon.solarmoon_core.api.compat.jei.category.BaseJEICategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;

public class SoupServingCategory extends BaseJEICategory<SoupServingRecipe> {

    private final int fluidX = 50;
    private final int allY = 15;
    private final int slotX = 0;
    private final int slotX2 = 100;

    protected final IDrawableStatic back;
    protected final IDrawableStatic pot;

    public SoupServingCategory(IGuiHelper helper) {
        super(helper);
        back = helper.createDrawable(ResList.JEI_SOUP_SERVING, 0, 0, 13, 25);
        pot = helper.createDrawable(ResList.JEI_SOUP_POT, 0, 0, 28, 20);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SoupServingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, slotX, 1).addItemStack(recipe.container());
        builder.addSlot(RecipeIngredientRole.INPUT, fluidX, allY).addFluidStack(recipe.fluidToServe().getFluid(), recipe.getAmountToServe())
                        .addTooltipCallback((recipeSlotView, tooltip) ->
                                tooltip.add(IdyllicFoodDiary.TRANSLATOR.set("jei", "fluid_amount", recipe.getAmountToServe())));
        builder.addSlot(RecipeIngredientRole.OUTPUT, slotX2, allY).addItemStack(recipe.result());
    }

    @Override
    public void draw(SoupServingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        back.draw(guiGraphics, slotX + 25, allY - 3);
        helper.getSlotDrawable().draw(guiGraphics, slotX - 1, 1 - 1);
        DEFAULT_SLOT.draw(guiGraphics, slotX2 - 1, allY - 1);
        EMPTY_ARROW.draw(guiGraphics, fluidX + 24, allY);
        pot.draw(guiGraphics, fluidX - 6, allY);
    }

}
