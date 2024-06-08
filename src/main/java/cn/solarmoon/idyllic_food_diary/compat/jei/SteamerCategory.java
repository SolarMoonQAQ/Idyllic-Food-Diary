package cn.solarmoon.idyllic_food_diary.compat.jei;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.logic.steaming.SteamingRecipe;
import cn.solarmoon.idyllic_food_diary.util.namespace.ResList;
import cn.solarmoon.solarmoon_core.api.compat.jei.category.BaseJEICategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class SteamerCategory extends BaseJEICategory<SteamingRecipe> {

    protected final IDrawable back;
    protected final IDrawable deco;

    public SteamerCategory(IGuiHelper helper) {
        super(helper);
        back = helper.createDrawable(ResList.JEI_STEAMER, 0, 0, 26, 26);
        deco = helper.createDrawable(ResList.JEI_STEAMER_DECO, 0, 0, 15, 15);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SteamingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 20).addIngredients(recipe.input());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 20).addItemStack(recipe.output());
    }

    @Override
    public void draw(SteamingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        helper.getSlotDrawable().draw(guiGraphics, 84, 19);
        EMPTY_ARROW.draw(guiGraphics, 48, 21);
        ANIMATED_ARROW.draw(guiGraphics, 48, 21);
        deco.draw(guiGraphics, 52, 5);
        back.draw(guiGraphics, 11, 15);
    }

    @Override
    public List<Component> getTooltipStrings(SteamingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> components = new ArrayList<>();
        if (mouseX >= 48 && mouseX <= 70 && mouseY >= 21 && mouseY <= 37) {
            components.add(IdyllicFoodDiary.TRANSLATOR.set("jei", "time", recipe.time() / 20));
        }
        return components;
    }

}
