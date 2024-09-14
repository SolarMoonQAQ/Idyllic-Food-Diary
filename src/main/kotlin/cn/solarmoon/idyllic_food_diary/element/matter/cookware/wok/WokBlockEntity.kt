package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock
import cn.solarmoon.idyllic_food_diary.element.recipe.StirFryRecipe
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.spark_core.api.attachment.animation.AnimHelper
import cn.solarmoon.spark_core.api.blockentity.SyncedBlockEntity
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.cap.fluid.TileTank
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.TileInventory
import cn.solarmoon.spark_core.api.recipe.processor.RecipeProcessorHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class WokBlockEntity(pos: BlockPos, state: BlockState) : SyncedBlockEntity(IFDBlockEntities.WOK.get(), pos, state) {

    val inventory: TileInventory = object : TileInventory(this, 6, 1) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return Block.byItem(stack.item) !is CookwareBlock
        }
    }
    val fluidTank = TileTank(this, 250)

    var soundTick: Int = 0
    val fry = StirFryRecipe.Processor(this, inventory, fluidTank)

    init {
        RecipeProcessorHelper.createMap(this, fry)
        AnimHelper.Fluid.createFluidAnim(this)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ItemStackHandlerHelper.ITEM, inventory.serializeNBT(registries))
        tag.put(FluidHandlerHelper.FLUID, fluidTank.writeToNBT(registries, CompoundTag()))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        inventory.deserializeNBT(registries, tag.get(ItemStackHandlerHelper.ITEM) as CompoundTag)
        fluidTank.readFromNBT(registries, tag.getCompound(FluidHandlerHelper.FLUID))
    }

}