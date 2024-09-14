package cn.solarmoon.idyllic_food_diary.feature.food_container_mapping_combination

import net.minecraft.client.renderer.block.model.ItemOverrides
import net.minecraft.client.resources.model.BakedModel
import net.neoforged.neoforge.client.model.BakedModelWrapper

/**
 * 食物的物品贴图将合并其中的容器贴图
 */
class ContainableFoodBakedModel(originalModel: BakedModel): BakedModelWrapper<BakedModel>(originalModel) {

    override fun getOverrides(): ItemOverrides {
        return ContainableFoodItemOverride()
    }

}