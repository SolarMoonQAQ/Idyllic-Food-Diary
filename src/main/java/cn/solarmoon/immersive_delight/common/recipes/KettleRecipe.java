package cn.solarmoon.immersive_delight.common.recipes;

import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseTankBlockEntity;
import cn.solarmoon.immersive_delight.util.Util;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static cn.solarmoon.immersive_delight.common.registry.IMRecipes.KETTLE_RECIPE;
import static cn.solarmoon.immersive_delight.common.registry.IMRecipes.KETTLE_RECIPE_SERIALIZER;

public class KettleRecipe implements Recipe<RecipeWrapper> {

    private final ResourceLocation id;
    private final String input;
    private final int time;
    private final String output;

    public KettleRecipe(ResourceLocation id, String input, int time, String output) {
        this.id = id;
        this.input = input;
        this.time = time;
        this.output = output;
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper inv, @NotNull Level level) {
        return false;
    }

    /**
     * 检查容器内液体是否匹配配方输入
     * 并且容器下方有热源
     * 并且容量要为最大容量
     */
    public boolean inputMatches(Level level, FluidStack fluidStackIn, BlockPos pos) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(input));
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (fluid != null) {
            BlockState state = level.getBlockState(pos.below());
            boolean isHeated = Util.isHeatSource(state);
            if(blockEntity instanceof BaseTankBlockEntity tankBlockEntity) {
                FluidStack fluidStack = new FluidStack(fluid, tankBlockEntity.maxCapacity);
                return fluidStack.equals(fluidStackIn) && isHeated && fluidStack.getAmount() == fluidStackIn.getAmount();
            }
        }
        return false;
    }

    /**
     * 获取输出流体
     */
    public Fluid getOutputFluid() {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation(output));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper inv, @NotNull RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return KETTLE_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return KETTLE_RECIPE.get();
    }

    /**
     * 用于比较配方时的匹配逻辑
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KettleRecipe that = (KettleRecipe) o;

        if (!id.equals(that.id)) return false;
        if (!input.equals(that.input)) return false;
        if (getTime() != that.getTime()) return false;
        return output.equals(that.output);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + input.hashCode();
        result = 31 * result + getTime();
        result = 31 * result + (output != null ? output.hashCode() : 0);
        return result;
    }


    //序列化编码解码
    public static class Serializer implements RecipeSerializer<KettleRecipe> {

        public Serializer() {
        }

        @Override
        public @NotNull KettleRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {

            String input = GsonHelper.getAsString(json, "input");

            final int time = GsonHelper.getAsInt(json, "time");

            String output = GsonHelper.getAsString(json, "output");

            return new KettleRecipe(recipeId, input, time, output);
        }

        @Nullable
        @Override
        public KettleRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {

            String input = buffer.readUtf();

            int time = buffer.readInt();

            String output = buffer.readUtf();

            return new KettleRecipe(recipeId, input, time, output);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, KettleRecipe recipe) {

            buffer.writeUtf(recipe.input);

            buffer.writeInt(recipe.time);

            buffer.writeUtf(recipe.output);

        }

    }

}
