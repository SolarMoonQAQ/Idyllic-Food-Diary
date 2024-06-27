package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.tea_production;

import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.tile.IIndividualTimeRecipeTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Optional;

public interface ITeaProductionRecipe extends IContainerTile, IIndividualTimeRecipeTile<TeaProductionRecipe> {

    default BlockEntity tp() {
        return (BlockEntity) this;
    }

    default boolean tryProductTea() {
        boolean flag = false;
        for (int i = 0; i < getInventory().getSlots(); i++) {
            if (getCheckedRecipe(i).isPresent()) {
                TeaProductionRecipe recipe = getCheckedRecipe(i).get();
                getRecipeTimes()[i] = recipe.time();
                getTimes()[i] = getTimes()[i] + 1;
                if (getTimes()[i] > recipe.time()) {
                    getInventory().setStackInSlot(i, recipe.result().copy());
                }
                flag = true;
            } else {
                getRecipeTimes()[i] = 0;
                getTimes()[i] = 0;
            }
        }
        return flag;
    }

    @Override
    default Optional<TeaProductionRecipe> getCheckedRecipe(int i) {
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

}
