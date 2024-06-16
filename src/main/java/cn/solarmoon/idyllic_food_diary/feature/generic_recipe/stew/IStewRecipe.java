package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stew;

import cn.solarmoon.idyllic_food_diary.compat.farmersdelight.FarmersUtil;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IPendingResult;
import cn.solarmoon.idyllic_food_diary.feature.spice.ISpiceable;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.blockentity_util.IContainerBE;
import cn.solarmoon.solarmoon_core.api.blockentity_util.ITankBE;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 接入该接口后立刻实现煮锅配方，但要注意接入后无法再调用新的带有ITimeRecipeBlockEntity的配方
 */
public interface IStewRecipe extends IContainerBE,
        ITankBE, IPendingResult, ISpiceable, IHeatable {

    String STEW_TIME = "StewTime";
    String STEW_RECIPE_TIME = "StewRecipeTime";

    default BlockEntity cookingPot() {
        return (BlockEntity) this;
    }

    default boolean tryStew() {
        Optional<StewRecipe> recipeOp = findStewRecipe();
        int time = getStewTime();
        if (recipeOp.isPresent() && !hasResult()) {
            StewRecipe recipe = recipeOp.get();
            if (withTrueSpices(recipe.withSpices(), true)) { // 虽然配方匹配了，但是调料不足所需也不会开始，但是可以继续加调料
                time++;
                setStewRecipeTime(recipe.time());
                if (time >= recipe.time()) {
                    //输出物
                    if (!recipe.result().isEmpty()) {
                        setPending(recipe.result().copy(), recipe.container());
                        addSpicesToItem(getResult(), true);
                    }
                    clearTank();
                    setExp(recipe.exp());
                    clearInv();
                    setStewTime(0);
                    cookingPot().setChanged();
                }
                setStewTime(time);
            }
            return true;
        } else {
            setStewTime(0);
            setStewRecipeTime(0);
        }
        return false;
    }

    default boolean isStewing() {
        return getStewTime() > 0;
    }

    /**
     * 获取首个匹配的配方
     */
    default Optional<StewRecipe> findStewRecipe() {
        Level level = cookingPot().getLevel();
        if (level == null) return Optional.empty();
        List<StewRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.STEW.get());
        List<StewRecipe> combination = new ArrayList<>(recipes);
        FarmersUtil.addAllRecipeToIFDCookingPotRecipe(combination, level); // 农夫乐事兼容
        return combination.stream().filter(recipe -> {
            /*
             * 要求：
             * 输入物完全匹配
             * 输入液体及量完全匹配
             * 下方为热源
             */
            List<ItemStack> stacks = getStacks();
            if (RecipeMatcher.findMatches(stacks, recipe.ingredients()) != null) {
                FluidStack ctStack = getTank().getFluid();
                if (FluidUtil.isMatch(ctStack, recipe.inputFluid(), true, false)) {
                    return isOnHeatSource();
                }
            }
            return false;
        }).findFirst();
    }

    int getStewTime();

    void setStewTime(int time);

    int getStewRecipeTime();

    void setStewRecipeTime(int time);

}
