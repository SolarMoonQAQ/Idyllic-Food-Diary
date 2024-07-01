package cn.solarmoon.idyllic_food_diary.element.matter.stove.water_storage_stove;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.evaporation.IEvaporationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.water_boiling.IWaterBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTile;
import cn.solarmoon.solarmoon_core.api.tile.fluid.TileTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class WaterStorageStoveBlockEntity extends SyncedBlockEntity implements ITankTile, IEvaporationRecipe, IWaterBoilingRecipe {

    private final TileTank tank;
    private int eTick;
    private int boilTime;
    private int boilRecipeTime;

    public WaterStorageStoveBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.WATER_STORAGE_STOVE.get(), pos, state);
        tank = new TileTank(1000, this);
    }

    /**
     * 鉴于灶台自己烧自己所以这里热源触发条件改了一下
     */
    @Override
    public boolean isOnHeatSource() {
        return IEvaporationRecipe.super.isOnHeatSource() || getBlockState().getValue(ILitBlock.LIT);
    }

    @Override
    public FluidTank getTank() {
        return tank;
    }

    @Override
    public void setEvaporationTick(int tick) {
        this.eTick = tick;
    }

    @Override
    public int getEvaporationTick() {
        return eTick;
    }

    @Override
    public int getBoilRecipeTime() {
        return boilRecipeTime;
    }

    @Override
    public int getBoilTime() {
        return boilTime;
    }

    @Override
    public void setBoilRecipeTime(int time) {
        boilRecipeTime = time;
    }

    @Override
    public void setBoilTime(int time) {
        boilTime = time;
    }

}
