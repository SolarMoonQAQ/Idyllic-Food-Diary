package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.CupBlockEntity
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class LittleCupBlockEntity(pos: BlockPos, state: BlockState): CupBlockEntity(IFDBlockEntities.LITTLE_CUP.get(), 1, 250, pos, state) {
}