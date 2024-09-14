package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.cleaver.CleaverItem
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneItem
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.SpatulaItem
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokItem
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockItem
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.feast.BeggarsChickenBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.FoodContainerItem
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.InlaidStoveBlock
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.InlaidStoveItem
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.StoveLidBlock
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.StoveLidItem
import cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin.RollingPinItem
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.cap.fluid.SyncedTileItemTank
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.world.item.component.CustomData
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidStack

object IFDItems {

    @JvmStatic
    fun register() {}

    @JvmStatic
    val INLAID_STOVE = IdyllicFoodDiary.REGISTER.item<InlaidStoveItem>()
        .id("inlaid_stove")
        .bound { InlaidStoveItem() }
        .build()

    @JvmStatic
    val STOVE_LID = IdyllicFoodDiary.REGISTER.item<StoveLidItem>()
        .id("stove_lid")
        .bound { StoveLidItem() }
        .build()

    @JvmStatic
    val MILLSTONE = IdyllicFoodDiary.REGISTER.item<MillstoneItem>()
        .id("millstone")
        .bound { MillstoneItem() }
        .build()

    @JvmStatic
    val WOK = IdyllicFoodDiary.REGISTER.item<WokItem>()
        .id("wok")
        .bound(::WokItem)
        .capability(Capabilities.FluidHandler.ITEM) { s, _ -> SyncedTileItemTank(s, 250) }
        .build()

    @JvmStatic
    val SPATULA = IdyllicFoodDiary.REGISTER.item<SpatulaItem>()
        .id("spatula")
        .bound(::SpatulaItem)
        .build()

    @JvmStatic
    val ROLLING_PIN = IdyllicFoodDiary.REGISTER.item<RollingPinItem>()
        .id("rolling_pin")
        .bound(::RollingPinItem)
        .build()

    @JvmStatic
    val CLEAVER = IdyllicFoodDiary.REGISTER.item<CleaverItem>()
        .id("cleaver")
        .bound(::CleaverItem)
        .build()

    @JvmStatic
    val WOODEN_PLATE = IdyllicFoodDiary.REGISTER.item<FoodContainerItem>()
        .id("wooden_plate")
        .bound { FoodContainerItem(IFDBlocks.WOODEN_PLATE.get()) }
        .build()

    @JvmStatic
    val PORCELAIN_PLATE = IdyllicFoodDiary.REGISTER.item<FoodContainerItem>()
        .id("porcelain_plate")
        .bound { FoodContainerItem(IFDBlocks.PORCELAIN_PLATE.get()) }
        .build()

    @JvmStatic
    val LONG_WOODEN_PLATE = IdyllicFoodDiary.REGISTER.item<FoodContainerItem>()
        .id("long_wooden_plate")
        .bound { FoodContainerItem(IFDBlocks.LONG_WOODEN_PLATE.get()) }
        .build()

    @JvmStatic
    val LONG_PORCELAIN_PLATE = IdyllicFoodDiary.REGISTER.item<FoodContainerItem>()
        .id("long_porcelain_plate")
        .bound { FoodContainerItem(IFDBlocks.LONG_PORCELAIN_PLATE.get()) }
        .build()

    @JvmStatic
    val BEGGARS_CHICKEN = IdyllicFoodDiary.REGISTER.item<FoodBlockItem>()
        .id("beggars_chicken")
        .bound { FoodBlockItem(IFDBlocks.BEGGARS_CHICKEN.get()) }
        .build()

}