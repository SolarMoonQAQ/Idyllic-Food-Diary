package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IIngredientHandlingRecipe;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.ISpiceable;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.Spice;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.SpiceList;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class CuttingBoardBlockEntity extends BaseContainerBlockEntity implements IIngredientHandlingRecipe {

    public SpiceList spices = new SpiceList();

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.CUTTING_BOARD.get(), 9, 1, pos, state);
    }

    @Override
    public SpiceList getSpices() {
        return spices;
    }

    @Override
    public Spice.Step getSpiceStep() {
        return Spice.Step.ADD;
    }

    @Override
    public boolean timeToResetSpices() {
        return findHandleRecipe().isEmpty();
    }

}
