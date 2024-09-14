package cn.solarmoon.idyllic_food_diary.feature.optinal_recipe

import cn.solarmoon.spark_core.api.data.SerializeHelper
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.codec.StreamDecoder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

data class RecipeSelectData(private val selection: MutableMap<Block, Int>) {

    fun getIndex(input: Block): Int {
        return selection.getOrDefault(input, 0)
    }

    fun putIndexAndCopy(block: Block, index: Int): RecipeSelectData {
        selection[block] = index
        return copy(selection)
    }

    companion object {
        @JvmStatic
        val EMPTY = RecipeSelectData(mutableMapOf())

        @JvmStatic
        val CODEC: Codec<RecipeSelectData> = RecordCodecBuilder.create {
            it.group(
                Codec.unboundedMap(SerializeHelper.BLOCK.CODEC, Codec.INT).xmap( { it.toMutableMap() }, { it } ).fieldOf("selection").forGetter { it.selection }
            ).apply(it, ::RecipeSelectData)
        }

        @JvmStatic
        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map({ mutableMapOf() }, SerializeHelper.BLOCK.STREAM_CODEC, ByteBufCodecs.INT), RecipeSelectData::selection,
            ::RecipeSelectData
        )
    }

}
