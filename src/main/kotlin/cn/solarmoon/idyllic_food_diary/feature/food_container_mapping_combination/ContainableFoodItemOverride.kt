package cn.solarmoon.idyllic_food_diary.feature.food_container_mapping_combination

import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.spark_core.api.renderer.model.CompositeBakedModel
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.block.model.ItemOverrides
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class ContainableFoodItemOverride: ItemOverrides() {

    override fun resolve(
        model: BakedModel,
        stack: ItemStack,
        level: ClientLevel?,
        entity: LivingEntity?,
        seed: Int
    ): BakedModel? {
        val origin = super.resolve(model, stack, level, entity, seed)
        origin?.run {
            stack.get(IFDDataComponents.FOOD_CONTAINER)?.let { container ->
                if (container.stack.isEmpty) return origin
                val containerModel = Minecraft.getInstance().itemRenderer.getModel(container.stack, level, entity, seed)
                return CompositeBakedModel(origin, containerModel)
            }
        }
        return origin
    }

}