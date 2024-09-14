package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp
import cn.solarmoon.idyllic_food_diary.feature.util.HeatSourceUtil
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.data.RecipeJsonBuilder
import cn.solarmoon.spark_core.api.data.SerializeHelper
import cn.solarmoon.spark_core.api.entry_builder.common.RecipeBuilder
import cn.solarmoon.spark_core.api.recipe.IConcreteRecipe
import cn.solarmoon.spark_core.api.recipe.processor.SingleTimeRecipeProcessor
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Direction
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
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import net.neoforged.neoforge.items.wrapper.RecipeWrapper


@JvmRecord
data class WaterBoilingRecipe(
    val input: Fluid,
    val time: Int
) : IConcreteRecipe {

    override val entry: RecipeBuilder.RecipeEntry<*>
        get() = IFDRecipes.WATER_BOILING

    companion object {
        const val BASE_FLUID_AMOUNT: Int = 1000
    }

    /**
     * 把烧水时间和当前要烧的水的容量绑定（实现动态烧水时间）
     */
    fun getActualTime(blockEntity: BlockEntity): Int {
        val level = blockEntity.level ?: return BASE_FLUID_AMOUNT
        val tank = level.getCapability(Capabilities.FluidHandler.BLOCK, blockEntity.blockPos, Direction.NORTH) ?: return BASE_FLUID_AMOUNT
        val scale = tank.getFluidInTank(0).amount.toDouble() / BASE_FLUID_AMOUNT
        return (time * scale).toInt()
    }

    class Serializer: RecipeSerializer<WaterBoilingRecipe> {
        override fun codec(): MapCodec<WaterBoilingRecipe> {
            return RecordCodecBuilder.mapCodec {
                it.group(
                    SerializeHelper.FLUID.CODEC.fieldOf("input").forGetter { it.input },
                    Codec.INT.fieldOf("time").forGetter { it.time }
                ).apply(it, ::WaterBoilingRecipe)
            }
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf?, WaterBoilingRecipe?> {
            return StreamCodec.composite(
                SerializeHelper.FLUID.STREAM_CODEC, WaterBoilingRecipe::input,
                ByteBufCodecs.INT, WaterBoilingRecipe::time,
                ::WaterBoilingRecipe
            )
        }
    }

    class Processor(be: BlockEntity, val tank: FluidTank) : SingleTimeRecipeProcessor<RecipeWrapper, WaterBoilingRecipe>(be) {

        override fun tryWork(): Boolean {
            findRecipe()?.let {
                time++
                recipeTime = it.value.getActualTime(be)
                if (time > recipeTime) {
                    tank.fluid.update(IFDDataComponents.TEMP, Temp.COMMON) { Temp.HOT }
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

        override fun isRecipeMatch(recipe: WaterBoilingRecipe): Boolean {
            val fluidStackIn = tank.fluid
            return fluidStackIn.fluid == recipe.input && HeatSourceUtil.isOnHeatSource(be) && fluidStackIn.getOrDefault(IFDDataComponents.TEMP, Temp.COMMON) != Temp.HOT
        }

        /**
         * @return 是否正在烧水
         */
        fun isBoiling(): Boolean {
            return time > 0
        }

        /**
         * @return 是否已处于沸腾状态（容器内是热液体且下方为热源）
         */
        fun isInBoil(): Boolean {
            return tank.fluid.getOrDefault(IFDDataComponents.TEMP, Temp.COMMON) == Temp.HOT && HeatSourceUtil.isOnHeatSource(be)
        }

        override fun getRecipeType(): RecipeType<WaterBoilingRecipe> = IFDRecipes.WATER_BOILING.type.get()

    }

    class JsonBuilder(val r: () -> WaterBoilingRecipe): RecipeJsonBuilder() {
        override val name: ResourceLocation
            get() = BuiltInRegistries.FLUID.getKey(r.invoke().input)

        override val prefix: String
            get() = IFDRecipes.WATER_BOILING.type.id.path

        override fun getRecipe(recipeOutput: RecipeOutput, location: ResourceLocation): Recipe<*> {
            return r.invoke()
        }
    }

}