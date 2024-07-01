package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.water_boiling;

import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import cn.solarmoon.solarmoon_core.api.entry.common.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.recipe.IConcreteRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 烧水配方，所有烧东西的容器可通用，以水壶为基准，也就是1000mB为基准
 */
public record WaterBoilingRecipe(
        ResourceLocation id,
        Fluid input,
        int time
) implements IConcreteRecipe {

    public static final int BaseFluidAmount = 1000;

    /**
     * 把烧水时间和当前要烧的水的容量绑定（实现动态烧水时间）
     */
    public int getActualTime(BlockEntity blockEntity) {
        return blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER)
                .map(tank -> {
                    double scale = (double) tank.getFluidInTank(0).getAmount() / BaseFluidAmount;
                    return (int) (time * scale);
                })
                .orElse(time);
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
            return new WaterBoilingRecipe(recipeId, input, time);
        }

        @Nullable
        @Override
        public WaterBoilingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            Fluid input = SerializeHelper.readFluid(buffer);
            int time = buffer.readInt();
            return new WaterBoilingRecipe(recipeId, input, time);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, WaterBoilingRecipe recipe) {
            SerializeHelper.writeFluid(buffer, recipe.input());
            buffer.writeInt(recipe.time());
        }

    }
}
