package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.feature.food_container.FoodContainer
import cn.solarmoon.idyllic_food_diary.feature.util.HeatSourceUtil
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.data.RecipeJsonBuilder
import cn.solarmoon.spark_core.api.entry_builder.common.RecipeBuilder
import cn.solarmoon.spark_core.api.recipe.IConcreteRecipe
import cn.solarmoon.spark_core.api.recipe.processor.SingleTimeRecipeProcessor
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.wrapper.RecipeWrapper

@JvmRecord
data class BakingRecipe(
    val ingredient: Ingredient,
    val time: Int,
    val result: ItemStack
) : IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.BAKING

    class Serializer : RecipeSerializer<BakingRecipe> {
        override fun codec(): MapCodec<BakingRecipe> {
            return RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter{ it.ingredient },
                    Codec.INT.fieldOf("time").forGetter{ it.time },
                    ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter{ it.result }
                ).apply(instance, ::BakingRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, BakingRecipe> {
            return StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, BakingRecipe::ingredient,
                ByteBufCodecs.INT, BakingRecipe::time,
                ItemStack.OPTIONAL_STREAM_CODEC, BakingRecipe::result,
                ::BakingRecipe
            )
        }
    }

    class Processor(be: BlockEntity, val inv: ItemStackHandler): SingleTimeRecipeProcessor<RecipeWrapper, BakingRecipe>(be) {

        override fun tryWork(): Boolean {
            findRecipe()?.let {
                time++
                recipeTime = it.value.time
                if (time > recipeTime) {
                    val result = it.value.result.copy()
                    val container = inv.getStackInSlot(0).getOrDefault(IFDDataComponents.FOOD_CONTAINER, FoodContainer.EMPTY).stack
                    if (!container.isEmpty) result.set(IFDDataComponents.FOOD_CONTAINER, FoodContainer(container))
                    inv.setStackInSlot(0, result)
                    time = 0
                    recipeTime = 0
                    be.setChanged()
                }
                return true
            } ?: let {
                time = 0
                recipeTime = 0
                return false
            }
        }

        override fun isRecipeMatch(recipe: BakingRecipe): Boolean {
            return recipe.ingredient.test(inv.getStackInSlot(0)) && HeatSourceUtil.isOnHeatSource(be)
        }

        override fun getRecipeType(): RecipeType<BakingRecipe> = IFDRecipes.BAKING.type.get()

    }

    class JsonBuilder(val r: () -> BakingRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.ITEM.getKey(r.invoke().result.item)

        override val prefix: String
            get() = "${IFDRecipes.BAKING.type.id.path}"

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}