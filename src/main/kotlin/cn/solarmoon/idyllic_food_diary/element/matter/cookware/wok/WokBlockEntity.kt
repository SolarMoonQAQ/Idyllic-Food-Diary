package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock
import cn.solarmoon.idyllic_food_diary.element.recipe.EvaporationRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.StirFryRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.WaterBoilingRecipe
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
    val boil = WaterBoilingRecipe.Processor(this, fluidTank)
    val eva = EvaporationRecipe.Processor(this, fluidTank)

    init {
        RecipeProcessorHelper.createMap(this, fry, boil, eva)
        AnimHelper.Fluid.createFluidAnim(this)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ItemStackHandlerHelper.ITEM, inventory.serializeNBT(registries))
        saveFluid(fluidTank.fluid)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        loadFluid(fluidTank, tag, registries)
        if (tag.isEmpty) return // 防止新物品什么都没有的情况下读取报错
        inventory.deserializeNBT(registries, tag.get(ItemStackHandlerHelper.ITEM) as CompoundTag)
    }

}