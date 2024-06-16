package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.food_boiling;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.blockentity_util.IContainerBE;
import cn.solarmoon.solarmoon_core.api.blockentity_util.IIndividualTimeRecipeBE;
import cn.solarmoon.solarmoon_core.api.blockentity_util.ITankBE;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Optional;

public interface IFoodBoilingRecipe extends IContainerBE, ITankBE,
        IIndividualTimeRecipeBE<FoodBoilingRecipe>, IHeatable {

    default BlockEntity fb() {
        return (BlockEntity) this;
    }

    default boolean tryBoilFood() {
        for (int i = 0; i < getInventory().getSlots(); i++) {
            if (getCheckedRecipe(i).isPresent()) {
                FoodBoilingRecipe recipe = getCheckedRecipe(i).get();
                getTimes()[i] = getTimes()[i] + 1;
                getRecipeTimes()[i] = recipe.time();
                if (getTimes()[i] >= recipe.time()) {
                    getInventory().setStackInSlot(i, recipe.result().copy());
                    getTank().getFluid().shrink(recipe.fluidConsumption().getAmount());
                    fb().setChanged();
                }
                return true;
            } else {
                getTimes()[i] = 0;
                getRecipeTimes()[i] = 0;
            }
        }
        return false;
    }

    @Override
    default Optional<FoodBoilingRecipe> getCheckedRecipe(int index) {
        Level level = fb().getLevel();
        if (level == null) return Optional.empty();
        List<FoodBoilingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.FOOD_BOILING.get());
        ItemStack stack = getInventory().getStackInSlot(index);
        for (var recipe : recipes) {
            if (recipe.ingredient().test(stack)
                    && getTank().getFluid().getFluid() == recipe.fluidConsumption().getFluid()
                    && getTank().getFluid().getAmount() >= recipe.fluidConsumption().getAmount()
                    && isOnHeatSource()
            ) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

}
