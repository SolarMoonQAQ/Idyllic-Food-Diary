package cn.solarmoon.idyllic_food_diary.core.common.recipe;

import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.util.SerializeHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 烧水配方，所有烧东西的容器可通用，以水壶为基准，也就是1000mB为基准
 */
public record WaterBoilingRecipe(
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
        return IMRecipes.WATER_BOILING;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    public static class Serializer implements RecipeSerializer<WaterBoilingRecipe> {

        @Override
        public @NotNull WaterBoilingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            Fluid input = SerializeHelper.readFluid(json, "input");
            final int time = GsonHelper.getAsInt(json, "time");
            Fluid output = SerializeHelper.readFluid(json, "output");
            return new WaterBoilingRecipe(recipeId, input, time, output);
        }

        @Nullable
        @Override
        public WaterBoilingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            Fluid input = SerializeHelper.readFluid(buffer);
            int time = buffer.readInt();
            Fluid output = SerializeHelper.readFluid(buffer);
            return new WaterBoilingRecipe(recipeId, input, time, output);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, WaterBoilingRecipe recipe) {
            SerializeHelper.writeFluid(buffer, recipe.input());
            buffer.writeInt(recipe.time());
            SerializeHelper.writeFluid(buffer, recipe.output());
        }

    }
}
