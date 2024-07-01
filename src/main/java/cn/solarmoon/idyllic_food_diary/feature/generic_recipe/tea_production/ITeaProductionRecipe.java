package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.tea_production;

import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Optional;

public interface ITeaProductionRecipe extends IContainerTile {

    String TEA_PRODUCTION_TIME = "TeaProductionTime";
    String TEA_PRODUCTION_RECIPE_TIME = "TeaProductionRecipeTime";

    default BlockEntity tp() {
        return (BlockEntity) this;
    }

    default boolean tryProductTea() {
        boolean flag = false;
        for (int i = 0; i < getInventory().getSlots(); i++) {
            if (findTeaPrdRecipe(i).isPresent()) {
                TeaProductionRecipe recipe = findTeaPrdRecipe(i).get();
                getTeaPrdRecipeTimes()[i] = recipe.time();
                getTeaPrdTimes()[i] = getTeaPrdTimes()[i] + 1;
                if (getTeaPrdTimes()[i] > recipe.time()) {
                    getInventory().setStackInSlot(i, recipe.result().copy());
                }
                flag = true;
            } else {
                getTeaPrdRecipeTimes()[i] = 0;
                getTeaPrdTimes()[i] = 0;
            }
        }
        return flag;
    }

    default Optional<TeaProductionRecipe> findTeaPrdRecipe(int i) {
        ItemStack stack = getInventory().getStackInSlot(i);
        Level level = tp().getLevel();
        if (level == null) return Optional.empty();
        List<TeaProductionRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.TEA_PRODUCTION.get());
        return recipes.stream().filter(recipe ->
                recipe.ingredient().test(stack) && recipe.environment().isEnvironmentMatching(this)).findFirst();
    }

    default boolean isInShade() {
        return !isUnderSunshine() && !isInRain();
    }

    default boolean isUnderSunshine() {
        Level level = tp().getLevel();
        BlockPos pos = tp().getBlockPos();
        if (level == null) return false;
        return level.canSeeSky(pos.above()) && !level.isRainingAt(pos) && level.isDay();
    }

    default boolean isInRain() {
        Level level = tp().getLevel();
        BlockPos pos = tp().getBlockPos();
        if (level == null) return false;
        return level.isRainingAt(pos);
    }

    int[] getTeaPrdTimes();
    int[] getTeaPrdRecipeTimes();
    void setTeaPrdTimes(int[] ints);
    void setTeaPrdRecipeTimes(int[] ints);

}
