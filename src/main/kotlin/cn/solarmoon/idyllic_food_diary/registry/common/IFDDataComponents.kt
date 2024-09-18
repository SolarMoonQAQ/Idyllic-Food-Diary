package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin.RollingContent
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp
import cn.solarmoon.idyllic_food_diary.feature.food_container.FoodContainer
import cn.solarmoon.idyllic_food_diary.feature.optinal_recipe.RecipeSelectData
import com.mojang.serialization.Codec
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents

object IFDDataComponents {

    @JvmStatic
    fun register() {}

    @JvmStatic
    val TEMP = IdyllicFoodDiary.REGISTER.dataComponent<Temp>()
        .id("temp")
        .build {
            it.persistent(Temp.CODEC)
                .networkSynchronized(Temp.STREAM_CODEC)
                .cacheEncoding()
        }

    @JvmStatic
    val FOOD_CONTAINER = IdyllicFoodDiary.REGISTER.dataComponent<FoodContainer>()
        .id("food_container")
        .build {
            it.persistent(FoodContainer.CODEC)
                .networkSynchronized(FoodContainer.STREAM_CODEC)
                .cacheEncoding()
        }

    @JvmStatic
    val ROLLING_CONTENT = IdyllicFoodDiary.REGISTER.dataComponent<RollingContent>()
        .id("rolling_content")
        .build {
            it.persistent(RollingContent.CODEC)
                .networkSynchronized(RollingContent.STREAM_CODEC)
                .cacheEncoding()
        }

    @JvmStatic
    val RECIPE_SELECTION = IdyllicFoodDiary.REGISTER.dataComponent<RecipeSelectData>()
        .id("recipe_selection")
        .build {
            it.persistent(RecipeSelectData.CODEC)
                .networkSynchronized(RecipeSelectData.STREAM_CODEC)
                .cacheEncoding()
        }

    /**
     * 保存在item上的食物剩余交互次数，尽量避免保存0次
     */
    @JvmStatic
    val INTERACTION = IdyllicFoodDiary.REGISTER.dataComponent<Int>()
        .id("interaction")
        .build {
            it.persistent(Codec.INT)
                .networkSynchronized(ByteBufCodecs.INT)
                .cacheEncoding()
        }

    @JvmStatic
    val FLUID_INTERACT_VALUE = IdyllicFoodDiary.REGISTER.dataComponent<Int>()
        .id("fluid_interact_value")
        .build {
            it.persistent(Codec.INT)
                .networkSynchronized(ByteBufCodecs.INT)
                .cacheEncoding()
        }


}