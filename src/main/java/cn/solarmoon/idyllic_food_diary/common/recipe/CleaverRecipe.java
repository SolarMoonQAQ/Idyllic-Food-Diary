package cn.solarmoon.idyllic_food_diary.common.recipe;

import cn.solarmoon.idyllic_food_diary.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.recipe.serializable.ChanceResult;
import cn.solarmoon.solarmoon_core.api.registry.object.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CleaverRecipe(
        ResourceLocation id,
        Ingredient input,
        NonNullList<ChanceResult> chanceResults
) implements IConcreteRecipe {

    public Ingredient getInput() {
        return this.input;
    }

    public List<ItemStack> getResults() {
        return RecipeUtil.getResults(chanceResults);
    }

    public List<ItemStack> getRolledResults(Player player) {
        return RecipeUtil.getRolledResults(player, chanceResults);
    }

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.CLEAVER;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

}
