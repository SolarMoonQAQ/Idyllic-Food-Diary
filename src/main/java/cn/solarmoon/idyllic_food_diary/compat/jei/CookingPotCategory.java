package cn.solarmoon.idyllic_food_diary.compat.jei;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stew.StewRecipe;
import cn.solarmoon.solarmoon_core.api.compat.jei.BaseJEICategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class CookingPotCategory extends BaseJEICategory<StewRecipe> {

    private final int x1 = 7, y1 = 3;
    private final int x2 = 99, y2 = 3;
    private final int backToHeatY = 57;

    protected final IDrawableStatic back;
    protected final IDrawableStatic heatSourceDeco;

    public CookingPotCategory(IGuiHelper helper) {
        super(helper);
        back = helper.createDrawable(ResList.JEI_COOKING_POT, 0, 0, 28, 20);
        heatSourceDeco = helper.createDrawable(ResList.JEI_HEAT_SOURCE_DECO, 0, 0, 20, 16);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, StewRecipe recipe, IFocusGroup focuses) {
        for (int i = 0; i < 9; i++) {
            int row = i / 3;
            int column = (i + 3) % 3;
            if (i < recipe.ingredients().size()) {
                builder.addSlot(RecipeIngredientRole.INPUT, x1 + 1 + column * 18, y1 + 1 + row * 18).addIngredients(recipe.ingredients().get(i));
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, x2 + 1 + column * 18, y2 + 1 + row * 18).addItemStack(recipe.result());

        }
        builder.addSlot(RecipeIngredientRole.INPUT, x1 + 1 + 18, backToHeatY)
                .addFluidStack(recipe.inputFluid().getFluid(), recipe.inputFluid().getAmount())
                .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(IdyllicFoodDiary.TRANSLATOR.set("jei", "fluid_amount", recipe.inputFluid().getAmount())));
    }

    @Override
    public void draw(StewRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        for (int i = 0; i < 9; i++) {
            int row = i / 3;
            int column = (i + 3) % 3;
            helper.getSlotDrawable().draw(guiGraphics, x1 + column * 18, y1 + row * 18);
            DEFAULT_SLOT.draw(guiGraphics, x2 + column * 18, y2 + row * 18);
        }
        back.draw(guiGraphics, x1 + 1 + 12, backToHeatY);
        heatSourceDeco.draw(guiGraphics, x1 + 1 + 16, backToHeatY + 21);
        helper.getSlotDrawable().draw(guiGraphics, x2 + 18, backToHeatY - 1);
        EMPTY_ARROW.draw(guiGraphics, 69, 57);
        ANIMATED_ARROW.draw(guiGraphics, 69, 57);
    }

    @Override
    public List<Component> getTooltipStrings(StewRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> components = new ArrayList<>();
        if (mouseX >= 69 && mouseX <= 91 && mouseY >= 58 && mouseY <= 74) {
            components.add(IdyllicFoodDiary.TRANSLATOR.set("jei", "time", recipe.time() / 20));
        }
        return components;
    }

}
