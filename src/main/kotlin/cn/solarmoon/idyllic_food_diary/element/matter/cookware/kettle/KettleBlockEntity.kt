package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle

import cn.solarmoon.idyllic_food_diary.element.recipe.BrewingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.WaterBoilingRecipe
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.spark_core.api.blockentity.SyncedBlockEntity
import cn.solarmoon.spark_core.api.cap.fluid.TileTank
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.TileInventory
import cn.solarmoon.spark_core.api.recipe.processor.RecipeProcessorHelper
import cn.solarmoon.spark_core.registry.common.SparkDataComponents
import com.mojang.logging.LogUtils
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.SimpleFluidContent

class KettleBlockEntity(pos: BlockPos, state: BlockState) : SyncedBlockEntity(IFDBlockEntities.KETTLE.get(), pos, state) {

    val tank = TileTank(this, 1000)
    val inventory = TileInventory(this, 4, 1)
    val boil = WaterBoilingRecipe.Processor(this, tank)
    val brew = BrewingRecipe.Processor(this, inventory, tank)

    init {
        tank.fluid = components().getOrDefault(SparkDataComponents.SIMPLE_FLUID_CONTENT.value(), SimpleFluidContent.EMPTY).copy()
        RecipeProcessorHelper.createMap(this, boil, brew)
    }

    override fun saveFluid(fluid: FluidStack) {
        super.saveFluid(fluid)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        saveFluid(tank.fluid)
        tag.put(ItemStackHandlerHelper.ITEM, inventory.serializeNBT(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        loadFluid(tank, tag, registries)
        if (tag.isEmpty) return // 防止新物品什么都没有的情况下读取报错
        inventory.deserializeNBT(registries, tag.getCompound(ItemStackHandlerHelper.ITEM))
    }

}