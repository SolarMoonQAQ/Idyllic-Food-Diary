package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp
import cn.solarmoon.idyllic_food_diary.feature.util.HeatSourceUtil
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
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.wrapper.RecipeWrapper

@JvmRecord
data class FoodBoilingRecipe(
    val ingredient: Ingredient,
    val fluidConsumption: FluidStack,
    val temp: Temp,
    val time: Int,
    val result: ItemStack
): IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.FOOD_BOILING

    class Serializer : RecipeSerializer<FoodBoilingRecipe> {
        override fun codec(): MapCodec<FoodBoilingRecipe> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter { it.ingredient },
                    FluidStack.OPTIONAL_CODEC.fieldOf("fluid_consumption").forGetter { it.fluidConsumption },
                    Temp.CODEC.fieldOf("temp").forGetter { it.temp },
                    Codec.INT.fieldOf("time").forGetter { it.time },
                    ItemStack.OPTIONAL_CODEC.fieldOf("result").forGetter { it.result }
                ).apply(it, ::FoodBoilingRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, FoodBoilingRecipe> {
            return StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, FoodBoilingRecipe::ingredient,
                FluidStack.OPTIONAL_STREAM_CODEC, FoodBoilingRecipe::fluidConsumption,
                Temp.STREAM_CODEC, FoodBoilingRecipe::temp,
                ByteBufCodecs.INT, FoodBoilingRecipe::time,
                ItemStack.OPTIONAL_STREAM_CODEC, FoodBoilingRecipe::result,
                ::FoodBoilingRecipe
            )
        }
    }

    class Processor(be: BlockEntity, val inv: ItemStackHandler, val tank: FluidTank): MultiTimeRecipeProcessor<RecipeWrapper, FoodBoilingRecipe>(be) {

        override fun isRecipeMatch(recipe: FoodBoilingRecipe, index: Int): Boolean {
            val stack = inv.getStackInSlot(index)
            return recipe.ingredient.test(stack)
                    && tank.fluid.fluid === recipe.fluidConsumption.fluid
                    && tank.fluid.amount >= recipe.fluidConsumption.amount
                    && Temp.isSame(tank.fluid, recipe.temp)
                    && HeatSourceUtil.isOnHeatSource(be)
        }

        override fun tryWork(): Boolean {
            var consumptionSum = 0
            val level: Level = be.level ?: return false
            if (level.isClientSide) return false
            for (i in 0 until inv.slots) {
                findRecipe(i)?.let {
                    consumptionSum += it.value.fluidConsumption.amount // 保证同时存在多个配方时液体总量要大于所有配方所需的消耗量
                    if (tank.fluidAmount >= consumptionSum) {
                        times[i]++
                        recipeTimes[i] = it.value.time
                        if (times[i] > recipeTimes[i]) {
                            inv.setStackInSlot(i, it.value.result.copy())
                            tank.fluid.shrink(it.value.fluidConsumption.amount)
                            be.setChanged()
                        }
                    } else {
                        times[i] = 0
                        recipeTimes[i] = 0
                    }
                } ?: let {
                    times[i] = 0
                    recipeTimes[i] = 0
                }
            }
            return isWorking()
        }

        override fun getRecipeType(): RecipeType<FoodBoilingRecipe> = IFDRecipes.FOOD_BOILING.type.get()

    }

    class JsonBuilder(val r: () -> FoodBoilingRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.ITEM.getKey(r.invoke().result.item)

        override val prefix: String
            get() = IFDRecipes.FOOD_BOILING.type.id.path

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}