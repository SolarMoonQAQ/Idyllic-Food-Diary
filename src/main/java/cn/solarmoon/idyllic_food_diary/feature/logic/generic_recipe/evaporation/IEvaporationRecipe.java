package cn.solarmoon.idyllic_food_diary.feature.logic.generic_recipe.evaporation;

import cn.solarmoon.idyllic_food_diary.data.IMFluidTags;
import cn.solarmoon.idyllic_food_diary.feature.logic.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.registry.common.IMFluids;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IEvaporationRecipe extends ITankBlockEntity, IHeatable {

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
        return getTank().getFluid().getFluid().getFluidType().equals(IMFluids.HOT_WATER.getType());
    }

    default boolean hasHotFluid() {
        return getTank().getFluid().getFluid().is(IMFluidTags.HOT_FLUID);
    }

    /**
     * @return 是否正在蒸发液体
     */
    default boolean isEvaporating() {
        return isHeatingConsiderStove() && hasHotFluid();
    }

    /**
     * @return 是否可供给蒸笼工作
     */
    default boolean isValidForSteamer() {
        return isHeatingConsiderStove() && hasHotWater();
    }

    void setEvaporationTick(int tick);

    int getEvaporationTick();

    /**
     * @return 是否能被蒸笼直接识别并开始蒸东西（像炉灶嵌进去的那样就属于间接）
     */
    boolean isDirectEnabled();

}
