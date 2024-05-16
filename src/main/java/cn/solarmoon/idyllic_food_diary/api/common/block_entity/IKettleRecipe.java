package cn.solarmoon.idyllic_food_diary.api.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.util.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.KettleRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.core.data.tags.IMFluidTags;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

/**
 * 直接接入就能实现水壶配方
 */
public interface IKettleRecipe extends ITankBlockEntity {

    default BlockEntity self() {
        return (BlockEntity) this;
    }

    default void tryBoilWater() {
        Optional<KettleRecipe> kettleRecipeOp = getCheckedKettleRecipe();
        Level level = self().getLevel();
        if (level == null) return;
        if (kettleRecipeOp.isPresent()) {
            KettleRecipe kettleRecipe = kettleRecipeOp.get();
            setBoilRecipeTime(kettleRecipe.getActualTime(self()));
            setBoilTime(getBoilTime() + 1);
            if (getBoilTime() > kettleRecipe.getActualTime(self())) {
                FluidStack fluidStack = new FluidStack(kettleRecipe.output(), getTank().getFluidAmount());
                getTank().setFluid(fluidStack);
                setBoilTime(0);
                self().setChanged();
            }
        } else {
            setBoilTime(0);
            setBoilRecipeTime(0);
        }
    }

    /**
     * 遍历所有配方检测液体是否匹配input且下方为热源<br/>
     * 返回匹配的配方
     */
    default Optional<KettleRecipe> getCheckedKettleRecipe() {
        Level level = self().getLevel();
        if (level == null) return Optional.empty();
        List<KettleRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.KETTLE.get());
        return recipes.stream().filter(recipe -> {
            BlockState state = level.getBlockState(self().getBlockPos().below());
            boolean isHeated = FarmerUtil.isHeatSource(state);
            FluidStack fluidStackIn = getTank().getFluid();
            return fluidStackIn.getFluid().equals(recipe.input()) && isHeated;
        }).findFirst();
    }

    /**
     * @return 是否正在烧水
     */
    default boolean isBoiling() {
        return getBoilTime() > 0;
    }

    /**
     * @return 是否已处于沸腾状态（容器内是热液体且下方为热源）
     */
    default boolean isInBoil() {
        Level level = self().getLevel();
        if (level != null) {
            BlockState stateBelow = level.getBlockState(self().getBlockPos().below());
            return getTank().getFluid().getFluid().is(IMFluidTags.HOT_FLUID) && FarmerUtil.isHeatSource(stateBelow);
        }
        return false;
    }

    int getBoilRecipeTime();

    int getBoilTime();

    void setBoilRecipeTime(int time);

    void setBoilTime(int time);

}
