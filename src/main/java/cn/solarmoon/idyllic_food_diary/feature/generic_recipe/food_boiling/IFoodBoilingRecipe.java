package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.food_boiling;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface IFoodBoilingRecipe extends IContainerTile, ITankTile, IHeatable {

    String FB_TIME = "FoodBoilingTime";
    String FB_RECIPE_TIME = "FoodBoilingRecipeTime";

    default BlockEntity fb() {
        return (BlockEntity) this;
    }

    default boolean tryBoilFood() {
        int consumptionSum = 0;
        Level level = fb().getLevel();
        if (level == null) return false;
        if (level.isClientSide) return false;
        for (int i = 0; i < getInventory().getSlots(); i++) {
            if (findFoodBoilingRecipe(i).isPresent()) {
                FoodBoilingRecipe recipe = findFoodBoilingRecipe(i).get();
                consumptionSum += recipe.fluidConsumption().getAmount(); // 保证同时存在多个配方时液体总量要大于所有配方所需的消耗量
                if (getTank().getFluidAmount() >= consumptionSum) {
                    getFBTimes()[i] = getFBTimes()[i] + 1;
                    getFBRecipeTimes()[i] = recipe.time();
                    if (getFBTimes()[i] >= recipe.time()) {
                        getInventory().setStackInSlot(i, recipe.result().copy());
                        getTank().getFluid().shrink(recipe.fluidConsumption().getAmount());
                        fb().setChanged();
                    }
                } else {
                    getFBTimes()[i] = 0;
                    getFBRecipeTimes()[i] = 0;
                }
            } else {
                getFBTimes()[i] = 0;
                getFBRecipeTimes()[i] = 0;
            }
        }
        return isBoilingFood();
    }

    default boolean isBoilingFood() {
        return Arrays.stream(getFBTimes()).anyMatch(time -> time > 0);
    }

    default Optional<FoodBoilingRecipe> findFoodBoilingRecipe(int index) {
        Level level = fb().getLevel();
        if (level == null) return Optional.empty();
        List<FoodBoilingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.FOOD_BOILING.get());
        ItemStack stack = getInventory().getStackInSlot(index);
        for (var recipe : recipes) {
            if (recipe.ingredient().test(stack)
                    && getTank().getFluid().getFluid() == recipe.fluidConsumption().getFluid()
                    && getTank().getFluid().getAmount() >= recipe.fluidConsumption().getAmount()
                    && Temp.isSame(getTank().getFluid(), recipe.temp())
                    && isOnHeatSource()
            ) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    int[] getFBTimes();
    int[] getFBRecipeTimes();
    void setFBTimes(int[] ints);
    void setFBRecipeTimes(int[] ints);

}
