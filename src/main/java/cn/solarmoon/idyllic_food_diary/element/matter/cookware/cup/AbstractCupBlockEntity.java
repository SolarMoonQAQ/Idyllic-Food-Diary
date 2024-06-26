package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup;

import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.IBrewingRecipe;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.fluid.TileTank;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public abstract class AbstractCupBlockEntity extends SyncedBlockEntity implements IBrewingRecipe {

    private final TileInventory inventory;
    private final TileTank fluidTank;

    private int time;
    private int recipeTime;

    public AbstractCupBlockEntity(int size, int maxCapability, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        inventory = new TileInventory(size, 1, this);
        fluidTank = new TileTank(maxCapability, this);
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
