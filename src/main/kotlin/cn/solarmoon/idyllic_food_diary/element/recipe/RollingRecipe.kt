package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.data.RecipeJsonBuilder
import cn.solarmoon.spark_core.api.data.SerializeHelper
import cn.solarmoon.spark_core.api.data.element.ChanceResult
import cn.solarmoon.spark_core.api.entry_builder.common.RecipeBuilder
import cn.solarmoon.spark_core.api.recipe.IConcreteRecipe
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

@JvmRecord
data class RollingRecipe(
    val input: Ingredient,
    val time: Int,
    val output: Block,
    val chanceResults: List<ChanceResult> = listOf()
) : IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.ROLLING

    val results: List<ItemStack>
        get() = ChanceResult.getResults(chanceResults)

    fun getRolledResults(player: Player): List<ItemStack> {
        return ChanceResult.getRolledResults(player, chanceResults)
    }

    fun hasBlockOutput(): Boolean {
        return output !== Blocks.AIR
    }

    class Serializer : RecipeSerializer<RollingRecipe> {
        override fun codec(): MapCodec<RollingRecipe> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter { it.input },
                    Codec.INT.fieldOf("time").forGetter { it.time },
                    SerializeHelper.BLOCK.CODEC.fieldOf("output").forGetter { it.output },
                    ChanceResult.LIST_CODEC.fieldOf("results").forGetter { it.chanceResults }
                ).apply(it, ::RollingRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, RollingRecipe> {
            return StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, RollingRecipe::input,
                ByteBufCodecs.INT, RollingRecipe::time,
                SerializeHelper.BLOCK.STREAM_CODEC, RollingRecipe::output,
                ChanceResult.LIST_STREAM_CODEC, RollingRecipe::chanceResults,
                ::RollingRecipe
            )
        }
    }

    class JsonBuilder(val r: () -> RollingRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.BLOCK.getKey(r.invoke().output)

        override val prefix: String
            get() = IFDRecipes.ROLLING.type.id.path

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}