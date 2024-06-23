package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup;

import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.IBrewingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import cn.solarmoon.solarmoon_core.api.tile.fluid.TileTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class CupBlockEntity extends SyncedBlockEntity implements IBrewingRecipe {

    private final TileInventory inventory;
    private final TileTank fluidTank;


    private int time;
    private int recipeTime;

    public CupBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.LITTLE_CUP.get(), pos, state);
        inventory = new TileInventory(1, 1, this);
        fluidTank = new TileTank(250, this);
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

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public FluidTank getTank() {
        return fluidTank;
    }

}
