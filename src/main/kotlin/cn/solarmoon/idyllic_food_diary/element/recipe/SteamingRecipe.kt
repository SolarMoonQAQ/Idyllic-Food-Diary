package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerBlockEntity
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerItemHandler
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.data.RecipeJsonBuilder
import cn.solarmoon.spark_core.api.entry_builder.common.RecipeBuilder
import cn.solarmoon.spark_core.api.recipe.IConcreteRecipe
import cn.solarmoon.spark_core.api.recipe.processor.MultiTimeRecipeProcessor
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
import net.neoforged.neoforge.items.wrapper.RecipeWrapper

@JvmRecord
data class SteamingRecipe(
    val input: Ingredient,
    val time: Int,
    val output: ItemStack
) : IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.STEAMING

    class Serializer : RecipeSerializer<SteamingRecipe> {
        override fun codec(): MapCodec<SteamingRecipe> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter { it.input },
                    Codec.INT.fieldOf("time").forGetter { it.time },
                    ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter { it.output }
                ).apply(it, ::SteamingRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, SteamingRecipe> {
            return StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, SteamingRecipe::input,
                ByteBufCodecs.INT, SteamingRecipe::time,
                ItemStack.OPTIONAL_STREAM_CODEC, SteamingRecipe::output,
                ::SteamingRecipe
            )
        }
    }

    open class Processor(override val be: SteamerBlockEntity, val inv: SteamerItemHandler): MultiTimeRecipeProcessor<RecipeWrapper, SteamingRecipe>(be) {

        override fun tryWork(): Boolean {
            var flag = false
            val level = be.level ?: return false
            if (level.isClientSide) return false
            for (i in 0 until inv.slots) {
                findRecipe(i)?.let {
                    times[i]++
                    recipeTimes[i] = it.value.time
                    if (times[i] > recipeTimes[i]) {
                        inv.setStackInSlot(i, it.value.output.copy())
                        times[i] = 0
                        recipeTimes[i] = 0
                        be.setChanged()
                    }
                    flag = true
                } ?: let {
                    times[i] = 0
                    recipeTimes[i] = 0
                }
            }
            return flag
        }

        override fun isRecipeMatch(recipe: SteamingRecipe, index: Int): Boolean {
            val stack = inv.getStackInSlot(index)
            return recipe.input.test(stack) && canWork()
        }

        open fun canWork(): Boolean {
            return be.getBase()?.isValidForSteamer() == true && be.getTop()?.hasLid == true
        }

        override fun getRecipeType(): RecipeType<SteamingRecipe> = IFDRecipes.STEAMING.type.get()

    }

    class JsonBuilder(val r: () -> SteamingRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.ITEM.getKey(r.invoke().output.item)

        override val prefix: String
            get() = IFDRecipes.STEAMING.type.id.path

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}