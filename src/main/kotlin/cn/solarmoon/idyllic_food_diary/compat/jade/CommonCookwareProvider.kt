package cn.solarmoon.idyllic_food_diary.compat.jade

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.recipe.StirFryRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.assistant.IPlateable
import cn.solarmoon.spark_core.api.recipe.processor.RecipeProcessorHelper
import cn.solarmoon.spark_core.api.recipe.processor.SingleTimeRecipeProcessor
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

class CommonCookwareProvider(val configId: String): IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        val be = accessor.blockEntity
        RecipeProcessorHelper.getMap(be).forEach { type, processor ->
            if (processor is SingleTimeRecipeProcessor) {
                JadeUtil.addByTime(processor.time, processor.recipeTime, tooltip)
            }
            if (processor is IPlateable) {
                JadeUtil.addPlatingResult(tooltip, processor)
            }
            if (processor is StirFryRecipe.Processor) {
                JadeUtil.addStirFryStage(tooltip, processor)
            }
        }
    }

    override fun getUid(): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(IdyllicFoodDiary.MOD_ID, configId)
    }

    override fun appendServerData(tag: CompoundTag, accessor: BlockAccessor) {
    }

}