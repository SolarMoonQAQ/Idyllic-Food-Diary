package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.water_boiling.IWaterBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.IBrewingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.fluid.TileTank;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class KettleBlockEntity extends SyncedBlockEntity implements IWaterBoilingRecipe, IBrewingRecipe {

    private final TileInventory inventory;
    private final TileTank fluidTank;

    private int boilTime;
    private int boilRecipeTime;
    private int brewTime;
    private int brewRecipeTime;

    public KettleBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.KETTLE.get(), pos, state);
        inventory = new TileInventory(3, 1, this);
        fluidTank = new TileTank(1000, this);
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

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public FluidTank getTank() {
        return fluidTank;
    }

}
