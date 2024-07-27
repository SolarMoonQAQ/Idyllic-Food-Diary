package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.evaporation;

import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.IBuiltInStove;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
        if (canEvaporate()) {
            setEvaporationTick(getEvaporationTick() + 1);
            if (getEvaporationTick() >= 20) {
                setEvaporationTick(0);
                getTank().drain(getEvaporatingAmount(), IFluidHandler.FluidAction.EXECUTE);
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

    default boolean canEvaporate() {
        Level level = eva().getLevel();
        BlockPos pos = eva().getBlockPos();
        BlockState state = eva().getBlockState();
        if (state.getBlock() instanceof IBuiltInStove built) {
            // 当是炉灶嵌入模式且上方有盖子，就不蒸水
            if (built.isNestedInStove(state)) {
                if (level != null && level.getBlockState(pos.above()).is(IMBlocks.STOVE_LID.get())) {
                    return false;
                }
                // 当可以镶嵌入炉灶时，只能镶嵌中才能蒸水
                return isOnHeatSource() && hasHotFluid();
            }
            return false;
        }
        return isOnHeatSource() && hasHotFluid();
    }

    /**
     * @return 是否正在蒸发液体
     */
    default boolean isEvaporating() {
        return canEvaporate();
    }

    default int getEvaporatingAmount() {
        return 5;
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
