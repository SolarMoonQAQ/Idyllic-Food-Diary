package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class KettleBlockEntity extends BaseTankBlockEntity implements IKettleRecipe {

    private int time;
    private int recipeTime;

    public KettleBlockEntity(int maxCapacity, BlockPos pos, BlockState state) {
        super(IMBlockEntities.KETTLE.get(), maxCapacity, pos, state);
    }

    @Override
    public int getBoilRecipeTime() {
        return recipeTime;
    }

    @Override
    public int getBoilTime() {
        return time;
    }

    @Override
    public void setBoilRecipeTime(int time) {
        recipeTime = time;
    }

    @Override
    public void setBoilTime(int time) {
        this.time = time;
    }

}
