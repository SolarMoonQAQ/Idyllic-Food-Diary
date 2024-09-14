package cn.solarmoon.idyllic_food_diary.feature.food_container_mapping_combination

import cn.solarmoon.idyllic_food_diary.element.matter.food_container.FoodContainerItem
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.block.model.ItemOverrides
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class FoodContainerItemOverride(private val pureModel: BakedModel): ItemOverrides() {

    override fun resolve(
        model: BakedModel,
        stack: ItemStack,
        level: ClientLevel?,
        entity: LivingEntity?,
        seed: Int
    ): BakedModel? {
        val origin = super.resolve(model, stack, level, entity, seed)
        if (level != null && FoodContainerItem.isPureTexture(stack, level)) return pureModel
        return origin
    }

}