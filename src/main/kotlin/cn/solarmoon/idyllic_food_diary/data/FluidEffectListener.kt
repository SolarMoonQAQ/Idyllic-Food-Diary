package cn.solarmoon.idyllic_food_diary.data

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.feature.fluid_effect.FluidEffect
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller

class FluidEffectListener: SimpleJsonResourceReloadListener(Gson().newBuilder().create(), FluidEffect.ID) {
    override fun apply(
        instance: Map<ResourceLocation, JsonElement>,
        resourceManager: ResourceManager,
        profiler: ProfilerFiller
    ) {
        instance.forEach { id, jsonElement ->
            val fluid = BuiltInRegistries.FLUID.get(id)
            val effect = FluidEffect.CODEC.decode(JsonOps.INSTANCE, jsonElement).orThrow.first
            FluidEffect.ALL_BOUNDS[fluid] = effect
        }
        IdyllicFoodDiary.LOGGER.info("成功加载 ${FluidEffect.ALL_BOUNDS.size} 个液体效果。")
    }
}