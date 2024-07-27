package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.api.AnimTicker;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.grinding.IGrindingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.fluid.TileTank;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class MillstoneBlockEntity extends SyncedBlockEntity implements IGrindingRecipe {

    public final MillstoneInventory inventory;
    private final List<TileTank> tanks;
    private int time;
    private int recipeTime;
    private boolean flowing;

    public MillstoneBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.MILLSTONE.get(), pos, state);
        inventory = new MillstoneInventory(2, this);
        tanks = List.of(
                new TileTank(1000, this),
                new TileTank(250, this)
        );
        AnimHelper.createMap(this, "rotation", "flow");
        AnimHelper.getMap(this).get("rotation").getTimer().setMaxTime(30);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox().inflate(3);
    }

    @Override
    public int getGrindingTime() {
        return time;
    }

    @Override
    public void setGrindingTime(int time) {
        this.time = time;
    }

    @Override
    public int getGrindingRecipeTime() {
        return recipeTime;
    }

    @Override
    public void setGrindingRecipeTime(int time) {
        recipeTime = time;
    }

    @Override
    public void setFlowing(boolean flag) {
        flowing = flag;
    }

    @Override
    public boolean isFlowing() {
        return flowing;
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public List<TileTank> getTanks() {
        return tanks;
    }

    @Override
    public FluidTank tankCapabilityProvider(Direction direction) {
        if (direction == Direction.DOWN) return getTank(1);
        return getTank(0);
    }

}
