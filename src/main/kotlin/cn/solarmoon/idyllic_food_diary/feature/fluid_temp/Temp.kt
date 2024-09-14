package cn.solarmoon.idyllic_food_diary.feature.fluid_temp

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.neoforged.neoforge.fluids.FluidStack
import kotlin.math.abs

enum class Temp {
    HOT, COMMON, COLD, ANY; //Any是比较用的，不可用于任何实际内容中

    /**
     * @return 除了比较温度等级本身以外，如果任意一个为ANY，就可以接受任意温度等级通过
     */
    fun isSame(temp: Temp): Boolean = this == temp || temp == ANY || this == ANY

    fun getPrefix(): Component {
        return if (this == COMMON) Component.empty()
        else IdyllicFoodDiary.TRANSLATOR.set("fluid", "temp." + this.toString().lowercase())
    }

    /**
     * @return 根据温度动态调节变温概率，如冷水在寒冷群系不易变为常温，而在炎热群系则极易变为常温，热水同理
     */
    fun getMaxProbabilityFromBiome(biome: Biome): Int {
        val baseP = 50000
        val bTemp = biome.baseTemperature
        val differ = abs(bTemp - 1) * 5
        when (this) {
            COLD -> {
                if (bTemp <= 0.15) {
                    return (baseP * differ).toInt()
                } else if (bTemp >= 0.95) {
                    return (baseP / differ).toInt()
                }
            }
            HOT -> {
                if (bTemp >= 0.95) {
                    return (baseP * differ).toInt()
                } else if (bTemp <= 0.15) {
                    return (baseP / differ).toInt()
                }
            }
            else -> {
                return baseP
            }
        }
        return baseP
    }

    companion object {

        @JvmStatic
        val CODEC: Codec<Temp> = Codec.STRING.xmap({ Temp.valueOf(it.uppercase()) }, { it.toString().lowercase() })

        @JvmStatic
        val STREAM_CODEC = object : StreamCodec<RegistryFriendlyByteBuf, Temp> {
            override fun decode(buffer: RegistryFriendlyByteBuf): Temp {
                return buffer.readEnum(Temp::class.java)
            }

            override fun encode(buffer: RegistryFriendlyByteBuf, value: Temp) {
                buffer.writeEnum(value)
            }
        }

        @JvmStatic
        fun shrink(stack: FluidStack): Boolean {
            return if (!stack.isEmpty) {
                val temp = stack.get(IFDDataComponents.TEMP)
                when (temp) {
                    HOT -> {
                        stack.set(IFDDataComponents.TEMP, COMMON)
                        return true
                    }
                    COMMON -> {
                        stack.set(IFDDataComponents.TEMP, COLD)
                        return true
                    }
                    else -> return false
                }
            } else false
        }

        @JvmStatic
        fun grow(stack: FluidStack): Boolean {
            return if (!stack.isEmpty) {
                val temp = stack.get(IFDDataComponents.TEMP)
                when (temp) {
                    COLD -> {
                        stack.set(IFDDataComponents.TEMP, COMMON)
                        return true
                    }
                    COMMON -> {
                        stack.set(IFDDataComponents.TEMP, HOT)
                        return true
                    }
                    else -> return false
                }
            } else false
        }

        @JvmStatic
        fun tick(fluidStack: FluidStack, level: Level, pos: BlockPos): Boolean {
            val random = level.random;
            val biome = level.getBiome(pos).value()
            val temp = fluidStack.getOrDefault(IFDDataComponents.TEMP, COMMON)
            val probability = temp.getMaxProbabilityFromBiome(biome)
            when (temp) {
                COLD -> {
                    if (random.nextInt(probability) == 0) {
                        IdyllicFoodDiary.LOGGER.info("已变温")
                        grow(fluidStack)
                        return true
                    }
                }
                HOT -> {
                    if (random.nextInt(probability) == 0) {
                        IdyllicFoodDiary.LOGGER.info("已变温")
                        shrink(fluidStack)
                        return true
                    }
                }
                else -> return false
            }
            return false
        }

        /**
         * 尽量使用此方法比较，会通过any类型的温度
         */
        @JvmStatic
        fun isSame(stack: FluidStack, temp: Temp): Boolean {
            return temp.isSame(stack.getOrDefault(IFDDataComponents.TEMP, COMMON))
        }

    }

}