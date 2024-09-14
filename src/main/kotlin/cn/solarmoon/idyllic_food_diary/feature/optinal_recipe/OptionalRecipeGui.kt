package cn.solarmoon.idyllic_food_diary.feature.optinal_recipe

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.registry.client.IFDResources
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.spark_core.api.util.HitResultUtil
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

abstract class OptionalRecipeGui: LayeredDraw.Layer {

    var middleX = -1; var middleY = -1 // 中间物品的坐标，同时控制整个gui的坐标
    val internal = 27 // 最大移动距离，也是上下两个物品的间隔距离
    val maxTime = 7 / 0.02
    var tickSpeed = 1
    private var fade = 0f
    private var tick = 0
    private val progress
        get() = if (up) tick / maxTime.toFloat() else (maxTime.toFloat() - tick) / maxTime.toFloat()
    private var up = false
    private var index = 0
    private val targetIndex: Int
        get() {
            val player = Minecraft.getInstance().player ?: return 0
            val level = player.level()
            val hitResult = HitResultUtil.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE)
            val targetBlock = level.getBlockState(hitResult.blockPos).block
            return player.mainHandItem.getOrDefault(IFDDataComponents.RECIPE_SELECTION, RecipeSelectData.EMPTY).getIndex(targetBlock)
        }

    override fun render(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
        if (!shouldRender()) {
            fade = 0f
            tick = 0
            index = targetIndex // 保证不渲染物品以后不再进行动画直接完成目标赋值
            return
        }

        val size = getShowListSize()
        repeat(tickSpeed) {
            if (up && tick < maxTime && size > 1) tick++
            else if (!up && tick > 0 && size > 1) tick--
        }

        if (index != targetIndex) {
            // 每轮动画结束后(或者理解为两个动画衔接处)重置当前值
            if (up == true && tick == maxTime.toInt()) {
                if (size != 0) index = (index + 1 + size) % size
                tick = 0
            }
            else if (up == false && tick == 0) {
                if (size != 0) index = (index - 1 + size) % size
                tick = maxTime.toInt()
            }
        } else {
            tickSpeed = 1
        }

        fade = fade.coerceIn(0f, 1f)
        if (renderCheckable(guiGraphics)) {
            fade += deltaTracker.gameTimeDeltaTicks / 7
        } else {
            fade -= deltaTracker.gameTimeDeltaTicks / 7
            index = targetIndex // 保证不渲染物品以后不再进行动画直接完成目标赋值
        }

        // 渲染背景贴图
        val colors = RenderSystem.getShaderColor()
        RenderSystem.setShaderColor(colors[0], colors[1], colors[2], fade)
        RenderSystem.enableBlend()
        val h = 84
        guiGraphics.blit(
            IFDResources.RECIPE_SELECTION,
            middleX - 11, (middleY - internal - 7) + (h/2 * (1 - fade)).toInt(),
            0, (h/2 * (1 - fade)).toInt(),
            38, (h * fade).toInt()
        ) // 卷轴动画
        RenderSystem.disableBlend()
        RenderSystem.setShaderColor(colors[0], colors[1], colors[2], 1.0f)
    }

    open fun renderCheckable(guiGraphics: GuiGraphics): Boolean {
        val scale = Minecraft.getInstance().window.guiScale
        middleX = guiGraphics.guiWidth() * 3 / 5; middleY = (guiGraphics.guiHeight() * 1 / 2 - 4 * scale).toInt()
        val level = Minecraft.getInstance().level ?: return false
        val player = Minecraft.getInstance().player.takeIf { it?.isCrouching == true } ?: return false // 必须蹲下
        val hitResult = HitResultUtil.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE)
        val targetBlock = level.getBlockState(hitResult.blockPos).block
        val shownItems = getItemsShown(targetBlock, level).takeIf { !it.isEmpty() } ?: return false
        val size = shownItems.size
        val up2Item = shownItems[(index + 2 + size) % size]
        val upItem = shownItems[(index + 1 + size) % size]
        val middleItem = shownItems[index]
        val downItem = shownItems[(index - 1 + size) % size]
        val down2Item = shownItems[(index - 2 + size) % size]
        val poseStack = guiGraphics.pose()

        fun anim(stack: ItemStack, originScale: Float, targetScale: Float, xOffset: Float, yOffset: Float) {
            val p = progress
            poseStack.pushPose()
            poseStack.translate(xOffset, yOffset, 10f)
            val scale = originScale + (targetScale - originScale) * p
            poseStack.scale(scale, scale, 1f)
            poseStack.translate(middleX / scale * (1 - scale), middleY / scale * (1 - scale), 0f) // 坐标恢复公式
            if (fade == 1f) guiGraphics.renderItem(stack, middleX, middleY)
            poseStack.popPose()
        }

        if (up) {
            val p = progress
            val q = 1 - p
            anim(upItem, 0f, 1f, 8f * q, -internal.toFloat())
            anim(middleItem, 1f, 1.5f, -4f * p, -4f * p - internal * q)
            anim(downItem, 1.5f, 1f, -4f * q, -4f * q + internal * p)
            anim(down2Item, 1f, 0f, 8f * p, internal.toFloat() + 16f * p)
        } else {
            val p = progress
            val q = 1 - p
            anim(up2Item, 1f, 0f, 8f * p, -internal.toFloat())
            anim(upItem, 1.5f, 1f, -4f * q, -4f * q - internal * p)
            anim(middleItem, 1f, 1.5f, -4f * p, -4f * p + internal * q)
            anim(downItem, 0f, 1f, 8f * q, internal.toFloat() + 16f * q)
        }
        return true
    }

    fun up() {
        val size = getShowListSize()
        if (tick == 0) index = (index + 1 + size) % size // 动画开始时设定值
        if (up == true) tickSpeed++
        up = true
    }

    fun down() {
        val size = getShowListSize()
        if (tick == maxTime.toInt()) index = (index - 1 + size) % size
        if (up == false) tickSpeed++
        up = false
    }

    fun shouldRender(): Boolean {
        return matchItems.any { Minecraft.getInstance().player?.mainHandItem?.`is`(it) == true }
    }

    private fun getShowListSize(): Int {
        val level = Minecraft.getInstance().level ?: return 0
        val player = Minecraft.getInstance().player ?: return 0
        val hitResult = HitResultUtil.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE)
        val targetBlock = level.getBlockState(hitResult.blockPos).block
        val shownItems = getItemsShown(targetBlock, level)
        return shownItems.size
    }

    /**
     * 展示gui的物品
     */
    abstract val matchItems: List<Item>

    /**
     * 获取要展示的物品列表，必须保证顺序和配方提供的一致
     */
    abstract fun getItemsShown(input: Block, level: Level): List<ItemStack>

}