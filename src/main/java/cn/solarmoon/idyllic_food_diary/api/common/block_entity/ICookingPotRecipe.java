package cn.solarmoon.idyllic_food_diary.api.common.block_entity;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.util.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.CookingPotRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

/**
 * 接入该接口后立刻实现煮锅配方，但要注意接入后无法再调用新的带有ITimeRecipeBlockEntity的配方
 */
public interface ICookingPotRecipe extends ITimeRecipeBlockEntity<CookingPotRecipe>, IContainerBlockEntity,
        ITankBlockEntity, IPendingResult, ISpiceable {

    default BlockEntity cookingPot() {
        return (BlockEntity) this;
    }

    default boolean tryCook() {
        Optional<CookingPotRecipe> recipeOp = getCheckedRecipe();
        int time = getTime();
        if (recipeOp.isPresent() && !hasResult()) {
            CookingPotRecipe recipe = recipeOp.get();
            if (withTrueSpices(recipe.withSpices(), true)) { // 虽然配方匹配了，但是调料不足所需也不会开始，但是可以继续加调料
                time++;
                setRecipeTime(recipe.time());
                if (time >= recipe.time()) {
                    setFluid(recipe.outputFluid().copy());
                    //输出物
                    if (!recipe.result().isEmpty()) {
                        setPending(recipe.result().copy(), recipe.container());
                        addSpicesToItem(getResult(), true);
                    }
                    clear();
                    setTime(0);
                    cookingPot().setChanged();
                }
                setTime(time);
                return true;
            }
        } else {
            setTime(0);
            setRecipeTime(0);
        }
        return false;
    }

    default boolean isCooking() {
        return getTime() > 0;
    }

    /**
     * @return 液体是否正被加热（是否有液体且下方是否为热源）
     */
    default boolean isHeatingFluid() {
        Level level = cookingPot().getLevel();
        BlockPos pos = cookingPot().getBlockPos();
        return level != null && !getTank().isEmpty() && FarmerUtil.isHeatSource(level.getBlockState(pos.below()));
    }

    /**
     * 获取首个匹配的配方
     */
    @Override
    default Optional<CookingPotRecipe> getCheckedRecipe() {
        Level level = cookingPot().getLevel();
        BlockPos pos = cookingPot().getBlockPos();
        if (level == null) return Optional.empty();
        List<CookingPotRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.COOKING_POT.get());
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
                    return FarmerUtil.isHeatSource(level.getBlockState(pos.below()));
                }
            }
            return false;
        }).findFirst();
    }

}
