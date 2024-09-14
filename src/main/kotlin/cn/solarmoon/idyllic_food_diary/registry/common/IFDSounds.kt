package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary

object IFDSounds {

    @JvmStatic
    fun register() {}

    @JvmStatic
    val STIR_FRY = IdyllicFoodDiary.REGISTER.sound()
        .id("block.stir_fry")
        .build()

    @JvmStatic
    val STIR_SIZZLE = IdyllicFoodDiary.REGISTER.sound()
        .id("block.stir_sizzle")
        .build()

    @JvmStatic
    val WOODEN_FISH = IdyllicFoodDiary.REGISTER.sound()
        .id("gui.wooden_fish")
        .build()

    @JvmStatic
    val CHOPPING = IdyllicFoodDiary.REGISTER.sound()
        .id("player.chopping")
        .build()

}