package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IBrewTeaRecipe;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNBTList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class KettleBlockEntity extends BaseTCBlockEntity implements IKettleRecipe, IBrewTeaRecipe {

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
