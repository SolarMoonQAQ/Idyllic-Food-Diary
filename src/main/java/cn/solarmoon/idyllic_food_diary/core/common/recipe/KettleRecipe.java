package cn.solarmoon.idyllic_food_diary.core.common.recipe;

import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

/**
 * 烧水配方，所有烧东西的容器可通用，以水壶为基准，也就是1000mB为基准
 */
public record KettleRecipe(
        ResourceLocation id,
        Fluid input,
        int time,
        Fluid output
) implements IConcreteRecipe {

    public static final int BaseFluidAmount = 1000;

    /**
     * 把烧水时间和当前要烧的水的容量绑定（实现动态烧水时间）
     */
    public int getActualTime(BlockEntity blockEntity) {
        if (blockEntity instanceof ITankBlockEntity container) {
            double scale = (double) container.getTank().getFluidAmount() / BaseFluidAmount;
            return (int) (time * scale);
        }
        return time;
    }

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.KETTLE;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

}
