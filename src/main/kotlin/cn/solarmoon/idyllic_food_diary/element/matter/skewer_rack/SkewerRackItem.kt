package cn.solarmoon.idyllic_food_diary.element.matter.skewer_rack

import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlocks
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item.Properties

class SkewerRackItem: BlockItem(IFDBlocks.SKEWER_RACK.get(), Properties().stacksTo(1)) {
}