package cn.solarmoon.idyllic_food_diary.data

import cn.solarmoon.idyllic_food_diary.feature.fluid_effect.FluidEffect
import com.mojang.serialization.JsonOps
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import java.util.concurrent.CompletableFuture

open class FluidEffectProvider(
    private val packOutput: PackOutput
): DataProvider {

    private val fluidEffects = mutableMapOf<Fluid, FluidEffect>()

    open fun addOutput() {
        add(Fluids.LAVA, FluidEffect(FoodProperties.Builder().alwaysEdible().build(), fire = 200))
        add(Fluids.WATER, FluidEffect(FoodProperties.Builder().alwaysEdible().build(), extinguishing = true))
    }

    protected fun add(fluid: Fluid, effect: FluidEffect) {
        fluidEffects[fluid] = effect
    }

    override fun run(output: CachedOutput): CompletableFuture<*> {
        addOutput()
        val listOut = mutableListOf<CompletableFuture<*>>()
        fluidEffects.forEach {
            val id = BuiltInRegistries.FLUID.getKey(it.key)
            val modId = id.namespace
            val fluidName = id.path
            val path = packOutput.outputFolder.resolve("data/$modId/${FluidEffect.ID}/$fluidName.json")
            val json = FluidEffect.CODEC.encodeStart(JsonOps.INSTANCE, it.value).orThrow
            listOut.add(DataProvider.saveStable(output, json, path))
        }
        return CompletableFuture.allOf(*listOut.toTypedArray())
    }

    override fun getName(): String {
        return "FluidEffects"
    }

}