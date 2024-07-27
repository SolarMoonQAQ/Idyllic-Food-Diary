package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.baking;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.feature.spice.ISpiceable;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.ContainerHelper;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public interface IBakingRecipe extends IContainerTile, IHeatable, ISpiceable {

    String BAKE_TIME = "BakeTime";
    String BAKE_RECIPE_TIME = "BakeRecipeTime";

    default boolean tryBake() {
        if (findBakingRecipe().isPresent()) {
            BakingRecipe recipe = findBakingRecipe().get();
            setBakeRecipeTime(recipe.time());
            setBakeTime(getBakeTime() + 1);
            if (getBakeTime() > recipe.time()) {
                ItemStack result = recipe.result().copy();
                ItemStack container = ContainerHelper.getContainer(getInventory().getStackInSlot(0));
                ContainerHelper.setContainer(result, container);
                addSpicesToItem(new SpiceList(), result, true);
                getInventory().setStackInSlot(0, result);
                setBakeTime(0);
                setBakeRecipeTime(0);
            }
            return true;
        } else {
            setBakeTime(0);
            setBakeRecipeTime(0);
            return false;
        }
    }

    default boolean isBaking() {
        return getBakeTime() > 0;
    }

    default Optional<BakingRecipe> findBakingRecipe() {
        Level level = h().getLevel();
        if (level == null) return Optional.empty();
        List<BakingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.BAKING.get());
        return recipes.stream()
                .filter(recipe -> recipe.ingredient().test(getInventory().getStackInSlot(0)) && isOnHeatSource())
                .findFirst();
    }

    int getBakeTime();
    int getBakeRecipeTime();
    void setBakeTime(int time);
    void setBakeRecipeTime(int time);

}
