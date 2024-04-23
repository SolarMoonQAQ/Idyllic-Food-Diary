package cn.solarmoon.idyllic_food_diary.common.block_entity.base;

import cn.solarmoon.idyllic_food_diary.common.block_entity.iutor.IKettleRecipe;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractKettleBlockEntity extends BaseTankBlockEntity implements IKettleRecipe {

    private int time;
    private int recipeTime;

    public AbstractKettleBlockEntity(BlockEntityType<?> type, int maxCapacity, BlockPos pos, BlockState state) {
        super(type, maxCapacity, pos, state);
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
