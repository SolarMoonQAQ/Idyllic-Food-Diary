package cn.solarmoon.idyllic_food_diary.feature.util

import cn.solarmoon.idyllic_food_diary.compat.farmersdelight.FarmersUtil
import cn.solarmoon.idyllic_food_diary.data.IFDBlockTags
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.IBuiltInStove
import cn.solarmoon.spark_core.api.blockstate.ILitState
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

object HeatSourceUtil {

    /**
     * 检测是否在热源上，并且包含了炉灶的情况，因此这个方法只适用于检测方块实体的热源情况
     */
    @JvmStatic
    fun isOnHeatSource(be: BlockEntity): Boolean {
        val level = be.level
        val pos = be.blockPos
        val state = be.blockState
        val block = state.block
        if (block is IBuiltInStove && block.isNestedInStove(state)) {
            return state.getValue(ILitState.LIT)
        }
        return level != null && isHeatSource(level.getBlockState(pos.below()))
    }

    /**
     * 检查方块自身是否为热源
     */
    @JvmStatic
    fun isHeatSource(state: BlockState): Boolean {
        val commonFlag = state.`is`(IFDBlockTags.HEAT_SOURCE) || FarmersUtil.isHeatSource(state)
        if (state.hasProperty(ILitState.LIT)) {
            return commonFlag && state.getValue(ILitState.LIT)
        }
        return commonFlag
    }

}