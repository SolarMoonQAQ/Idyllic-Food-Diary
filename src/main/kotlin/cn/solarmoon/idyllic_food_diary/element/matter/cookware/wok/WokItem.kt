package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok

import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlocks
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block

class WokItem: BlockItem(IFDBlocks.WOK.get(), Properties().stacksTo(1)) {
}