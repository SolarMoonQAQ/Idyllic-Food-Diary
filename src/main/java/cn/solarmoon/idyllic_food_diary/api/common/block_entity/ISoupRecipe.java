package cn.solarmoon.idyllic_food_diary.api.common.block_entity;

import cn.solarmoon.idyllic_food_diary.core.common.recipe.SoupRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public interface ISoupRecipe extends IContainerBlockEntity, ITankBlockEntity, ISpiceable, IExpGiver, IHeatable {

    default BlockEntity sim() {
        return (BlockEntity) this;
    }

    default boolean trySimmer() {
        Optional<SoupRecipe> recipeOp = findSimmerRecipe();
        int time = getSimmerTime();
        if (recipeOp.isPresent()) {
            SoupRecipe recipe = recipeOp.get();
            if (withTrueSpices(recipe.withSpices(), true)) { // 虽然配方匹配了，但是调料不足所需也不会开始，但是可以继续加调料
                time++;
                setSimmerRecipeTime(recipe.time());
                if (time >= recipe.time()) {
                    setFluid(recipe.outputFluid());
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
             * 输入液体及量完全匹配
             * 下方为热源
             */
            List<ItemStack> stacks = getStacks();
            if (RecipeMatcher.findMatches(stacks, recipe.ingredients()) != null) {
                FluidStack ctStack = getTank().getFluid();
                if (FluidUtil.isMatch(ctStack, recipe.inputFluid(), true, false)) {
                    return isHeatingConsiderStove();
                }
            }
            return false;
        }).findFirst();
    }

    int getSimmerTime();

    void setSimmerTime(int time);

    int getSimmerRecipeTime();

    void setSimmerRecipeTime(int time);
    
}
