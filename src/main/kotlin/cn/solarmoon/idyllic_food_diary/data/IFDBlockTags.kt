package cn.solarmoon.idyllic_food_diary.data

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class IFDBlockTags(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, existingFileHelper: ExistingFileHelper?):
    BlockTagsProvider(output, lookupProvider, IdyllicFoodDiary.MOD_ID, existingFileHelper) {


    companion object {

        /**
         * 热源方块识别
         */
        @JvmStatic
        val HEAT_SOURCE = modTag("heat_source")

        private fun modTag(path: String): TagKey<Block> {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(IdyllicFoodDiary.MOD_ID, path))
        }

        private fun forgeTag(path: String): TagKey<Block> {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", path))
        }
    }

    override fun addTags(provider: HolderLookup.Provider) {
        tag(HEAT_SOURCE).add(
            Blocks.FIRE,
            Blocks.MAGMA_BLOCK,
            Blocks.LAVA,
            Blocks.CAMPFIRE,
            Blocks.SOUL_CAMPFIRE,
            Blocks.FURNACE
        ).replace(false)
    }

}