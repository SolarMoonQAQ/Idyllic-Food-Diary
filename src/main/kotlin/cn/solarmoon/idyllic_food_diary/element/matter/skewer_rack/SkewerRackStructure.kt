package cn.solarmoon.idyllic_food_diary.element.matter.skewer_rack

import cn.solarmoon.idyllic_food_diary.element.matter.skewer_rack.SkewerRackBlockEntity.Companion.ANIM_ROTATION
import cn.solarmoon.spark_core.registry.common.SparkAttachments

data class SkewerRackStructure(
    val leftTerminal: SkewerRackBlockEntity,
    val rightTerminal: SkewerRackBlockEntity,
    val allRack: List<SkewerRackBlockEntity>
) {

    fun roll() {
        val anim = leftTerminal.getData(SparkAttachments.ANIMTICKER)
        val timer = anim.timers[ANIM_ROTATION]!!
        timer.start()
    }

    /**
     * 同步整体的旋转角
     */
    fun syncRot() {
        allRack.forEach {
            if (it.blockPos != leftTerminal.blockPos) {
                val mainAnim = leftTerminal.getData(SparkAttachments.ANIMTICKER)
                val anim = it.getData(SparkAttachments.ANIMTICKER)
                anim.timers[ANIM_ROTATION] = mainAnim.timers[ANIM_ROTATION]!!.copy()
                anim.fixedValues.clear()
                anim.fixedValues.putAll(mainAnim.fixedValues)
            }
        }
    }

}
