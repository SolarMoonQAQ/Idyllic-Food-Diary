package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.cleaver.CleaverItem
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.CupItem
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.KettleBlock
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.KettleItem
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneItem
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerItem
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerLidBlock
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerLidItem
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.SpatulaItem
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokItem
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockItem
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodPropertyData
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodTier
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.feast.RoastedSucklingPigBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.FoodContainerItem
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.InlaidStoveItem
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.StoveLidItem
import cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin.RollingPinItem
import cn.solarmoon.idyllic_food_diary.element.matter.skewer_rack.SkewerRackBlock
import cn.solarmoon.idyllic_food_diary.element.matter.skewer_rack.SkewerRackItem
import cn.solarmoon.spark_core.registry.common.SparkDataComponents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.Properties
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack
import kotlin.jvm.JvmStatic

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
        .capability(Capabilities.FluidHandler.ITEM) { s, _ -> FluidHandlerItemStack(SparkDataComponents.SIMPLE_FLUID_CONTENT, s, 250) }
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
    val JADE_CHINA_CUP = IdyllicFoodDiary.REGISTER.item<CupItem>()
        .id("jade_china_cup")
        .bound { CupItem(IFDBlocks.JADE_CHINA_CUP.get(), Properties().stacksTo(1)) }
        .capability(Capabilities.FluidHandler.ITEM) { s, _ -> FluidHandlerItemStack(SparkDataComponents.SIMPLE_FLUID_CONTENT, s, 250) }
        .build()

    @JvmStatic
    val KETTLE = IdyllicFoodDiary.REGISTER.item<KettleItem>()
        .id("kettle")
        .bound { KettleItem(IFDBlocks.KETTLE.get(), Properties().stacksTo(1)) }
        .capability(Capabilities.FluidHandler.ITEM) { s, _ -> FluidHandlerItemStack(SparkDataComponents.SIMPLE_FLUID_CONTENT, s, 1000) }
        .build()

    @JvmStatic
    val STEAMER = IdyllicFoodDiary.REGISTER.item<SteamerItem>()
        .id("steamer")
        .bound(::SteamerItem)
        .build()

    @JvmStatic
    val STEAMER_LID = IdyllicFoodDiary.REGISTER.item<SteamerLidItem>()
        .id("steamer_lid")
        .bound(::SteamerLidItem)
        .build()

    @JvmStatic
    val SKEWER_RACK = IdyllicFoodDiary.REGISTER.item<SkewerRackItem>()
        .id("skewer_rack")
        .bound(::SkewerRackItem)
        .build()

    // ——————————————————————————————————————————————食物分割线——————————————————————————————————————————————————————————//

    @JvmStatic
    val WHEAT_DOUGH = IdyllicFoodDiary.REGISTER.item<FoodBlockItem>()
        .id("wheat_dough")
        .bound { FoodBlockItem(IFDBlocks.WHEAT_DOUGH.get(), true, FoodTier.BLAND_FOOD, Properties().food(FoodPropertyData.PRIMARY_1)) }
        .build()

    @JvmStatic
    val STEAMED_BUN = IdyllicFoodDiary.REGISTER.item<FoodBlockItem>()
        .id("steamed_bun")
        .bound { FoodBlockItem(IFDBlocks.STEAMED_BUN.get(), true, FoodTier.SIMPLE_MEAL, Properties().food(FoodProperties.Builder().nutrition(4).saturationModifier(0.5f).build())) }
        .build()

    @JvmStatic
    val BEGGARS_CHICKEN = IdyllicFoodDiary.REGISTER.item<FoodBlockItem>()
        .id("beggars_chicken")
        .bound { FoodBlockItem(IFDBlocks.BEGGARS_CHICKEN.get(), false, FoodTier.GREAT_MEAL) }
        .build()

    @JvmStatic
    val ROASTED_SUCKLING_PIG = IdyllicFoodDiary.REGISTER.item<FoodBlockItem>()
        .id("roasted_suckling_pig")
        .bound { FoodBlockItem(IFDBlocks.ROASTED_SUCKLING_PIG.get(), false, FoodTier.FEAST) }
        .build()

}