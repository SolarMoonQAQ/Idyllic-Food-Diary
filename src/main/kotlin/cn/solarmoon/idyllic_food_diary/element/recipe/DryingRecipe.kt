package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.data.RecipeJsonBuilder
import cn.solarmoon.spark_core.api.entry_builder.common.RecipeBuilder
import cn.solarmoon.spark_core.api.recipe.IConcreteRecipe
import cn.solarmoon.spark_core.api.recipe.processor.MultiTimeRecipeProcessor
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
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
data class DryingRecipe(
    val ingredient: Ingredient,
    val time: Int,
    val environment: Environment,
    val result: ItemStack
): IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.DRYING

    enum class Environment {
        SUNLIGHT, SHADE;

        fun isEnvironmentMatching(p: Processor): Boolean {
            return when (this) {
                SUNLIGHT -> {
                    p.isUnderSunshine()
                }
                SHADE -> {
                    p.isInShade()
                }
            }
        }

        companion object {
            @JvmStatic
            val CODEC: Codec<Environment> = Codec.STRING.xmap({ Environment.valueOf(it.uppercase()) }, { it.toString().lowercase() })
            @JvmStatic
            val STREAM_CODEC = object : StreamCodec<RegistryFriendlyByteBuf, Environment> {
                override fun decode(buffer: RegistryFriendlyByteBuf): Environment {
                    return buffer.readEnum(Environment::class.java)
                }

                override fun encode(buffer: RegistryFriendlyByteBuf, value: Environment) {
                    buffer.writeEnum(value)
                }
            }
        }
    }

    class Serializer: RecipeSerializer<DryingRecipe> {
        override fun codec(): MapCodec<DryingRecipe> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter { it.ingredient },
                    Codec.INT.fieldOf("time").forGetter { it.time },
                    Environment.CODEC.fieldOf("environment").forGetter { it.environment },
                    ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter { it.result }
                ).apply(it, ::DryingRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, DryingRecipe> {
            return StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, DryingRecipe::ingredient,
                ByteBufCodecs.INT, DryingRecipe::time,
                Environment.STREAM_CODEC, DryingRecipe::environment,
                ItemStack.OPTIONAL_STREAM_CODEC, DryingRecipe::result,
                ::DryingRecipe
            )
        }
    }

    class Processor(be: BlockEntity, val inventory: ItemStackHandler): MultiTimeRecipeProcessor<RecipeWrapper, DryingRecipe>(be) {

        override fun tryWork(): Boolean {
            var flag = false
            for (i in 0 until inventory.slots) {
                findRecipe(i)?.let {
                    times[i]++
                    recipeTimes[i] = it.value.time
                    if (times[i] > recipeTimes[i]) {
                        times[i] = 0
                        recipeTimes[i] = 0
                        inventory.setStackInSlot(i, it.value.result.copy())
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

        override fun isRecipeMatch(recipe: DryingRecipe, index: Int): Boolean {
            val stack = inventory.getStackInSlot(index)
            return recipe.ingredient.test(stack) && recipe.environment.isEnvironmentMatching(this)
        }

        fun isInShade(): Boolean {
            return !isUnderSunshine() && !isInRain()
        }

        fun isUnderSunshine(): Boolean {
            val level = be.level
            val pos: BlockPos = be.blockPos
            if (level == null) return false
            return level.canSeeSky(pos.above()) && !level.isRainingAt(pos) && level.isDay
        }

        fun isInRain(): Boolean {
            val level = be.level
            val pos: BlockPos = be.blockPos
            if (level == null) return false
            return level.isRainingAt(pos)
        }

        override fun getRecipeType(): RecipeType<DryingRecipe> = IFDRecipes.DRYING.type.get()

    }

    class JsonBuilder(val r: () -> DryingRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.ITEM.getKey(r.invoke().result.item)

        override val prefix: String
            get() = IFDRecipes.DRYING.type.id.path

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}
