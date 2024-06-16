package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.evaporation.IEvaporationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.food_boiling.IFoodBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.soup.ISoupRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stew.IStewRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.IStirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.StirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.spice.Spice;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.water_boiling.IWaterBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.blockentity_base.BaseTCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CookingPotBlockEntity extends BaseTCBlockEntity implements IStewRecipe, IStirFryRecipe,
        IWaterBoilingRecipe, IEvaporationRecipe, ISoupRecipe, IFoodBoilingRecipe {

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

    private int evaTick;

    private int stageNumber;
    private int stirFryTime;
    private int stirFryRecipeTime;
    private boolean canStirFry;
    private int fryCount;
    private ItemStack pending;
    private StirFryRecipe stirFryRecipe;

    public CookingPotBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.COOKING_POT.get(), 1000, 9, 1, pos, state);
        pending = ItemStack.EMPTY;
    }

    /**
     * @return 液体是否正被加热（是否有液体且下方是否为热源）,这个和配方无关
     */
    public boolean isHeatingFluid() {
        return isOnHeatSource() && !getTank().isEmpty();
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
        return !(findStewRecipe().isPresent() || getPresentFryStage() != null || findSimmerRecipe().isPresent());
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
    public void setEvaporationTick(int tick) {
        evaTick = tick;
    }

    @Override
    public int getEvaporationTick() {
        return evaTick;
    }

    @Override
    public boolean isDirectEnabled() {
        return false;
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
    public int[] getTimes() {
        return iTimes;
    }

    @Override
    public int[] getRecipeTimes() {
        return iRecipeTimes;
    }

    @Override
    public void setTimes(int[] ints) {
        iTimes = ints;
    }

    @Override
    public void setRecipeTimes(int[] ints) {
        iRecipeTimes = ints;
    }

    @Override
    public @Nullable StirFryRecipe getStirFryRecipe() {
        return stirFryRecipe;
    }

    @Override
    public void setStirFryRecipe(StirFryRecipe recipe) {
        stirFryRecipe = recipe;
    }

    @Override
    public int getPresentStage() {
        return stageNumber;
    }

    @Override
    public void setPresentStage(int stage) {
        stageNumber = stage;
    }

    @Override
    public int getStirFryTime() {
        return stirFryTime;
    }

    @Override
    public void setStirFryTime(int time) {
        stirFryTime = time;
    }

    @Override
    public int getStirFryRecipeTime() {
        return stirFryRecipeTime;
    }

    @Override
    public void setStirFryRecipeTime(int time) {
        stirFryRecipeTime = time;
    }

    @Override
    public boolean canStirFry() {
        return canStirFry;
    }

    @Override
    public void setCanStirFry(boolean or) {
        canStirFry = or;
    }

    @Override
    public int getFryCount() {
        return fryCount;
    }

    @Override
    public void setFryCount(int count) {
        fryCount = count;
    }

    @Override
    public ItemStack getPendingItem() {
        return pending;
    }

    @Override
    public void setPendingItem(ItemStack stack) {
        pending = stack;
    }

}
