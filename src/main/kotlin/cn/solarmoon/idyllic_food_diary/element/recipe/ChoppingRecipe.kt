package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.data.RecipeJsonBuilder
import cn.solarmoon.spark_core.api.data.element.ChanceResult
import cn.solarmoon.spark_core.api.entry_builder.common.RecipeBuilder
import cn.solarmoon.spark_core.api.recipe.IConcreteRecipe
import cn.solarmoon.spark_core.feature.inlay.AttributeForgingRecipe
import cn.solarmoon.spark_core.registry.common.SparkRecipes
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer

@JvmRecord
data class ChoppingRecipe(
    val input: Ingredient,
    val chanceResults: List<ChanceResult>
) : IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.CHOPPING

    val results: List<ItemStack>
        get() = ChanceResult.getResults(chanceResults)

    fun getRolledResults(player: Player): List<ItemStack> = ChanceResult.getRolledResults(player, chanceResults)

    class Serializer : RecipeSerializer<ChoppingRecipe> {
        override fun codec(): MapCodec<ChoppingRecipe> {
            return RecordCodecBuilder.mapCodec<ChoppingRecipe> { instance ->
                instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter { it.input },
                    ChanceResult.LIST_CODEC.fieldOf("results").forGetter { it.chanceResults }
                ).apply(instance, ::ChoppingRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, ChoppingRecipe> {
            return StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, ChoppingRecipe::input,
                ChanceResult.LIST_STREAM_CODEC, ChoppingRecipe::chanceResults,
                ::ChoppingRecipe
            )
        }
    }

    class JsonBuilder(val r: () -> ChoppingRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.ITEM.getKey(r.invoke().chanceResults[0].stack.item)

        override val prefix: String
            get() = "${IFDRecipes.CHOPPING.type.id.path}"

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}