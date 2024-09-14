package cn.solarmoon.idyllic_food_diary.element.matter.food_container

import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.spark_core.api.blockentity.SyncedBlockEntity
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.TileInventory
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class FoodContainerBlockEntity(type: BlockEntityType<*>, size: Int, pos: BlockPos, state: BlockState): SyncedBlockEntity(type, pos, state) {

    val inventory = FoodContainerInventory(this, size)
    val lastItem: ItemStack
        get() {
            val list = ItemStackHandlerHelper.getStacks(inventory)
            return if (list.isEmpty()) ItemStack.EMPTY else list.last()
        }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ItemStackHandlerHelper.ITEM, inventory.serializeNBT(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        inventory.deserializeNBT(registries, tag.getCompound(ItemStackHandlerHelper.ITEM))
    }

}