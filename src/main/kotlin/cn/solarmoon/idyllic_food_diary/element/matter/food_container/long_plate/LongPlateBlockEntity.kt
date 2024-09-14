package cn.solarmoon.idyllic_food_diary.element.matter.food_container.long_plate

import cn.solarmoon.idyllic_food_diary.element.matter.food_container.FoodContainerBlockEntity
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class LongPlateBlockEntity(pos: BlockPos, state: BlockState): FoodContainerBlockEntity(IFDBlockEntities.LONG_PLATE.get(), 15, pos, state) {
}