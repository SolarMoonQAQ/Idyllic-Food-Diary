package cn.solarmoon.idyllic_food_diary.core.common.recipe;

import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.recipe.serializable.ChanceResult;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public record RollingRecipe(
        ResourceLocation id,
        Ingredient input,
        int time,
        Block output,
        NonNullList<ChanceResult> chanceResults
) implements IConcreteRecipe {

    public List<ItemStack> getResults() {
        return RecipeUtil.getResults(chanceResults);
    }

    public List<ItemStack> getRolledResults(Player player) {
        return RecipeUtil.getRolledResults(player, chanceResults);
    }

    public boolean hasBlockOutput() {
        return output != Blocks.AIR;
    }

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.ROLLING;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

}

