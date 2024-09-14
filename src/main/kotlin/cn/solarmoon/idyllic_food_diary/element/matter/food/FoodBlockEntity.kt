package cn.solarmoon.idyllic_food_diary.element.matter.food

import cn.solarmoon.idyllic_food_diary.registry.common.IFDAttachments
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.spark_core.api.blockentity.SyncedBlockEntity
import cn.solarmoon.spark_core.api.util.BlockUtil
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class FoodBlockEntity(pos: BlockPos, blockState: BlockState): SyncedBlockEntity(IFDBlockEntities.FOOD.value(), pos, blockState) {

    val containerBlockState: BlockState
        get() {
            val container = getData(IFDAttachments.FOOD_CONTAINER)
            val block = Block.byItem(container.stack.item)
            return BlockUtil.inheritBlockWithAllState(blockState, block.defaultBlockState())
        }

}