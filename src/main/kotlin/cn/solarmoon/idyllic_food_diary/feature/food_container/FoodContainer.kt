package cn.solarmoon.idyllic_food_diary.feature.food_container

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack

data class FoodContainer(val stack: ItemStack) {

    companion object {
        @JvmStatic
        val EMPTY = FoodContainer(ItemStack.EMPTY)

        @JvmStatic
        val CODEC: Codec<FoodContainer> = RecordCodecBuilder.create {
            it.group(
                ItemStack.OPTIONAL_CODEC.fieldOf("container").forGetter { it.stack }
            ).apply(it, ::FoodContainer)
        }

        @JvmStatic
        val STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, FoodContainer::stack,
            ::FoodContainer
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is FoodContainer) {
            return false
        }
        val thatItem = other.stack
        return ItemStack.isSameItemSameComponents(stack, thatItem)
    }


    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + stack.hashCode()
        return result
    }

}
