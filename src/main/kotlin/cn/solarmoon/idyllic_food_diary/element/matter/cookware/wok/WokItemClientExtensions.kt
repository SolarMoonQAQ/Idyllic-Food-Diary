package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok

import cn.solarmoon.idyllic_food_diary.feature.hug_item.IHuggableItemExtensions
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

class WokItemClientExtensions: IHuggableItemExtensions {

    override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
        return WokItemRenderer()
    }

}