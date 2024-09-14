package cn.solarmoon.idyllic_food_diary.element.recipe

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
import cn.solarmoon.spark_core.feature.inlay.AttributeForgingRecipe
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.RecipeWrapper

@JvmRecord
data class BrewingRecipe(
    val ingredients: List<ProportionalIngredient>,
    val inputFluid: FluidStack,
    val temp: Temp,
    val time: Int,
    val outputFluid: FluidStack,
    val needHeating: Boolean // 是否需要持续加热（成煲汤了）
): IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.BREWING

    class Serializer: RecipeSerializer<BrewingRecipe> {
        override fun codec(): MapCodec<BrewingRecipe> {
            return RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    ProportionalIngredient.LIST_CODEC.fieldOf("proportional_ingredients").forGetter { it.ingredients },
                    FluidStack.OPTIONAL_CODEC.fieldOf("input_fluid").forGetter { it.inputFluid },
                    Temp.CODEC.fieldOf("temp").forGetter { it.temp },
                    Codec.INT.fieldOf("time").forGetter { it.time },
                    FluidStack.OPTIONAL_CODEC.fieldOf("output_fluid").forGetter { it.outputFluid },
                    Codec.BOOL.fieldOf("need_heating").forGetter { it.needHeating }
                ).apply(instance, ::BrewingRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, BrewingRecipe> {
            return StreamCodec.composite(
                ProportionalIngredient.LIST_STREAM_CODEC, BrewingRecipe::ingredients,
                FluidStack.OPTIONAL_STREAM_CODEC, BrewingRecipe::inputFluid,
                Temp.STREAM_CODEC, BrewingRecipe::temp,
                ByteBufCodecs.INT, BrewingRecipe::time,
                FluidStack.OPTIONAL_STREAM_CODEC, BrewingRecipe::outputFluid,
                ByteBufCodecs.BOOL, BrewingRecipe::needHeating,
                ::BrewingRecipe
            )
        }
    }

    class Processor(be: BlockEntity, val inventory: IItemHandler, val tank: FluidTank) : SingleTimeRecipeProcessor<RecipeWrapper, BrewingRecipe>(be) {

        override fun tryWork(): Boolean {
            findRecipe()?.let {
                val scale = ProportionalIngredient.findMatch(ItemStackHandlerHelper.getStacks(inventory), it.value.ingredients).second
                time++
                recipeTime = it.value.time * scale
                if (time > recipeTime) {
                    ItemStackHandlerHelper.clearInv(inventory)
                    val result = it.value.outputFluid.copy()
                    result.amount *= scale
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
                return false
            }
        }

        override fun isRecipeMatch(recipe: BrewingRecipe): Boolean {
            val find = ProportionalIngredient.findMatch(ItemStackHandlerHelper.getStacks(inventory), recipe.ingredients)
            val fluidNeed = recipe.inputFluid.copy()
            fluidNeed.amount *= find.second
            val itemMatch = find.first
            val fluidMatch = FluidHandlerHelper.isMatch(tank.fluid, fluidNeed, true, false)
            val enoughSpace = tank.capacity >= recipe.outputFluid.amount * find.second
            val meetHeating = !recipe.needHeating || HeatSourceUtil.isOnHeatSource(be)
            return itemMatch && fluidMatch && enoughSpace && meetHeating
        }

        override fun getRecipeType(): RecipeType<BrewingRecipe> = IFDRecipes.BREWING.type.value()

    }

    class JsonBuilder(val r: () -> BrewingRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.FLUID.getKey(r.invoke().outputFluid.fluid)

        override val prefix: String
            get() = "${IFDRecipes.CHOPPING.type.id.path}"

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}
