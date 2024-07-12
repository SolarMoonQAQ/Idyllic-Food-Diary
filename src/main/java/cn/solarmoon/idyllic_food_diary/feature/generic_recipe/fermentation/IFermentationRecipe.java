package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.fermentation;

import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp;
import cn.solarmoon.idyllic_food_diary.feature.spice.ISpiceable;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.recipe.ProportionalIngredient;
import cn.solarmoon.solarmoon_core.api.tile.fluid.FluidHandlerUtil;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public interface IFermentationRecipe extends IContainerTile, ITankTile, ISpiceable {

    String FERMENT_TIME = "FermentTime";
    String FERMENT_RECIPE_TIME = "FermentRecipeTime";

    default BlockEntity fr() {
        return (BlockEntity) this;
    }

    default boolean tryFerment() {
        Optional<FermentationRecipe> recipeOp = findFermentRecipe();
        int time = getFermentTime();
        Level level = fr().getLevel();
        if (recipeOp.isPresent() && level != null) {
            FermentationRecipe recipe = recipeOp.get();
            // 按输入比例增加产物比例和所需时间比例
            int scale = ProportionalIngredient.findMatch(ItemHandlerUtil.getStacks(getInventory()), recipe.ingredients()).getSecond();
            if (withTrueSpices(recipe.withSpices(), true)) { // 虽然配方匹配了，但是调料不足所需也不会开始，但是可以继续加调料
                time++;
                setFermentRecipeTime(recipe.time() * scale);
                if (time >= getFermentRecipeTime()) {
                    FluidStack result = recipe.outputFluid().copy();
                    result.setAmount(result.getAmount() * scale);
                    Temp.set(result, Temp.get(getTank().getFluid()));
                    getTank().setFluid(result);
                    ItemHandlerUtil.clearInv(getInventory(), fr());
                    setFermentTime(0);
                    fr().setChanged();
                }
                setFermentTime(time);
                return true;
            }
        } else {
            setFermentTime(0);
            setFermentRecipeTime(0);
        }
        return false;
    }

    default boolean isFermenting() {
        return getFermentTime() > 0;
    }

    /**
     * 获取首个匹配的配方
     */
    default Optional<FermentationRecipe> findFermentRecipe() {
        Level level = fr().getLevel();
        if (level == null) return Optional.empty();
        List<FermentationRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.FERMENTATION.get());
        return recipes.stream().filter(recipe -> {
            /*
             * 要求：
             * 输入物按比例完全匹配
             * 输入液体及量及温度完全匹配
             */
            List<ItemStack> stacks = ItemHandlerUtil.getStacks(getInventory());
            Pair<Boolean, Integer> match = ProportionalIngredient.findMatch(stacks, recipe.ingredients());
            FluidStack fluidStack = getTank().getFluid();
            int scale = match.getSecond(); // 放大所需液体比例
            FluidStack fluidNeed = recipe.inputFluid().copy();
            fluidNeed.setAmount(fluidNeed.getAmount() * scale);
            return match.getFirst()
                    && FluidHandlerUtil.isMatch(fluidStack, fluidNeed, true, false)
                    && Temp.isSame(fluidStack, recipe.temp());
        }).findFirst();
    }

    int getFermentTime();

    void setFermentTime(int time);

    int getFermentRecipeTime();

    void setFermentRecipeTime(int time);

}
