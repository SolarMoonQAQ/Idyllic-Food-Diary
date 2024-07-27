package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock;
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.ITempChanger;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.evaporation.IEvaporationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.food_boiling.IFoodBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.soup.ISoupRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stew.IStewRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.water_boiling.IWaterBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.spice.Spice;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.fluid.TileTank;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class CookingPotBlockEntity extends SyncedBlockEntity implements IStewRecipe,
        IWaterBoilingRecipe, ISoupRecipe, IFoodBoilingRecipe, ITempChanger, IEvaporationRecipe {

    private final TileInventory inventory;
    private final TileTank fluidTank;

    private int time;
    private int recipeTime;
    private int simmerTime;
    private int simmerRecipeTime;
    private int[] iTimes = new int[64];
    private int[] iRecipeTimes = new int[64];

    private ItemStack pendingResult = ItemStack.EMPTY;
    private Ingredient container = Ingredient.EMPTY;

    private int exp;

    private final SpiceList spices = new SpiceList();

    private int boilTime;
    private int boilRecipeTime;

    public CookingPotBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.COOKING_POT.get(), pos, state);
        inventory = new TileInventory(12, 1, this) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return !(Block.byItem(stack.getItem()) instanceof CookwareBlock);
            }
        };
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
    public SpiceList getSpices() {
        return spices;
    }

    @Override
    public Spice.Step getSpiceStep() {
        return Spice.Step.MIX;
    }

    @Override
    public boolean timeToResetSpices() {
        return !(findStewRecipe().isPresent() || findSimmerRecipe().isPresent());
    }

    @Override
    public ItemStack getResult() {
        return pendingResult;
    }

    @Override
    public Ingredient getContainer() {
        return container;
    }

    @Override
    public void setResult(ItemStack newResult) {
        pendingResult = newResult;
    }

    @Override
    public void setContainer(Ingredient newContainer) {
        container = newContainer;
    }

    @Override
    public int getExp() {
        return exp;
    }

    @Override
    public void setExp(int exp) {
        this.exp = exp;
    }

    @Override
    public int getStewTime() {
        return time;
    }

    @Override
    public void setStewTime(int time) {
        this.time = time;
    }

    @Override
    public int getStewRecipeTime() {
        return recipeTime;
    }

    @Override
    public void setStewRecipeTime(int time) {
        recipeTime = time;
    }

    @Override
    public int getSimmerTime() {
        return simmerTime;
    }

    @Override
    public void setSimmerTime(int time) {
        simmerTime = time;
    }

    @Override
    public int getSimmerRecipeTime() {
        return simmerRecipeTime;
    }

    @Override
    public void setSimmerRecipeTime(int time) {
        simmerRecipeTime = time;
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public FluidTank getTank() {
        return fluidTank;
    }

    @Override
    public int[] getFBTimes() {
        return iTimes;
    }

    @Override
    public int[] getFBRecipeTimes() {
        return iRecipeTimes;
    }

    @Override
    public void setFBTimes(int[] ints) {
        iTimes = ints;
    }

    @Override
    public void setFBRecipeTimes(int[] ints) {
        iRecipeTimes = ints;
    }

    private int evaTick;

    @Override
    public void setEvaporationTick(int tick) {
        evaTick = tick;
    }

    @Override
    public int getEvaporationTick() {
        return evaTick;
    }

}
