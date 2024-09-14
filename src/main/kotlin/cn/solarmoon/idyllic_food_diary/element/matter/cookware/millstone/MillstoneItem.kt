package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone

import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlocks
import net.minecraft.world.item.BlockItem
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import java.util.function.Consumer

class MillstoneItem: BlockItem(IFDBlocks.MILLSTONE.get(), Properties().stacksTo(1)) {

}