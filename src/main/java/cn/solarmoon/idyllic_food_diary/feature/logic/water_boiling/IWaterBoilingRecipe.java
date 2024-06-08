package cn.solarmoon.idyllic_food_diary.feature.logic.water_boiling;

import cn.solarmoon.idyllic_food_diary.data.IMFluidTags;
import cn.solarmoon.idyllic_food_diary.feature.logic.basic_feature.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.feature.logic.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

/**
 * 直接接入就能实现水壶配方
 */
public interface IWaterBoilingRecipe extends ITankBlockEntity, IHeatable {

    default BlockEntity self() {
        return (BlockEntity) this;
    }

    default boolean tryBoilWater() {
        Optional<WaterBoilingRecipe> kettleRecipeOp = getCheckedKettleRecipe();
        Level level = self().getLevel();
        if (level == null) return false;
        if (kettleRecipeOp.isPresent()) {
            WaterBoilingRecipe kettleRecipe = kettleRecipeOp.get();
            setBoilRecipeTime(kettleRecipe.getActualTime(self()));
            setBoilTime(getBoilTime() + 1);
            if (getBoilTime() > kettleRecipe.getActualTime(self())) {
                FluidStack fluidStack = new FluidStack(kettleRecipe.output(), getTank().getFluidAmount());
                getTank().setFluid(fluidStack);
                setBoilTime(0);
                self().setChanged();
            }
            return true;
        } else {
            setBoilTime(0);
            setBoilRecipeTime(0);
            return false;
        }
    }

    /**
     * 遍历所有配方检测液体是否匹配input且下方为热源<br/>
     * 返回匹配的配方
     */
    default Optional<WaterBoilingRecipe> getCheckedKettleRecipe() {
        Level level = self().getLevel();
        if (level == null) return Optional.empty();
        List<WaterBoilingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.WATER_BOILING.get());
        return recipes.stream().filter(recipe -> {
            FluidStack fluidStackIn = getTank().getFluid();
            return fluidStackIn.getFluid().equals(recipe.input()) && isHeatingConsiderStove();
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
