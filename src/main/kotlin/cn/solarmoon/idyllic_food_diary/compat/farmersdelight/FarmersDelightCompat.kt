package cn.solarmoon.idyllic_food_diary.compat.farmersdelight

import net.neoforged.fml.ModList

class FarmersDelightCompat {



    companion object {
        const val MOD_ID = "farmersdelight"

        @JvmStatic
        fun isLoaded(): Boolean = ModList.get().isLoaded(MOD_ID)
    }

}