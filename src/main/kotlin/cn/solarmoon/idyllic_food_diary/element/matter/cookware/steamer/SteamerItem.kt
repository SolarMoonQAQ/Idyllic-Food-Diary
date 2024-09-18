package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer

import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.ListTag
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.block.entity.BlockEntityType

class SteamerItem: BlockItem(IFDBlocks.STEAMER.get(), Properties().stacksTo(1)) {

    companion object {
        @JvmStatic
        fun singleEmptyLayer(registries: HolderLookup.Provider) = CustomData.EMPTY.update {
            val listTag = ListTag()
            listTag.add(SteamerInventory(4).serializeNBT(registries))
            it.putString("id", BlockEntityType.getKey(IFDBlockEntities.STEAMER.get()).toString()) // 此项为系统自动添加，不可能不加
            it.put(SteamerBlockEntity.INV_LIST, listTag)
            it.put(SteamerBlockEntity.LID, ItemStack.EMPTY.saveOptional(registries))
        }

        @JvmStatic
        fun getInvListTag(stack: ItemStack, registries: HolderLookup.Provider): ListTag {
            val tag = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, singleEmptyLayer(registries)).copyTag()
            return tag.getList(SteamerBlockEntity.INV_LIST, ListTag.TAG_COMPOUND.toInt())
        }

        @JvmStatic
        fun getLid(stack: ItemStack, registries: HolderLookup.Provider): ItemStack {
            val tag = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, singleEmptyLayer(registries)).copyTag()
            return ItemStack.parseOptional(registries, tag.getCompound(SteamerBlockEntity.LID))
        }

        @JvmStatic
        fun setLid(stack: ItemStack, lid: ItemStack, registries: HolderLookup.Provider) {
            stack.update(DataComponents.BLOCK_ENTITY_DATA, singleEmptyLayer(registries)) {
                it.update {
                    it.put(SteamerBlockEntity.LID, lid.saveOptional(registries))
                }
            }
        }
    }

}