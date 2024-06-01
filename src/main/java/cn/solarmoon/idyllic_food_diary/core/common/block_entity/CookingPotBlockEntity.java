package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.*;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.Spice;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.SpiceList;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CookingPotBlockEntity extends BaseTCBlockEntity implements IStewRecipe,
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

    public CookingPotBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.COOKING_POT.get(), 1000, 9, 1, pos, state);
    }

    /**
     * @return 液体是否正被加热（是否有液体且下方是否为热源）,这个和配方无关
     */
    public boolean isHeatingFluid() {
        return isHeatingConsiderStove() && !getTank().isEmpty();
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
        return findStewRecipe().isEmpty();
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

}
