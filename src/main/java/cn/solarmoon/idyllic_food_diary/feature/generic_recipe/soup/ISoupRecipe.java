package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.soup;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IExpGiver;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp;
import cn.solarmoon.idyllic_food_diary.feature.spice.Flavor;
import cn.solarmoon.idyllic_food_diary.feature.spice.ISpiceable;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.recipe.ProportionalIngredient;
import cn.solarmoon.solarmoon_core.api.tile.fluid.FluidHandlerUtil;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;
import java.util.stream.Collectors;

public interface ISoupRecipe extends ITankTile, ISpiceable, IExpGiver, IHeatable {

    String SIMMER_TIME = "SimmerTime";
    String SIMMER_RECIPE_TIME = "SimmerRecipeTime";

    default BlockEntity sim() {
        return (BlockEntity) this;
    }

    default boolean trySimmer() {
        Optional<SoupRecipe> recipeOp = findSimmerRecipe();
        int time = getSimmerTime();
        Level level = sim().getLevel();
        if (recipeOp.isPresent() && level != null) {
            SoupRecipe recipe = recipeOp.get();
            // 按输入比例增加产物比例和所需时间比例
            int scale = ProportionalIngredient.findMatch(ItemHandlerUtil.getStacks(getInventory()), recipe.ingredients()).getSecond();
            if (withTrueSpices(recipe.withSpices(), true)) { // 虽然配方匹配了，但是调料不足所需也不会开始，但是可以继续加调料
                time++;
                setSimmerRecipeTime(recipe.time() * scale);
                if (time >= getSimmerRecipeTime()) {
                    FluidStack result = recipe.outputFluid().copy();
                    result.setAmount(result.getAmount() * scale);
                    Temp.set(result, Temp.get(getTank().getFluid()));
                    getTank().setFluid(result);
                    setExp(recipe.exp());
                    ItemHandlerUtil.clearInv(getInventory(), sim());
                    setSimmerTime(0);
                    sim().setChanged();
                }
                setSimmerTime(time);
                return true;
            }
        } else {
            setSimmerTime(0);
            setSimmerRecipeTime(0);
        }
        return false;
    }

    default boolean isSimmering() {
        return getSimmerTime() > 0;
    }

    /**
     * 获取首个匹配的配方
     */
    default Optional<SoupRecipe> findSimmerRecipe() {
        Level level = sim().getLevel();
        if (level == null) return Optional.empty();
        List<SoupRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.SOUP.get());
        return recipes.stream().filter(recipe -> {
            /*
             * 要求：
             * 输入物按比例完全匹配
             * 输入液体及量及温度完全匹配
             * 下方为热源
             */
            List<ItemStack> stacks = ItemHandlerUtil.getStacks(getInventory());
            Pair<Boolean, Integer> match = ProportionalIngredient.findMatch(stacks, recipe.ingredients());
            FluidStack fluidStack = getTank().getFluid();
            int scale = match.getSecond(); // 放大所需液体比例
            FluidStack fluidNeed = recipe.inputFluid().copy();
            fluidNeed.setAmount(fluidNeed.getAmount() * scale);
            return match.getFirst()
                    && FluidHandlerUtil.isMatch(fluidStack, fluidNeed, true, false)
                    && Temp.isSame(fluidStack, recipe.temp())
                    && isOnHeatSource();
        }).findFirst();
    }

    int getSimmerTime();

    void setSimmerTime(int time);

    int getSimmerRecipeTime();

    void setSimmerRecipeTime(int time);
    
}
