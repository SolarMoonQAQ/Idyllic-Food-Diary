package cn.solarmoon.idyllic_food_diary.compat.jade

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.recipe.StirFryRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.assistant.IPlateable
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.util.StringUtil
import net.minecraft.world.phys.Vec2
import snownee.jade.api.ITooltip
import snownee.jade.api.ui.BoxStyle
import snownee.jade.api.ui.BoxStyle.GradientBorder
import snownee.jade.api.ui.IElement
import snownee.jade.api.ui.ProgressStyle
import snownee.jade.impl.ui.ItemStackElement
import snownee.jade.impl.ui.ProgressElement
import snownee.jade.impl.ui.SimpleProgressStyle
import snownee.jade.impl.ui.TextElement

object JadeUtil {

    @JvmStatic
    fun addByTime(timeNow: Int, recipeTime: Int, iTooltip: ITooltip) {
        val scale = timeNow / recipeTime.toFloat()
        if (timeNow != 0) {
            val progress = ProgressElement(
                scale,
                Component.literal(StringUtil.formatTickDuration(timeNow, 20f) + "/" + StringUtil.formatTickDuration(recipeTime, 20f)).withStyle(ChatFormatting.WHITE),
                SimpleProgressStyle(),
                GradientBorder.DEFAULT_VIEW_GROUP,
                true
            )
            iTooltip.add(progress)
        }
    }

    @JvmStatic
    fun addStirFryStage(iTooltip: ITooltip, stir: StirFryRecipe.Processor) {
        val text = TextElement(IdyllicFoodDiary.TRANSLATOR.set("jade", "stir_fry.stage",
            if (stir.isWorking()) stir.presentStageIndex + 1 else 0))
        iTooltip.add(text)
    }

    @JvmStatic
    fun addPlatingResult(iTooltip: ITooltip, pr: IPlateable) {
        val text = TextElement(IdyllicFoodDiary.TRANSLATOR.set("jade", "plating").copy().append(
            if (pr.result.isEmpty) IdyllicFoodDiary.TRANSLATOR.set("jade", "plating.empty")
            else Component.translatable(pr.result.descriptionId)
        ))
        iTooltip.add(text)
        val item = ItemStackElement.of(pr.result, 0.5f)
        if (!pr.result.isEmpty) iTooltip.append(item)
    }

}