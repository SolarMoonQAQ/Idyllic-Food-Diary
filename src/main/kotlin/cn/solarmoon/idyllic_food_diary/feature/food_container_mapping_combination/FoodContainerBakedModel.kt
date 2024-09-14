package cn.solarmoon.idyllic_food_diary.feature.food_container_mapping_combination

import net.minecraft.client.renderer.block.model.ItemOverrides
import net.minecraft.client.resources.model.BakedModel
import net.neoforged.neoforge.client.model.BakedModelWrapper

/**
 * 食物容器拥有可调用的方块模型和物品贴图
 */
class FoodContainerBakedModel(originalModel: BakedModel, private val pureModel: BakedModel): BakedModelWrapper<BakedModel>(originalModel) {

    override fun getOverrides(): ItemOverrides {
        return FoodContainerItemOverride(pureModel)
    }

}