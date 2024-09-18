package cn.solarmoon.idyllic_food_diary.data

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class IFDItemTags(output: PackOutput,
                  lookupProvider: CompletableFuture<HolderLookup.Provider>,
                  blockTags: CompletableFuture<TagLookup<Block>>,
                  existingFileHelper: ExistingFileHelper?): ItemTagsProvider(output, lookupProvider, blockTags, IdyllicFoodDiary.MOD_ID, existingFileHelper) {

    companion object {

        /**
         * 一格盘类物品，用于配方容器标识
         */
        @JvmStatic
        val PLATE = modTag("plate")

        /**
         * 决定了该类物品是否可渲染特殊的流体展示工具提示
         */
        @JvmStatic
        val FLUID_DISPLAYER = modTag("fluid_displayer")

        private fun modTag(path: String): TagKey<Item> {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(IdyllicFoodDiary.MOD_ID, path))
        }

        private fun forgeTag(path: String): TagKey<Item> {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path))
        }

    }

    override fun addTags(provider: HolderLookup.Provider) {
        tag(PLATE).add(
            IFDItems.WOODEN_PLATE.value(),
            IFDItems.PORCELAIN_PLATE.value()
        )
        tag(FLUID_DISPLAYER).add(
            IFDItems.KETTLE.get(),
            IFDItems.JADE_CHINA_CUP.value()
        )
    }

}