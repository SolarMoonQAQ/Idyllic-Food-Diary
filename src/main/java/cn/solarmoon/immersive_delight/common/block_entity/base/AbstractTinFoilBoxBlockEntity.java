package cn.solarmoon.immersive_delight.common.block_entity.base;

import cn.solarmoon.immersive_delight.common.recipe.TinFoilGrillingRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.common.block_entity.BaseContainerBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.iutor.ITimeRecipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class AbstractTinFoilBoxBlockEntity extends BaseContainerBlockEntity implements ITimeRecipeBlockEntity<TinFoilGrillingRecipe> {

    private int time;
    private int recipeTime;

    public AbstractTinFoilBoxBlockEntity(BlockEntityType<?> type, int size, BlockPos pos, BlockState state) {
        super(type, size, 1, pos, state);
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
    public void setTime(int i) {
        time = i;
    }

    @Override
    public void setRecipeTime(int i) {
        recipeTime = i;
    }

    @Override
    public TinFoilGrillingRecipe getCheckedRecipe() {
        Level level = getLevel();
        if (level == null) return null;
        BlockPos pos = getBlockPos();
        List<TinFoilGrillingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.TIN_FOIL_GRILLING.get());
        for (var recipe : recipes) {
            if (recipe.inputMatches(this, pos, level)) {
                return recipe;
            }
        }
        return null;
    }

}
