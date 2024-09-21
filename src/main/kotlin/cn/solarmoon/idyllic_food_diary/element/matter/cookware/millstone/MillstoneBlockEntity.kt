package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone

import cn.solarmoon.idyllic_food_diary.element.recipe.GrindingRecipe
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.spark_core.api.attachment.animation.AnimHelper
import cn.solarmoon.spark_core.api.attachment.animation.Timer
import cn.solarmoon.spark_core.api.blockentity.SyncedBlockEntity
import cn.solarmoon.spark_core.api.cap.fluid.TileTank
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.recipe.processor.RecipeProcessorHelper
import cn.solarmoon.spark_core.registry.common.SparkAttachments
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.world.level.block.state.BlockState

class MillstoneBlockEntity(pos: BlockPos, state: BlockState) : SyncedBlockEntity(IFDBlockEntities.MILLSTONE.get(), pos, state) {

    val inventory = MillstoneInventory(this, 2)

    val tanks: List<TileTank> = listOf(
        TileTank(this, 1000),
        TileTank(this, 250)
    )

    val grind = GrindingRecipe.Processor(this, inventory, tanks)

    companion object {
        const val ANIM_ROTATION = "rotation"
        const val ANIM_FLOW = "flow"
    }

    init {
        val anim = getData(SparkAttachments.ANIMTICKER)
        anim.timers[ANIM_ROTATION] = Timer().apply { maxTime = 30f }
        anim.timers[ANIM_FLOW] = Timer()
        AnimHelper.Fluid.createFluidAnim(this, Direction.DOWN)
        RecipeProcessorHelper.createMap(this, grind)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ItemStackHandlerHelper.ITEM, inventory.serializeNBT(registries))
        val listTag = ListTag()
        for (tank in tanks) listTag.add(tank.writeToNBT(registries, CompoundTag()))
        tag.put("FluidTanks", listTag)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        if (tag.isEmpty) return
        inventory.deserializeNBT(registries, tag.get(ItemStackHandlerHelper.ITEM) as CompoundTag)
        val listTag = tag.getList("FluidTanks", ListTag.TAG_COMPOUND.toInt())
        listTag.forEachIndexed { i, t -> tanks[i].readFromNBT(registries, t as CompoundTag) }
    }

}