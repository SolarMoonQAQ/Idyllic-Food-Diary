package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneBlock
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.feast.BeggarsChickenBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.FoodContainerItem
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.long_plate.LongPlateBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.plate.PlateBlock
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.InlaidStoveBlock
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.StoveLidBlock
import net.minecraft.world.level.block.SoundType

object IFDBlocks {

    @JvmStatic
    fun register() {}

    @JvmStatic
    val INLAID_STOVE = IdyllicFoodDiary.REGISTER.block<InlaidStoveBlock>()
        .id("inlaid_stove")
        .bound(::InlaidStoveBlock)
        .build()

    @JvmStatic
    val STOVE_LID = IdyllicFoodDiary.REGISTER.block<StoveLidBlock>()
        .id("stove_lid")
        .bound(::StoveLidBlock)
        .build()

    @JvmStatic
    val MILLSTONE = IdyllicFoodDiary.REGISTER.block<MillstoneBlock>()
        .id("millstone")
        .bound(::MillstoneBlock)
        .build()

    @JvmStatic
    val WOK = IdyllicFoodDiary.REGISTER.block<WokBlock>()
        .id("wok")
        .bound(::WokBlock)
        .build()

    @JvmStatic
    val WOODEN_PLATE = IdyllicFoodDiary.REGISTER.block<PlateBlock>()
        .id("wooden_plate")
        .bound { PlateBlock(SoundType.WOOD) }
        .build()

    @JvmStatic
    val PORCELAIN_PLATE = IdyllicFoodDiary.REGISTER.block<PlateBlock>()
        .id("porcelain_plate")
        .bound { PlateBlock(SoundType.GLASS) }
        .build()

    @JvmStatic
    val LONG_WOODEN_PLATE = IdyllicFoodDiary.REGISTER.block<LongPlateBlock>()
        .id("long_wooden_plate")
        .bound { LongPlateBlock(SoundType.WOOD) }
        .build()

    @JvmStatic
    val LONG_PORCELAIN_PLATE = IdyllicFoodDiary.REGISTER.block<LongPlateBlock>()
        .id("long_porcelain_plate")
        .bound { LongPlateBlock(SoundType.GLASS) }
        .build()

    @JvmStatic
    val BEGGARS_CHICKEN = IdyllicFoodDiary.REGISTER.block<BeggarsChickenBlock>()
        .id("beggars_chicken")
        .bound(::BeggarsChickenBlock)
        .build()

}