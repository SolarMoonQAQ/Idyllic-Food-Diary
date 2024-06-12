package cn.solarmoon.idyllic_food_diary.compat.jei;

import cn.solarmoon.idyllic_food_diary.feature.logic.generic_recipe.chopping.ChoppingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.common.recipe.serializable.ChanceResult;
import cn.solarmoon.solarmoon_core.api.compat.jei.category.BaseJEICategory;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.text.DecimalFormat;

public class CleaverCategory extends BaseJEICategory<ChoppingRecipe> {

    private final int gridX = 76;
    private final int gridY = 10;

    public CleaverCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChoppingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 20).addIngredients(recipe.input());

        NonNullList<ChanceResult> recipeOutputs = recipe.chanceResults();

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
                        ChanceResult output = recipeOutputs.get(index);
                        float chance = output.chance();
                        if (chance != 1) {
                            tooltip.add(1, chanceText(chance));
                        }
                    });
        }

    }

    @Override
    public void draw(ChoppingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        NonNullList<ChanceResult> recipeOutputs = recipe.chanceResults();
        PoseStack poseStack = guiGraphics.pose();

        int size = recipe.chanceResults().size();
        int centerX = size > 1 ? 0 : 9;
        int centerY = size > 2 ? 0 : 9;

        for (int i = 0; i < size; i++) {
            int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
            int yOffset = centerY + ((i / 2) * 19);

            if (recipeOutputs.get(i).chance() != 1) {
                DEFAULT_CHANCE_SLOT.draw(guiGraphics, gridX + xOffset, gridY + yOffset);
            } else {
                DEFAULT_SLOT.draw(guiGraphics, gridX + xOffset, gridY + yOffset);
            }
        }

        IDrawable board = getGuiHelper().createDrawableItemStack(new ItemStack(IMItems.CUTTING_BOARD.get()));
        poseStack.pushPose();
        float zoom = 2.5f;
        poseStack.translate(1, 2, 0);
        poseStack.scale(zoom, zoom, 1f);
        board.draw(guiGraphics, 1, 1);
        poseStack.popPose();

        EMPTY_ARROW.draw(guiGraphics, 48, 21);
    }

    /**
     * @return 如果有小数就保留两位小数，没有就不写
     */
    private Component chanceText(float chance) {
        DecimalFormat df = new DecimalFormat("0.##");
        String result = df.format(chance * 100);
        return SolarMoonCore.TRANSLATOR.set("jei", "chance", ChatFormatting.GOLD, result);
    }

}
