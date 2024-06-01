package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IWaterBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTankBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IBlockEntityAnimateTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SteamerBaseBlockEntity extends BaseTankBlockEntity implements IWaterBoilingRecipe, IBlockEntityAnimateTicker, cn.solarmoon.idyllic_food_diary.api.common.block_entity.IEvaporationRecipe {

    private int boilTime;
    private int recipeTime;
    private int drainTick;
    private int animTick;
    private float last;

    public SteamerBaseBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.STEAMER_BASE.get(), 1000, pos, state);
    }

    @Override
    public int getBoilRecipeTime() {
        return recipeTime;
    }

    @Override
    public int getBoilTime() {
        return boilTime;
    }

    @Override
    public void setBoilRecipeTime(int time) {
        recipeTime = time;
    }

    @Override
    public void setBoilTime(int time) {
        boilTime = time;
    }

    @Override
    public int getTicks() {
        return animTick;
    }

    @Override
    public void setTicks(int ticks) {
        animTick = ticks;
    }

    @Override
    public float getLast() {
        return last;
    }

    @Override
    public void setLast(float last) {
        this.last = last;
    }

    @Override
    public void setEvaporationTick(int tick) {
        drainTick = tick;
    }

    @Override
    public int getEvaporationTick() {
        return drainTick;
    }

    @Override
    public boolean isDirectEnabled() {
        return true;
    }

}
