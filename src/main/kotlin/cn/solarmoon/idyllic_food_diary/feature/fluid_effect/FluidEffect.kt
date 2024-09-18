package cn.solarmoon.idyllic_food_diary.feature.fluid_effect

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.level.material.Fluid

data class FluidEffect(
    val foodProperties: FoodProperties,
    val fire: Int = 0,
    val consumeAmount: Int = 50,
    val extinguishing: Boolean = false,
    /**
     * 清空所有药水效果，但是不会清空此条自身所给予的效果
     */
    val clearAllEffects: Boolean = false
) {

    companion object {
        const val ID = "fluid_effect"

        @JvmStatic
        val CODEC: Codec<FluidEffect> = RecordCodecBuilder.create {
            it.group(
                FoodProperties.DIRECT_CODEC.fieldOf("foodProperties").forGetter { it.foodProperties },
                Codec.INT.fieldOf("fire").forGetter { it.fire },
                Codec.INT.fieldOf("consume_amount").forGetter { it.consumeAmount },
                Codec.BOOL.fieldOf("extinguishing").forGetter { it.extinguishing },
                Codec.BOOL.fieldOf("clear_all_effects").forGetter { it.clearAllEffects }
            ).apply(it, ::FluidEffect)
        }

        @JvmStatic
        val ALL_BOUNDS = mutableMapOf<Fluid, FluidEffect>()

        @JvmStatic
        fun get(fluid: Fluid): FluidEffect? {
            return ALL_BOUNDS[fluid]
        }
    }

}
