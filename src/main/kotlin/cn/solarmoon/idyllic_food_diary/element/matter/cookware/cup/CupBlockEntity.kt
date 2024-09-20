package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup

import cn.solarmoon.idyllic_food_diary.element.recipe.BrewingRecipe
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.attachment.animation.AnimHelper
import cn.solarmoon.spark_core.api.blockentity.SyncedBlockEntity
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.cap.fluid.TileTank
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.TileInventory
import cn.solarmoon.spark_core.api.recipe.processor.RecipeProcessorHelper
import cn.solarmoon.spark_core.registry.common.SparkDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.SimpleFluidContent

abstract class CupBlockEntity(type: BlockEntityType<*>, size: Int, capacity: Int, pos: BlockPos, state: BlockState): SyncedBlockEntity(type, pos, state) {

    val inventory = object : TileInventory(this, size, 1) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            val recipes = level?.recipeManager?.getAllRecipesFor(IFDRecipes.BREWING.type.get()) ?: return false
            return recipes.any { it.value.ingredients.any { it.ingredient.test(stack) } }
        }
    }
    val tank = CupTank(this, capacity)
    val brew = BrewingRecipe.Processor(this, inventory, tank)

    init {
        AnimHelper.Fluid.createFluidAnim(this, Direction.UP)
        RecipeProcessorHelper.createMap(this, brew)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(FluidHandlerHelper.FLUID, tank.writeToNBT(registries, CompoundTag()))
        tag.put(ItemStackHandlerHelper.ITEM, inventory.serializeNBT(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        inventory.deserializeNBT(registries, tag.getCompound(ItemStackHandlerHelper.ITEM))
        tank.readFromNBT(registries, tag.getCompound(FluidHandlerHelper.FLUID))
    }

}