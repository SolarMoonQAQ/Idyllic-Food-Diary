package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.water_boiling;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

/**
 * 直接接入就能实现水壶配方
 */
public interface IWaterBoilingRecipe extends ITankTile, IHeatable {

    String BOIL_TICK = "BoilTick";

    default BlockEntity wb() {
        return (BlockEntity) this;
    }

    default boolean tryBoilWater() {
        Optional<WaterBoilingRecipe> kettleRecipeOp = getCheckedKettleRecipe();
        Level level = wb().getLevel();
        if (level == null) return false;
        if (kettleRecipeOp.isPresent()) {
            WaterBoilingRecipe kettleRecipe = kettleRecipeOp.get();
            setBoilRecipeTime(kettleRecipe.getActualTime(wb()));
            setBoilTime(getBoilTime() + 1);
            if (getBoilTime() > kettleRecipe.getActualTime(wb())) {
                Temp.set(getTank().getFluid(), Temp.HOT);
                setBoilTime(0);
                wb().setChanged();
            }
            return true;
        } else {
            setBoilTime(0);
            setBoilRecipeTime(0);
            return false;
        }
    }

    /**
     * 遍历所有配方检测液体是否匹配input且下方为热源且液体本身不为热水<br/>
     * 返回匹配的配方
     */
    default Optional<WaterBoilingRecipe> getCheckedKettleRecipe() {
        Level level = wb().getLevel();
        if (level == null) return Optional.empty();
        List<WaterBoilingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.WATER_BOILING.get());
        return recipes.stream().filter(recipe -> {
            FluidStack fluidStackIn = getTank().getFluid();
            return fluidStackIn.getFluid().equals(recipe.input())
                    && isOnHeatSource()
                    && !Temp.isHot(fluidStackIn);
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
        return Temp.isHot(getTank().getFluid()) && isOnHeatSource();
    }

    int getBoilRecipeTime();

    int getBoilTime();

    void setBoilRecipeTime(int time);

    void setBoilTime(int time);

}
