package cn.solarmoon.idyllic_food_diary.element.matter.food_container.plate

import cn.solarmoon.idyllic_food_diary.element.matter.food_container.FoodContainerBlockEntity
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class PlateBlockEntity(pos: BlockPos, state: BlockState): FoodContainerBlockEntity(IFDBlockEntities.PLATE.get(), 9, pos, state) {
}