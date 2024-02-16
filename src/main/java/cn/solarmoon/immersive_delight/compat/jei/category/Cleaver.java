package cn.solarmoon.immersive_delight.compat.jei.category;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.recipe.CleaverRecipe;
import cn.solarmoon.solarmoon_core.compat.jei.category.BaseJEICategory;
import cn.solarmoon.solarmoon_core.util.RecipeUtil;
import cn.solarmoon.solarmoon_core.util.namespace.SolarBaseRes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;

public class Cleaver extends BaseJEICategory<CleaverRecipe> {

    private final int gridX = 76;
    private final int gridY = 10;

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CleaverRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 8).addIngredients(recipe.getInput());

        NonNullList<RecipeUtil.ChanceResult> recipeOutputs = recipe.getRollableResults();

        int size = recipeOutputs.size();
        int centerX = size > 1 ? 1 : 10;
        int centerY = size > 2 ? 1 : 10;

        for (int i = 0; i < size; i++) {
            int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
            int yOffset = centerY + ((i / 2) * 19);

            int index = i;
            builder.addSlot(RecipeIngredientRole.OUTPUT, gridX + xOffset, gridY + yOffset)
                    .addItemStack(recipeOutputs.get(i).stack())
                    .addTooltipCallback((slotView, tooltip) -> {
                        RecipeUtil.ChanceResult output = recipeOutputs.get(index);
                        float chance = output.chance();
                        if (chance != 1) {
                            tooltip.add(1, chanceText(chance));
                        }
                    });
        }

    }

    @Override
    public void draw(CleaverRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        NonNullList<RecipeUtil.ChanceResult> recipeOutputs = recipe.getRollableResults();
        IDrawable slot = getGuiHelper().createDrawable(SolarBaseRes.JEI_SLOT, 0, 0, 18, 18);
        IDrawable slotChance = getGuiHelper().createDrawable(SolarBaseRes.JEI_CHANCE_SLOT, 0, 0, 18, 18);

        int size = recipe.getResults().size();
        int centerX = size > 1 ? 0 : 9;
        int centerY = size > 2 ? 0 : 9;

        for (int i = 0; i < size; i++) {
            int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
            int yOffset = centerY + ((i / 2) * 19);

            if (recipeOutputs.get(i).chance() != 1) {
                slotChance.draw(guiGraphics, gridX + xOffset, gridY + yOffset);
            } else {
                slot.draw(guiGraphics, gridX + xOffset, gridY + yOffset);
            }
        }
    }

    /**
     * @return 如果有小数就保留两位小数，没有就不写
     */
    private Component chanceText(float chance) {
        DecimalFormat df = new DecimalFormat("0.##");
        String result = df.format(chance * 100);
        return ImmersiveDelight.TRANSLATOR.set("jei", "chance", ChatFormatting.GOLD, result);
    }

}
