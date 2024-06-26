package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.evaporation;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTile;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IEvaporationRecipe extends ITankTile, IHeatable {

    String DRAIN_TICK = "DrainTick";

    default BlockEntity eva() {
        return (BlockEntity) this;
    }

    /**
     * 用于tick中，连接底部为热源，连接底部为热水就每秒消耗热水<br/>
     */
    default boolean tryDrainHotFluid() {
        if (isEvaporating()) {
            setEvaporationTick(getEvaporationTick() + 1);
            if (getEvaporationTick() >= 20) {
                setEvaporationTick(0);
                getTank().drain(5, IFluidHandler.FluidAction.EXECUTE);
                eva().setChanged();
            }
            return true;
        } else {
            setEvaporationTick(0);
            return false;
        }
    }

    default boolean hasHotWater() {
        return hasHotFluid() && getTank().getFluid().getFluid() == Fluids.WATER;
    }

    default boolean hasHotFluid() {
        return Temp.isHot(getTank().getFluid());
    }

    /**
     * @return 是否正在蒸发液体
     */
    default boolean isEvaporating() {
        return isOnHeatSource() && hasHotFluid();
    }

    /**
     * @return 是否可供给蒸笼工作
     */
    default boolean isValidForSteamer() {
        return isOnHeatSource() && hasHotWater();
    }

    void setEvaporationTick(int tick);

    int getEvaporationTick();

}
