package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup

import cn.solarmoon.spark_core.api.cap.fluid.TileTank
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

class CupTank(be: BlockEntity, capacity: Int): TileTank(be, capacity) {
    public override fun onContentsChanged() {
        super.onContentsChanged()
    }
}