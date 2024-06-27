package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.tea_production;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.StirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.StirFryStage;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import cn.solarmoon.solarmoon_core.api.entry.common.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.recipe.IConcreteRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.List;

public record TeaProductionRecipe(
        ResourceLocation id,
        Ingredient ingredient,
        int time,
        Environment environment,
        ItemStack result
) implements IConcreteRecipe {

    public enum Environment {
        SUNLIGHT,
        SHADE;

        public boolean isEnvironmentMatching(ITeaProductionRecipe tp) {
            switch (this) {
                case SUNLIGHT -> {
                    return tp.isUnderSunshine();
                }
                case SHADE -> {
                    return tp.isInShade();
                }
            }
            return false;
        }

    }

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.TEA_PRODUCTION;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Serializer implements RecipeSerializer<TeaProductionRecipe> {

        @Override
        public @NotNull TeaProductionRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            Ingredient ingredient = SerializeHelper.readIngredient(json, "ingredient");
            int time = GsonHelper.getAsInt(json, "time", 0);
            Environment environment = Environment.valueOf(GsonHelper.getAsString(json, "environment", "sunlight").toUpperCase());
            ItemStack result = SerializeHelper.readItemStack(json, "result");
            return new TeaProductionRecipe(recipeId, ingredient, time, environment, result);
        }

        @Nullable
        @Override
        public TeaProductionRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int time = buf.readInt();
            Environment environment = buf.readEnum(Environment.class);
            ItemStack result = SerializeHelper.readItemStack(buf);
            return new TeaProductionRecipe(recipeId, ingredient, time, environment, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, TeaProductionRecipe recipe) {
            recipe.ingredient.toNetwork(buf);
            buf.writeInt(recipe.time);
            buf.writeEnum(recipe.environment);
            SerializeHelper.writeItemStack(buf, recipe.result);
        }

    }

}
