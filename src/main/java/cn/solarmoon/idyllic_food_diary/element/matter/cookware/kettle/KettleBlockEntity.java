package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle;

import cn.solarmoon.idyllic_food_diary.feature.logic.tea_brewing.IBrewingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.generic_recipe.water_boiling.IWaterBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class KettleBlockEntity extends BaseTCBlockEntity implements IWaterBoilingRecipe, IBrewingRecipe {

    private int boilTime;
    private int boilRecipeTime;
    private int brewTime;
    private int brewRecipeTime;

    public KettleBlockEntity(int maxCapacity, int size, int slotLimit, BlockPos pos, BlockState state) {
        super(IMBlockEntities.KETTLE.get(), maxCapacity, size, slotLimit, pos, state);
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

    @Override
    public void setBrewTime(int time) {
        brewTime = time;
    }

    @Override
    public void setBrewRecipeTime(int time) {
        brewRecipeTime = time;
    }

    @Override
    public int getBrewTime() {
        return brewTime;
    }

    @Override
    public int getBrewRecipeTime() {
        return brewRecipeTime;
    }

}
