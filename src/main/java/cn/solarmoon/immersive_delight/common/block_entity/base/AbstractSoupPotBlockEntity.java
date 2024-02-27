package cn.solarmoon.immersive_delight.common.block_entity.base;

import cn.solarmoon.immersive_delight.common.recipe.KettleRecipe;
import cn.solarmoon.immersive_delight.common.recipe.SoupPotRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.common.block_entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.util.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;

public abstract class AbstractSoupPotBlockEntity extends BaseTCBlockEntity implements ITimeRecipeBlockEntity<SoupPotRecipe>, IContainerBlockEntity {

    private int time;
    private int recipeTime;

    public int boilTime;
    public int boilRecipeTime;

    public AbstractSoupPotBlockEntity(BlockEntityType<?> type, int maxCapacity, int size, int slotLimit, BlockPos pos, BlockState state) {
        super(type, maxCapacity, size, slotLimit, pos, state);
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public int getRecipeTime() {
        return recipeTime;
    }

    @Override
    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void setRecipeTime(int recipeTime) {
        this.recipeTime = recipeTime;
    }

    /**
     * 接受的水壶配方
     */
    public void tryBoilWater() {
        KettleRecipe kettleRecipe = getCheckedKettleRecipe();
        Level level = getLevel();
        BlockPos pos = getBlockPos();
        if (level == null) return;
        if (kettleRecipe != null) {
            boilRecipeTime = kettleRecipe.getActualTime(level, pos);
            boilTime++;
            if (boilTime > kettleRecipe.getActualTime(level, pos)) {
                FluidStack fluidStack = new FluidStack(kettleRecipe.getOutputFluid(), getTank().getFluidAmount());
                getTank().setFluid(fluidStack);
                boilTime = 0;
                setChanged();
            }
        } else {
            boilTime = 0;
            boilRecipeTime = 0;
            setChanged();
        }
    }

    public boolean isBoiling() {
        return boilTime > 0;
    }

    /**
     * 遍历所有配方检测液体是否匹配input且下方为热源<br/>
     * 返回匹配的配方
     */
    public KettleRecipe getCheckedKettleRecipe() {
        Level level = getLevel();
        if (level == null) return null;
        BlockPos pos = getBlockPos();
        for (KettleRecipe kettleRecipe : RecipeUtil.getRecipes(level, IMRecipes.KETTLE.get())) {
            if(kettleRecipe.inputMatches(level, pos)) {
                return kettleRecipe;
            }
        }
        return null;
    }

    /**
     * 获取首个匹配的配方
     */
    @Override
    public SoupPotRecipe getCheckedRecipe() {
        Level level = getLevel();
        if (level == null) return null;
        BlockPos pos = getBlockPos();
        List<SoupPotRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.SOUP_POT.get());
        for (var recipe : recipes) {
            if (recipe.inputMatches(this, level, pos)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt(NBTList.BOIL_TICK, boilTime);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        boilTime = nbt.getInt(NBTList.BOIL_TICK);
    }

}
