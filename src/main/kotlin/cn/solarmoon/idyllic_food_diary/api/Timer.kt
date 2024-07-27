package cn.solarmoon.idyllic_food_diary.api

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.common.util.INBTSerializable

/**
 * 计时器，使用start()/stop()进行开关<br>
 * 自行调用帧tick
 */
class Timer: INBTSerializable<CompoundTag> {

    var timing = false
    var time = 0f
    var maxTime = 100f
    var onStartAction = {}
    var onStopAction = {}

    fun start() {
        timing = true
        time = 0f
        onStartAction.invoke()
    }

    fun stop() {
        timing = false
    }

    /**
     * 每tick增加指定计时器值直到最大值停止
     */
    fun tick() {
        if (!timing) return
        if (time < maxTime) {
            time++
        } else {
            onStopAction.invoke()
            stop()
        }
    }

    /**
     * 获取当前进度
     */
    fun getProgress(): Float = getProgress(0f)

    /**
     * 当前进度，但是输入了一个值（一般是帧时间）作为过渡，更为平滑
     */
    fun getProgress(partialTicks: Float): Float = (time + partialTicks) / maxTime

    override fun serializeNBT(): CompoundTag {
        val tag = CompoundTag()
        tag.putBoolean("Timing", timing)
        tag.putFloat("Time", time)
        tag.putFloat("MaxTime", maxTime)
        return tag
    }

    override fun deserializeNBT(tag: CompoundTag) {
        timing = tag.getBoolean("Timing")
        time = tag.getFloat("Time")
        maxTime = tag.getFloat("MaxTime")
    }

}