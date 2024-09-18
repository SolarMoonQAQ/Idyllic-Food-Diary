package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup.LittleCupBlock
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.KettleBlock
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneBlock
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerBlock
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerLidBlock
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.feast.BeggarsChickenBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.simple.SteamedBunBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.simple.WheatDoughBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.long_plate.LongPlateBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.plate.PlateBlock
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.InlaidStoveBlock
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.StoveLidBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour

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
    val JADE_CHINA_CUP = IdyllicFoodDiary.REGISTER.block<LittleCupBlock>()
        .id("jade_china_cup")
        .bound { LittleCupBlock(BlockBehaviour.Properties.of().sound(SoundType.GLASS).strength(1.0f).forceSolidOn()) }
        .build()

    @JvmStatic
    val KETTLE = IdyllicFoodDiary.REGISTER.block<KettleBlock>()
        .id("kettle")
        .bound(::KettleBlock)
        .build()

    @JvmStatic
    val STEAMER = IdyllicFoodDiary.REGISTER.block<SteamerBlock>()
        .id("steamer")
        .bound(::SteamerBlock)
        .build()

    @JvmStatic
    val STEAMER_LID = IdyllicFoodDiary.REGISTER.block<SteamerLidBlock>()
        .id("steamer_lid")
        .bound(::SteamerLidBlock)
        .build()

    // ——————————————————————————————————————————————食物分割线——————————————————————————————————————————————————————————//

    @JvmStatic
    val WHEAT_DOUGH = IdyllicFoodDiary.REGISTER.block<WheatDoughBlock>()
        .id("wheat_dough")
        .bound(::WheatDoughBlock)
        .build()

    @JvmStatic
    val STEAMED_BUN = IdyllicFoodDiary.REGISTER.block<SteamedBunBlock>()
        .id("steamed_bun")
        .bound(::SteamedBunBlock)
        .build()

    @JvmStatic
    val BEGGARS_CHICKEN = IdyllicFoodDiary.REGISTER.block<BeggarsChickenBlock>()
        .id("beggars_chicken")
        .bound(::BeggarsChickenBlock)
        .build()

}