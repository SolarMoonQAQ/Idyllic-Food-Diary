package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup;

import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.IBrewingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.blockentity_base.BaseTCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CupBlockEntity extends BaseTCBlockEntity implements IBrewingRecipe {

    private int time;
    private int recipeTime;

    public CupBlockEntity(int maxCapacity, int size, int slotLimit, BlockPos pos, BlockState state) {
        super(IMBlockEntities.LITTLE_CUP.get(), maxCapacity, size, slotLimit, pos, state);
    }

    @Override
    public void setBrewTime(int time) {
        this.time = time;
    }

    @Override
    public void setBrewRecipeTime(int time) {
        this.recipeTime = time;
    }

    @Override
    public int getBrewTime() {
        return time;
    }

    @Override
    public int getBrewRecipeTime() {
        return recipeTime;
    }

}
