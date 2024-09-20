package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle

import cn.solarmoon.idyllic_food_diary.element.recipe.BrewingRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.WaterBoilingRecipe
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.spark_core.api.blockentity.SyncedBlockEntity
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.cap.fluid.TileTank
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.TileInventory
import cn.solarmoon.spark_core.api.recipe.processor.RecipeProcessorHelper
import cn.solarmoon.spark_core.registry.common.SparkDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.state.BlockState
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

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(FluidHandlerHelper.FLUID, tank.writeToNBT(registries, CompoundTag()))
        tag.put(ItemStackHandlerHelper.ITEM, inventory.serializeNBT(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        tank.readFromNBT(registries, tag.getCompound(FluidHandlerHelper.FLUID))
        inventory.deserializeNBT(registries, tag.getCompound(ItemStackHandlerHelper.ITEM))
    }

}