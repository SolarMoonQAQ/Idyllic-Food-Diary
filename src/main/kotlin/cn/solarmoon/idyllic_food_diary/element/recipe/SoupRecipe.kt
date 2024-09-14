package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.element.recipe.assistant.IExpGiver
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp
import cn.solarmoon.idyllic_food_diary.feature.util.HeatSourceUtil
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.data.RecipeJsonBuilder
import cn.solarmoon.spark_core.api.data.element.ProportionalIngredient
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
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.RecipeWrapper

@JvmRecord
data class SoupRecipe(
    val ingredients: List<ProportionalIngredient>,
    val inputFluid: FluidStack,
    val temp: Temp,
    val time: Int,
    val outputFluid: FluidStack,
    val exp: Int
) : IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.SOUP

    class Serializer : RecipeSerializer<SoupRecipe> {
        override fun codec(): MapCodec<SoupRecipe> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    ProportionalIngredient.LIST_CODEC.fieldOf("proportional_ingredients").forGetter { it.ingredients },
                    FluidStack.OPTIONAL_CODEC.fieldOf("input_fluid").forGetter { it.inputFluid },
                    Temp.CODEC.fieldOf("temp").forGetter { it.temp },
                    Codec.INT.fieldOf("time").forGetter { it.time },
                    FluidStack.OPTIONAL_CODEC.fieldOf("output_fluid").forGetter { it.outputFluid },
                    Codec.INT.fieldOf("exp").forGetter { it.exp }
                ).apply(it, ::SoupRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, SoupRecipe> {
            return StreamCodec.composite(
                ProportionalIngredient.LIST_STREAM_CODEC, SoupRecipe::ingredients,
                FluidStack.OPTIONAL_STREAM_CODEC, SoupRecipe::inputFluid,
                Temp.STREAM_CODEC, SoupRecipe::temp,
                ByteBufCodecs.INT, SoupRecipe::time,
                FluidStack.OPTIONAL_STREAM_CODEC, SoupRecipe::outputFluid,
                ByteBufCodecs.INT, SoupRecipe::exp,
                ::SoupRecipe
            )
        }
    }

    class Processor(be: BlockEntity, val inventory: IItemHandler, val tank: FluidTank) : SingleTimeRecipeProcessor<RecipeWrapper, SoupRecipe>(be),
        IExpGiver {

        override var exp = 0

        override fun tryWork(): Boolean {
            findRecipe()?.let {
                // 按输入比例增加产物比例和所需时间比例
                val scale = ProportionalIngredient.findMatch(ItemStackHandlerHelper.getStacks(inventory), it.value.ingredients).second
                time++
                recipeTime = it.value.time * scale
                if (time >= recipeTime) {
                    val result = it.value.outputFluid.copy()
                    result.amount *= scale
                    result.update(IFDDataComponents.TEMP, Temp.COMMON) { tank.fluid.get(IFDDataComponents.TEMP) }
                    tank.fluid = result
                    exp = it.value.exp
                    ItemStackHandlerHelper.clearInv(inventory)
                    time = 0
                    recipeTime = 0
                    be.setChanged()
                }
                return true
            } ?: let {
                time = 0
                recipeTime = 0
            }
            return false
        }

        override fun isRecipeMatch(recipe: SoupRecipe): Boolean {
            /*
             * 要求：
             * 输入物按比例完全匹配
             * 输入液体及量及温度完全匹配
             * 下方为热源
             * 有足够的输出空间
             */
            val stacks = ItemStackHandlerHelper.getStacks(inventory)
            val match = ProportionalIngredient.findMatch(stacks, recipe.ingredients)
            val fluidStack = tank.fluid
            val scale = match.second // 放大所需液体比例
            val fluidNeed = recipe.inputFluid.copy()
            fluidNeed.amount *= scale
            val enoughSpace = tank.capacity >= recipe.outputFluid.amount * match.second
            return (match.first
                    && FluidHandlerHelper.isMatch(fluidStack, fluidNeed, true, false)
                    && Temp.isSame(fluidStack, recipe.temp)
                    && HeatSourceUtil.isOnHeatSource(be)
                    && enoughSpace
                    )
        }

        override fun getRecipeType(): RecipeType<SoupRecipe> = IFDRecipes.SOUP.type.get()

    }

    class JsonBuilder(val r: () -> SoupRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.FLUID.getKey(r.invoke().outputFluid.fluid)

        override val prefix: String
            get() = IFDRecipes.SOUP.type.id.path

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}