package cn.solarmoon.idyllic_food_diary.registry.client

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockItem
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.FoodContainerItem
import cn.solarmoon.idyllic_food_diary.feature.food_container_mapping_combination.ContainableFoodBakedModel
import cn.solarmoon.idyllic_food_diary.feature.food_container_mapping_combination.FoodContainerBakedModel
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import cn.solarmoon.spark_core.api.renderer.model.PerspectiveBakedModel
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.event.ModelEvent
import net.neoforged.neoforge.registries.RegisterEvent

object IFDBakedModelOverrides {

    val PERSPECTIVE_MODEL_LIST = mutableListOf<Pair<ModelResourceLocation, ModelResourceLocation>>()
    val CONTAINER_PURE_MODEL_LIST = mutableListOf<Pair<ModelResourceLocation, ModelResourceLocation>>()

    private fun reg(event: RegisterEvent) {
        if (event.registryKey.equals(Registries.ITEM)) {
            addInHandModel(IFDItems.ROLLING_PIN.get())
            addInHandModel(IFDItems.CLEAVER.get())
            IdyllicFoodDiary.REGISTER.itemDeferredRegister.entries.filter { it.value() is FoodContainerItem }.map { it.value() }.forEach(::addPureContainerModel)
        }
    }

    private fun onBaked(event: ModelEvent.BakingCompleted) {
        val models = event.modelBakery.bakedTopLevelModels
        PERSPECTIVE_MODEL_LIST.forEach {
            val newModel = PerspectiveBakedModel(models[it.first]!!, models[it.second]!!)
            models[it.first] = newModel
        }
        CONTAINER_PURE_MODEL_LIST.forEach {
            val newModel = FoodContainerBakedModel(models[it.first]!!, models[it.second]!!)
            models[it.first] = newModel
        }
        // 对于可被容器盛出的食物方块，将混合容器贴图到本体物品贴图下方
        IdyllicFoodDiary.REGISTER.itemDeferredRegister.entries.filter { it.value() is FoodBlockItem }.map { it.value() }.forEach {
            val res = ModelResourceLocation.inventory(BuiltInRegistries.ITEM.getKey(it))
            val newModel = ContainableFoodBakedModel(models[res]!!)
            models[res] = newModel
        }
    }

    private fun registerModels(event: ModelEvent.RegisterAdditional) {
        PERSPECTIVE_MODEL_LIST.forEach { event.register(it.second) }
        CONTAINER_PURE_MODEL_LIST.forEach { event.register(it.second) }
    }

    fun addInHandModel(item: Item) {
        val res = BuiltInRegistries.ITEM.getKey(item)
        val rawName = ModelResourceLocation.inventory(res)
        val inHandName = ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(res.namespace, "item/${res.path}_in_hand"))
        PERSPECTIVE_MODEL_LIST.add(Pair(rawName, inHandName))
    }

    fun addPureContainerModel(item: Item) {
        val res = BuiltInRegistries.ITEM.getKey(item)
        val rawName = ModelResourceLocation.inventory(res)
        val inHandName = ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(res.namespace, "item/${res.path}_pure"))
        CONTAINER_PURE_MODEL_LIST.add(Pair(rawName, inHandName))
    }

    @JvmStatic
    fun register(bus: IEventBus) {
        bus.addListener(::onBaked)
        bus.addListener(::registerModels)
        bus.addListener(::reg)
    }

}