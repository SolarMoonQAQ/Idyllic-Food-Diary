package cn.solarmoon.idyllic_food_diary.element.matter.food

import cn.solarmoon.idyllic_food_diary.feature.food_container.FoodContainer
import cn.solarmoon.idyllic_food_diary.registry.common.IFDAttachments
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.spark_core.api.blockentity.SyncedBlockEntity
import cn.solarmoon.spark_core.api.util.BlockUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class FoodBlockEntity(pos: BlockPos, blockState: BlockState): SyncedBlockEntity(IFDBlockEntities.FOOD.value(), pos, blockState) {

    val containerBlockState: BlockState
        get() {
            val container = components().getOrDefault(IFDDataComponents.FOOD_CONTAINER.get(), FoodContainer.EMPTY)
            val block = Block.byItem(container.stack.item)
            return BlockUtil.inheritBlockWithAllState(blockState, block.defaultBlockState())
        }

    val stage
        get() = blockState.getValue(FoodEntityBlock.INTERACTION)

    /**
     * 消耗一层交互阶段，并保存当前阶段到dataComponent以便传给物品或凋落物
     */
    fun consumeInteraction(destroy: Boolean) {
        val level = level ?: return
        val targetState = blockState.setValue(FoodEntityBlock.INTERACTION, stage - 1)
        if (destroy) level.destroyBlock(blockPos, false)
        level.setBlock(blockPos, targetState, 3)
        setComponents(DataComponentMap.builder().addAll(components()).set(IFDDataComponents.INTERACTION, stage).build())
        if (targetState.getValue(FoodEntityBlock.INTERACTION) == 0) {
            BlockUtil.replaceBlockWithAllState(blockState, containerBlockState, level, blockPos)
        }
    }

}