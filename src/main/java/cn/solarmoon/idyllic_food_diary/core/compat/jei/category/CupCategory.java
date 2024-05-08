package cn.solarmoon.idyllic_food_diary.core.compat.jei.category;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.CupRecipe;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.ResList;
import cn.solarmoon.solarmoon_core.api.compat.jei.category.BaseJEICategory;
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

public class CupCategory extends BaseJEICategory<CupRecipe> {

    protected final IDrawableStatic time;
    protected final IDrawableStatic back;

    public CupCategory(IGuiHelper helper) {
        super(helper);
        time = helper.createDrawable(ResList.JEI_LITTLE_TIME, 0, 0, 12, 12);
        back = helper.createDrawable(ResList.JEI_CUP, 0, 0, 23, 21);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CupRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 1).addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 20).addFluidStack(recipe.inputFluid().getFluid(), recipe.getInputAmount() * 4L)
                .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(IdyllicFoodDiary.TRANSLATOR.set("jei", "fluid_amount", recipe.getInputAmount())));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 12).addFluidStack(recipe.outputFluid().getFluid(), recipe.getInputAmount() * 4L)
                .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(IdyllicFoodDiary.TRANSLATOR.set("jei", "fluid_amount", recipe.getInputAmount())));
    }

    @Override
    public void draw(CupRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        time.draw(guiGraphics, 41, 14);
        back.draw(guiGraphics, 3, 20);
        DEFAULT_SLOT.draw(guiGraphics, 5, 0);
        helper.getSlotDrawable().draw(guiGraphics, 69, 11);
    }

    @Override
    public List<Component> getTooltipStrings(CupRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> components = new ArrayList<>();
        if (mouseX >= 41 && mouseX <= 52 && mouseY >= 14 && mouseY <= 25) {
            components.add(IdyllicFoodDiary.TRANSLATOR.set("jei", "time", recipe.time() / 20));
        }
        return components;
    }

}
