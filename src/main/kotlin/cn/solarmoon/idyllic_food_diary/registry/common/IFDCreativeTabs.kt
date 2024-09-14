package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.spark_core.api.entry_builder.common.CreativeTabBuilder
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object IFDCreativeTabs {

    @JvmStatic
    fun register() {}

    @JvmStatic
    val MAIN = IdyllicFoodDiary.REGISTER.creativeTab()
        .id(IdyllicFoodDiary.MOD_ID)
        .bound(CreativeModeTab.builder()
            .title(IdyllicFoodDiary.TRANSLATOR.set("creative_mode_tab", "main"))
            .icon { ItemStack(IFDItems.WOK) }
            .displayItems { params, output ->
                var list = IdyllicFoodDiary.REGISTER.itemDeferredRegister.getEntries().map { it.get() }.toMutableList()
                list.forEach(output::accept)
            }
        )
        .build()

}