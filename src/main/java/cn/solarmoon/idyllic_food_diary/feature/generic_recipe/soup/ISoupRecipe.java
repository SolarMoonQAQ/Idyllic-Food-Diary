package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.soup;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IExpGiver;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.feature.spice.ISpiceable;
import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.Temp;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.blockentity_util.IContainerBE;
import cn.solarmoon.solarmoon_core.api.blockentity_util.ITankBE;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public interface ISoupRecipe extends IContainerBE, ITankBE, ISpiceable, IExpGiver, IHeatable {

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
            if (withTrueSpices(recipe.withSpices(), true)) { // 虽然配方匹配了，但是调料不足所需也不会开始，但是可以继续加调料
                time++;
                setSimmerRecipeTime(recipe.time());
                if (time >= recipe.time()) {
                    FluidStack result = recipe.outputFluid().copy();
                    Temp.setFluidTemp(result, Temp.getOrCreateFluidTemp(getTank().getFluid(), level), level);
                    setFluid(result);
                    setExp(recipe.exp());
                    clearInv();
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
             * 输入物完全匹配
             * 输入液体及量及温度完全匹配
             * 下方为热源
             */
            List<ItemStack> stacks = getStacks();
            FluidStack fluidStack = getTank().getFluid();
            return RecipeMatcher.findMatches(stacks, recipe.ingredients()) != null
                    && FluidUtil.isMatch(fluidStack, recipe.inputFluid(), true, false)
                    && Temp.getOrCreateFluidTemp(fluidStack, level).isSame(recipe.tempScale())
                    && isOnHeatSource();
        }).findFirst();
    }

    int getSimmerTime();

    void setSimmerTime(int time);

    int getSimmerRecipeTime();

    void setSimmerRecipeTime(int time);
    
}
