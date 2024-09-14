package cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation

data class RollingContent(
    val targetPos: BlockPos,
    val targetRecipeId: ResourceLocation
) {

    companion object {
        @JvmStatic
        val EMPTY = RollingContent(BlockPos.ZERO, ResourceLocation.withDefaultNamespace("air"))

        @JvmStatic
        val CODEC: Codec<RollingContent> = RecordCodecBuilder.create {
            it.group(
                BlockPos.CODEC.fieldOf("pos").forGetter { it.targetPos },
                ResourceLocation.CODEC.fieldOf("recipe").forGetter { it.targetRecipeId }
            ).apply(it, ::RollingContent)
        }

        @JvmStatic
        val STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RollingContent::targetPos,
            ResourceLocation.STREAM_CODEC, RollingContent::targetRecipeId,
            ::RollingContent
        )
    }

}