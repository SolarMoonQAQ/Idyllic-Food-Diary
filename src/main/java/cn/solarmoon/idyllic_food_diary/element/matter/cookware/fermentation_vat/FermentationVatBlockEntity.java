package cn.solarmoon.idyllic_food_diary.element.matter.cookware.fermentation_vat;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.fermentation.IFermentationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.spice.Spice;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.fluid.TileTank;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class FermentationVatBlockEntity extends SyncedBlockEntity implements IFermentationRecipe {

    private int time;
    private int recipeTime;
    private final SpiceList spices;
    private final TileInventory inventory;
    private final TileTank tank;

    public FermentationVatBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.FERMENTER.get(), pos, state);
        spices = new SpiceList();
        inventory = new TileInventory(24, 1, this);
        tank = new TileTank(1000, this);
    }

    @Override
    public int getFermentTime() {
        return time;
    }

    @Override
    public void setFermentTime(int time) {
        this.time = time;
    }

    @Override
    public int getFermentRecipeTime() {
        return recipeTime;
    }

    @Override
    public void setFermentRecipeTime(int time) {
        recipeTime = time;
    }

    @Override
    public SpiceList getSpices() {
        return spices;
    }

    @Override
    public Spice.Step getSpiceStep() {
        return Spice.Step.MIX;
    }

    @Override
    public boolean timeToResetSpices() {
        return findFermentRecipe().isEmpty();
    }

    @Override
    public FluidTank getTank() {
        return tank;
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

}
