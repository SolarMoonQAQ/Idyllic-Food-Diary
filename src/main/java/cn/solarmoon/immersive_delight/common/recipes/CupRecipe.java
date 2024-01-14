package cn.solarmoon.immersive_delight.common.recipes;

import cn.solarmoon.immersive_delight.api.common.entity_block.entities.BaseContainerTankBlockEntity;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static cn.solarmoon.immersive_delight.common.IMRecipes.CUP_RECIPE;
import static cn.solarmoon.immersive_delight.common.IMRecipes.CUP_RECIPE_SERIALIZER;

public class CupRecipe implements Recipe<RecipeWrapper> {

    private final ResourceLocation id;
    private final Ingredient inputIngredient;
    private final String inputFluid;
    private final int fluidAmount;
    private final int time;
    private final String output;

    public CupRecipe(ResourceLocation id, Ingredient inputIngredient, String inputFluid, int fluidAmount, int time, String output) {
        this.id = id;
        this.inputIngredient = inputIngredient;
        this.inputFluid = inputFluid;
        this.fluidAmount = fluidAmount;
        this.time = time;
        this.output = output;
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper inv, @NotNull Level level) {
        return false;
    }

    /**
     * 检查容器内液体是否匹配配方输入:
     * 液体匹配，物品匹配
     * 并且容量要小于于设定容量
     */
    public boolean inputMatches(Level level, FluidStack fluidStackIn, ItemStack itemStackIn, BlockPos pos) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(inputFluid));
        BlockEntity blockEntity = level.getBlockEntity(pos);
        boolean itemFlag = inputIngredient.test(itemStackIn);
        if (fluid != null) {
            if(blockEntity instanceof BaseContainerTankBlockEntity) {
                return fluidStackIn.getFluid().equals(fluid) && itemFlag && fluidStackIn.getAmount() <= fluidAmount;
            }
        }
        return false;
    }

    /**
     * 获取设定的流体量
     */
    public int getFluidAmount() {
        return fluidAmount;
    }

    /**
     * 获取输出流体
     */
    public Fluid getOutputFluid() {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation(output));
    }

    public int getTime() {
        return this.time;
    }

    public Ingredient getInputIngredient() {return this.inputIngredient;}

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper inv, @NotNull RegistryAccess access) {
        return ItemStack.EMPTY;
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
        return CUP_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return CUP_RECIPE.get();
    }

    /**
     * 用于比较配方时的匹配逻辑
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CupRecipe that = (CupRecipe) o;

        if (!id.equals(that.id)) return false;
        if (!inputIngredient.equals(that.inputIngredient)) return false;
        if (!inputFluid.equals(that.inputFluid)) return false;
        if (fluidAmount != that.fluidAmount) return false;
        if (time != that.time) return false;
        return output.equals(that.output);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + inputIngredient.hashCode();
        result = 31 * result + inputFluid.hashCode();
        result = 31 * result + fluidAmount;
        result = 31 * result + time;
        result = 31 * result + (output != null ? output.hashCode() : 0);
        return result;
    }


    //序列化编码解码
    public static class Serializer implements RecipeSerializer<CupRecipe> {

        public Serializer() {
        }

        @Override
        public @NotNull CupRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {

            Ingredient inputIngredient = Ingredient.fromJson(json.get("input_ingredient"));

            String inputFluid = GsonHelper.getAsString(json, "input_fluid");

            final int fluidAmount = GsonHelper.getAsInt(json, "fluid_amount");

            final int time = GsonHelper.getAsInt(json, "time");

            String output = GsonHelper.getAsString(json, "output");

            return new CupRecipe(recipeId, inputIngredient, inputFluid, fluidAmount, time, output);
        }

        @Nullable
        @Override
        public CupRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {

            Ingredient inputIngredient = Ingredient.fromNetwork(buffer);

            String inputFluid = buffer.readUtf();

            int fluidAmount = buffer.readInt();

            int time = buffer.readInt();

            String output = buffer.readUtf();

            return new CupRecipe(recipeId, inputIngredient, inputFluid, fluidAmount, time, output);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, CupRecipe recipe) {

            recipe.inputIngredient.toNetwork(buffer);

            buffer.writeUtf(recipe.inputFluid);

            buffer.writeInt(recipe.fluidAmount);

            buffer.writeInt(recipe.time);

            buffer.writeUtf(recipe.output);

        }

    }

}
