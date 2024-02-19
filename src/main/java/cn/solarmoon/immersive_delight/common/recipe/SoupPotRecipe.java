package cn.solarmoon.immersive_delight.common.recipe;

import cn.solarmoon.immersive_delight.util.FarmerUtil;
import cn.solarmoon.solarmoon_core.common.block_entity.BaseTCBlockEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.solarmoon.immersive_delight.common.registry.IMRecipes.SOUP_POT;

public class SoupPotRecipe implements Recipe<RecipeWrapper> {

    private static final String AIR = "minecraft:air";

    private final ResourceLocation id;
    private final List<Ingredient> inputIngredients;
    private final String inputFluid;
    private final String inputFluidTag;
    public final int inputFluidAmount;
    private final int time;
    private final String outputFluid;
    public final int outputFluidAmount;
    public final List<Item> outputItems;

    public SoupPotRecipe(ResourceLocation id, List<Ingredient> inputs, String inputFluid, String inputFluidTag, int inputFluidAmount, int time, String output, int fluidAmount, List<Item> outputItems) {
        this.id = id;
        this.inputIngredients = inputs;
        this.inputFluid = inputFluid;
        this.inputFluidTag = inputFluidTag;
        this.inputFluidAmount = inputFluidAmount;
        this.time = time;
        this.outputFluid = output;
        this.outputFluidAmount = fluidAmount;
        this.outputItems = outputItems;
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper inv, @NotNull Level level) {
        return false;
    }

    /**
     * 要求：
     * 输入物完全匹配
     * 输入液体及量完全匹配
     * 下方为热源
     */
    public boolean inputMatches(BaseTCBlockEntity ct, Level level, BlockPos pos) {
        List<ItemStack> stacks = ct.getStacks();
        if (RecipeMatcher.findMatches(stacks, inputIngredients) != null) {
            FluidStack ctStack = ct.getTank().getFluid();
            if ( ( ( getInputFluid() != null && getInputFluid().equals(ctStack.getFluid()) )
                    || ( getInputFluidTag() != null && ctStack.getFluid().defaultFluidState().is(getInputFluidTag()) ) )
                    && inputFluidAmount == ct.getTank().getFluidAmount()) {
                return FarmerUtil.isHeatSource(level.getBlockState(pos.below()));
            }
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper inv, @NotNull RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    public int getTime() {
        return this.time;
    }

    public List<Ingredient> getInputIngredients() {
        return this.inputIngredients;
    }

    public TagKey<Fluid> getInputFluidTag() {
        return Objects.equals(inputFluidTag, AIR) ? null : FluidTags.create(new ResourceLocation(this.inputFluidTag));
    }

    public Fluid getInputFluid() {
        return Objects.equals(inputFluidTag, AIR) ? null : ForgeRegistries.FLUIDS.getValue(new ResourceLocation(inputFluid));
    }

    public Fluid getOutputFluid() {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation(outputFluid));
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return SOUP_POT.getSerializer();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return SOUP_POT.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoupPotRecipe that = (SoupPotRecipe) o;

        if (!id.equals(that.id)) return false;
        if (!inputIngredients.equals(that.inputIngredients)) return false;
        if (!inputFluid.equals(that.outputFluid)) return false;
        if (inputFluidAmount != that.inputFluidAmount) return false;
        if (time != that.time) return false;
        if (!outputFluid.equals(that.outputFluid)) return false;
        if (outputFluidAmount != that.outputFluidAmount) return false;
        return outputItems.equals(that.outputItems);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + inputIngredients.hashCode();
        result = 31 * result + inputFluid.hashCode();
        result = 31 * result + inputFluidAmount;
        result = 31 * result + time;
        result = 31 * result + outputFluid.hashCode();
        result = 31 * result + outputFluidAmount;
        result = 31 * result + outputItems.hashCode();
        return result;
    }


    //序列化编码解码
    public static class Serializer implements RecipeSerializer<SoupPotRecipe> {

        public Serializer() {
        }

        @Override
        public @NotNull SoupPotRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {

            List<Ingredient> inputIngredients = new ArrayList<>();
            JsonArray inArray = GsonHelper.getAsJsonArray(json, "input_ingredient");
            for (var element : inArray) {
                inputIngredients.add(Ingredient.fromJson(element));
            }

            JsonObject inFluid = GsonHelper.getAsJsonObject(json, "input_fluid");
            String inputFluid = AIR;
            String inputFluidTag = AIR;
            if (inFluid.has("id")) {
                inputFluid = GsonHelper.getAsString(inFluid, "id");
            } else if (inFluid.has("tag")) {
                inputFluidTag = GsonHelper.getAsString(inFluid, "tag");
            }
            int inputFluidAmount = GsonHelper.getAsInt(inFluid, "amount");

            int time = GsonHelper.getAsInt(json, "time");

            JsonObject outFluid = GsonHelper.getAsJsonObject(json, "output_fluid");
            String outputFluid = GsonHelper.getAsString(outFluid, "id");
            int outputFluidAmount = GsonHelper.getAsInt(outFluid, "amount");

            List<Item> outputItems = new ArrayList<>();
            if (json.has("output_item")) {
                JsonArray itemArray = GsonHelper.getAsJsonArray(json, "output_item");
                for (var element : itemArray) {
                    JsonObject object = element.getAsJsonObject();
                    outputItems.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(object.get("item").getAsString())));
                }
            }

            return new SoupPotRecipe(recipeId, inputIngredients, inputFluid, inputFluidTag, inputFluidAmount, time, outputFluid, outputFluidAmount, outputItems);
        }

        @Nullable
        @Override
        public SoupPotRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {

            List<Ingredient> inputIngredients = new ArrayList<>();
            int inCount = buffer.readVarInt();
            for (int i = 0; i < inCount; i++) {
                inputIngredients.add(Ingredient.fromNetwork(buffer));
            }

            String inputFluid = buffer.readUtf();
            String inputFluidTag = buffer.readUtf();
            int inputFluidAmount = buffer.readInt();

            int time = buffer.readInt();

            String outputFluid = buffer.readUtf();
            int outputFluidAmount = buffer.readInt();

            List<Item> outputItems = new ArrayList<>();
            int itemCount = buffer.readVarInt();
            for (int i = 0; i < itemCount; i++) {
                outputItems.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(buffer.readUtf())));
            }

            return new SoupPotRecipe(recipeId, inputIngredients, inputFluid, inputFluidTag, inputFluidAmount, time, outputFluid, outputFluidAmount, outputItems);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, SoupPotRecipe recipe) {

            for (var in : recipe.inputIngredients) {
                in.toNetwork(buffer);
            }

            buffer.writeUtf(recipe.inputFluid);
            buffer.writeUtf(recipe.inputFluidTag);
            buffer.writeInt(recipe.inputFluidAmount);

            buffer.writeInt(recipe.time);

            buffer.writeUtf(recipe.outputFluid);
            buffer.writeInt(recipe.outputFluidAmount);

            for (Item item : recipe.outputItems) {
                buffer.writeItem(item.getDefaultInstance());
            }

        }

    }


}