package cn.solarmoon.idyllic_food_diary.api

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.common.util.INBTSerializable

/**
 * 动画计时器，能够存储各类动画信息<br>
 * 必须提醒的是，不可在客户端设置ticker的值，会导致各种各样的问题，最优是在服务端自上而下地让客户端接收数据
 */
class AnimTicker: INBTSerializable<CompoundTag> {

    var fixedValues = mutableMapOf<String, Float>().withDefault { 0f }
    var fixedTags = mutableMapOf<String, CompoundTag>().withDefault { CompoundTag() }
    var timer: Timer = Timer()

    override fun serializeNBT(): CompoundTag {
        val tag = CompoundTag()

        val tagList1 = ListTag()
        fixedValues.forEach { (name, value) ->
            val single = CompoundTag()
            single.putString("Name", name)
            single.putFloat("FixedValue", value)
            tagList1.add(single)
        }
        tag.put("FixedValues", tagList1)

        val tagList2 = ListTag()
        fixedTags.forEach { (name, value) ->
            val single = CompoundTag()
            single.putString("Name", name)
            single.put("FixedTag", value)
            tagList2.add(single)
        }
        tag.put("FixedTags", tagList2)

        tag.put("Timer", timer.serializeNBT())

        return tag
    }

    override fun deserializeNBT(tag: CompoundTag) {
        val listTag1 = tag.getList("FixedValues", ListTag.TAG_COMPOUND.toInt())
        for (i in listTag1.indices) {
            val t = listTag1.getCompound(i)
            val name = t.getString("Name")
            val value = t.getFloat("FixedValue")
            fixedValues[name] = value
        }

        val listTag2 = tag.getList("FixedTags", ListTag.TAG_COMPOUND.toInt())
        for (i in listTag2.indices) {
            val t = listTag2.getCompound(i)
            val name = t.getString("Name")
            val value = t.getCompound("FixedTag")
            fixedTags[name] = value
        }

        timer.deserializeNBT(tag.getCompound("Timer"))
    }

    companion object {
        @JvmStatic
        fun of(tag: CompoundTag): AnimTicker {
            val anim = AnimTicker()
            anim.deserializeNBT(tag)
            return anim
        }
    }

}