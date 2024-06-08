package cn.solarmoon.idyllic_food_diary.compat.jei;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.logic.water_boiling.WaterBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.util.namespace.ResList;
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

public class KettleCategory extends BaseJEICategory<WaterBoilingRecipe> {

    protected final IDrawableStatic heatSourceDeco;
    protected final IDrawableStatic back;

    public KettleCategory(IGuiHelper helper) {
        super(helper);
        heatSourceDeco = helper.createDrawable(ResList.JEI_HEAT_SOURCE_DECO, 0, 0, 20, 16);
        back = helper.createDrawable(ResList.JEI_KETTLE, 0, 0, 34, 30);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WaterBoilingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 20).addFluidStack(recipe.input(), 1000);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 20).addFluidStack(recipe.output(), 1000);
    }

    @Override
    public void draw(WaterBoilingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        EMPTY_ARROW.draw(guiGraphics, 48, 21);
        ANIMATED_ARROW.draw(guiGraphics, 48, 21);
        helper.getSlotDrawable().draw(guiGraphics, 84, 19);
        heatSourceDeco.draw(guiGraphics, 14, 41);
        back.draw(guiGraphics, 1, 10);
    }

    @Override
    public List<Component> getTooltipStrings(WaterBoilingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> components = new ArrayList<>();
        if (mouseX >= 48 && mouseX <= 70 && mouseY >= 21 && mouseY <= 37) {
            components.add(IdyllicFoodDiary.TRANSLATOR.set("jei", "kettle_time", recipe.time() / 20));
        }
        return components;
    }

}
