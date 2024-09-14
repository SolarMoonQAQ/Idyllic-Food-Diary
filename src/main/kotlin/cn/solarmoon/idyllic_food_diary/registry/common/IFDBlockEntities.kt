package cn.solarmoon.idyllic_food_diary.registry.common

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneBlockEntity
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokBlockEntity
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockEntity
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodEntityBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.FoodContainerBlockEntity
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.long_plate.LongPlateBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.long_plate.LongPlateBlockEntity
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.plate.PlateBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food_container.plate.PlateBlockEntity
import cn.solarmoon.spark_core.api.blockstate.IBedPartState
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.properties.BedPart
import net.neoforged.neoforge.capabilities.Capabilities
import test.be

object IFDBlockEntities {

    @JvmStatic
    fun register() {}

    @JvmStatic
    val MILLSTONE = IdyllicFoodDiary.REGISTER.blockentity<MillstoneBlockEntity>()
        .id("millstone")
        .bound(::MillstoneBlockEntity)
        .capability(Capabilities.ItemHandler.BLOCK) { c, _ -> c.inventory }
        .capability(Capabilities.FluidHandler.BLOCK) { c, side -> if (side == Direction.DOWN) c.tanks[1] else c.tanks[0] }
        .validBlocks { arrayOf(IFDBlocks.MILLSTONE.get()) }
        .build()

    @JvmStatic
    val WOK = IdyllicFoodDiary.REGISTER.blockentity<WokBlockEntity>()
        .id("wok")
        .bound(::WokBlockEntity)
        .capability(Capabilities.ItemHandler.BLOCK) { be, _ -> be.inventory }
        .capability(Capabilities.FluidHandler.BLOCK) { be, _ -> be.fluidTank }
        .validBlocks { arrayOf(IFDBlocks.WOK.get()) }
        .build()

    @JvmStatic
    val PLATE = IdyllicFoodDiary.REGISTER.blockentity<PlateBlockEntity>()
        .id("plate")
        .bound(::PlateBlockEntity)
        .capability(Capabilities.ItemHandler.BLOCK) { be, _ -> be.inventory }
        .validBlocks { IdyllicFoodDiary.REGISTER.blockDeferredRegister.entries.filter { it.value() is PlateBlock }.map { it.value() }.toTypedArray() }
        .build()

    @JvmStatic
    val LONG_PLATE = IdyllicFoodDiary.REGISTER.blockentity<LongPlateBlockEntity>()
        .id("long_plate")
        .bound(::LongPlateBlockEntity)
        .capability(Capabilities.ItemHandler.BLOCK) { be, _ ->
            val level = be.level ?: return@capability null
            val state = be.blockState
            val pos = be.blockPos
            val opposite = level.getBlockEntity(IBedPartState.getFootPos(state, pos)) as FoodContainerBlockEntity
            if (state.getValue(IBedPartState.PART) == BedPart.HEAD) {
                return@capability opposite.inventory
            } else return@capability be.inventory
        }
        .validBlocks { IdyllicFoodDiary.REGISTER.blockDeferredRegister.entries.filter { it.value() is LongPlateBlock }.map { it.value() }.toTypedArray() }
        .build()

    @JvmStatic
    val FOOD = IdyllicFoodDiary.REGISTER.blockentity<FoodBlockEntity>()
        .id("food")
        .bound(::FoodBlockEntity)
        .validBlocks { IdyllicFoodDiary.REGISTER.blockDeferredRegister.entries.filter { it.value() is FoodEntityBlock }.map { it.value() }.toTypedArray() }
        .build()

}