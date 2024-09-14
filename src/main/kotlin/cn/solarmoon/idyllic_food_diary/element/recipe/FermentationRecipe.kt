package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.wrapper.RecipeWrapper
import java.util.function.Consumer
import java.util.stream.IntStream

@JvmRecord
data class FermentationRecipe(
    val ingredients: List<ProportionalIngredient>,
    val inputFluid: FluidStack,
    val temp: Temp,
    val time: Int,
    val results: List<ItemStack>,
    val outputFluid: FluidStack
) : IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.FERMENTATION

    class Serializer : RecipeSerializer<FermentationRecipe> {
        override fun codec(): MapCodec<FermentationRecipe> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    ProportionalIngredient.LIST_CODEC.fieldOf("proportional_ingredients").forGetter { it.ingredients },
                    FluidStack.OPTIONAL_CODEC.fieldOf("input_fluid").forGetter { it.inputFluid },
                    Temp.CODEC.fieldOf("temp").forGetter { it.temp },
                    Codec.INT.fieldOf("time").forGetter { it.time },
                    ItemStack.OPTIONAL_CODEC.listOf().fieldOf("results").forGetter { it.results },
                    FluidStack.OPTIONAL_CODEC.fieldOf("output_fluid").forGetter { it.outputFluid }
                ).apply(it, ::FermentationRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, FermentationRecipe> {
            return StreamCodec.composite(
                ProportionalIngredient.LIST_STREAM_CODEC, FermentationRecipe::ingredients,
                FluidStack.OPTIONAL_STREAM_CODEC, FermentationRecipe::inputFluid,
                Temp.STREAM_CODEC, FermentationRecipe::temp,
                ByteBufCodecs.INT, FermentationRecipe::time,
                ItemStack.OPTIONAL_LIST_STREAM_CODEC, FermentationRecipe::results,
                FluidStack.OPTIONAL_STREAM_CODEC, FermentationRecipe::outputFluid,
                ::FermentationRecipe
            )
        }
    }

    class Processor(be: BlockEntity, val inv: ItemStackHandler, val tank: FluidTank): SingleTimeRecipeProcessor<RecipeWrapper, FermentationRecipe>(be) {

        override fun isRecipeMatch(recipe: FermentationRecipe): Boolean {
            /*
             * 要求：
             * 输入物按比例完全匹配
             * 输入液体及量及温度完全匹配
             * 输出物有足够空间输出
             */
            val stacks = ItemStackHandlerHelper.getStacks(inv)
            val match = ProportionalIngredient.findMatch(stacks, recipe.ingredients)
            val fluidStack = tank.fluid
            val scale = match.second // 放大所需液体比例
            val fluidNeed = recipe.inputFluid.copy()
            fluidNeed.amount *= scale
            val resultFluidNeed = recipe.outputFluid.amount * scale
            val resultStackNeed = recipe.results.sumOf { it.count * scale }
            return match.first
                    && FluidHandlerHelper.isMatch(fluidStack, fluidNeed, true, false)
                    && Temp.isSame(fluidStack, recipe.temp)
                    && tank.capacity >= resultFluidNeed && inv.slots >= resultStackNeed
        }

        override fun tryWork(): Boolean {
            findRecipe()?.let {
                // 按输入比例增加产物比例和所需时间比例
                val scale = ProportionalIngredient.findMatch(ItemStackHandlerHelper.getStacks(inv), it.value.ingredients).second
                time++
                recipeTime = it.value.time * scale
                if (time >= recipeTime) {
                    val result = it.value.outputFluid.copy()
                    result.amount *= scale
                    val results = it.value.results
                    ItemStackHandlerHelper.clearInv(inv)
                    // 按比例多次插入匹配结果
                    results.forEach(Consumer { stack: ItemStack ->
                        IntStream.range(0, scale).forEach { i: Int ->
                            for (n in 0 until stack.count) {
                                ItemStackHandlerHelper.insertItem(inv, stack.copyWithCount(1))
                            }
                        }
                    })
                    result.update(IFDDataComponents.TEMP, Temp.COMMON) { tank.fluid.get(IFDDataComponents.TEMP) }
                    tank.fluid = result
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

        override fun getRecipeType(): RecipeType<FermentationRecipe> = IFDRecipes.FERMENTATION.type.get()

    }

    class JsonBuilder(val isNameByFluid: Boolean, val r: () -> FermentationRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = if (isNameByFluid) BuiltInRegistries.FLUID.getKey(r.invoke().outputFluid.fluid) else BuiltInRegistries.ITEM.getKey(r.invoke().results[0].item)

        override val prefix: String
            get() = IFDRecipes.FERMENTATION.type.id.path

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}