package cn.solarmoon.idyllic_food_diary.element.recipe.assistant

import cn.solarmoon.spark_core.api.data.SerializeHelper
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class StirFryStage(
    val ingredients: List<Ingredient>,
    val fluidStack: FluidStack,
    val time: Int,
    val fryCount: Int,
    val keepFluid: Boolean
) {

    companion object {
        @JvmStatic
        val CODEC: Codec<StirFryStage> = RecordCodecBuilder.create { instance ->
            instance.group(
                Ingredient.LIST_CODEC.fieldOf("ingredients").forGetter { it.ingredients },
                FluidStack.OPTIONAL_CODEC.fieldOf("input_fluid").forGetter { it.fluidStack },
                Codec.INT.fieldOf("time").forGetter { it.time },
                Codec.INT.fieldOf("fry_count").forGetter { it.fryCount },
                Codec.BOOL.optionalFieldOf("keep_fluid", true).forGetter { it.keepFluid }
            ).apply(instance, ::StirFryStage)
        }

        @JvmStatic
        val LIST_CODEC = CODEC.listOf()

        @JvmStatic
        val STREAM_CODEC = object : StreamCodec<RegistryFriendlyByteBuf, StirFryStage> {
            override fun decode(buffer: RegistryFriendlyByteBuf): StirFryStage {
                val ingredients = SerializeHelper.INGREDIENT.LIST_STREAM_CODEC.decode(buffer)
                val inputFluid = FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer)
                val time = buffer.readInt()
                val fryCount = buffer.readInt()
                val keepFluid = buffer.readBoolean()
                return StirFryStage(ingredients, inputFluid, time, fryCount, keepFluid)
            }

            override fun encode(buffer: RegistryFriendlyByteBuf, stage: StirFryStage) {
                SerializeHelper.INGREDIENT.LIST_STREAM_CODEC.encode(buffer, stage.ingredients)
                FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, stage.fluidStack)
                buffer.writeInt(stage.time)
                buffer.writeInt(stage.fryCount)
                buffer.writeBoolean(stage.keepFluid)
            }
        }

        @JvmStatic
        val LIST_STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, List<StirFryStage>> = STREAM_CODEC.apply(ByteBufCodecs.collection { mutableListOf() })
    }

}