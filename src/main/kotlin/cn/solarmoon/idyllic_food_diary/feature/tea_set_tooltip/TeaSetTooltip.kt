package cn.solarmoon.idyllic_food_diary.feature.tea_set_tooltip

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.data.IFDItemTags
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.TileItemContainerHelper
import cn.solarmoon.spark_core.api.renderer.TextureRenderHelper
import cn.solarmoon.spark_core.api.tooltip.CustomTooltip
import cn.solarmoon.spark_core.api.util.TextUtil
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

class TeaSetTooltip(component: Component): CustomTooltip<TeaSetTooltip.Component>(component) {

    //渲染大小
    val size = 16
    //渲染贴图数，间接决定渲染长度
    val count = 5
    //高度偏移
    val heightO = 3
    //液体和文字和物品的整体偏移量
    //除贴图外的长度偏移，用于和贴图匹配
    val deltaF = 4
    //除贴图外的高度偏移，用于和贴图匹配
    val hOffset = 2

    override fun getHeight(): Int {
        return if (shouldRender()) size + heightO + 10 else 0
    }

    override fun getWidth(font: Font): Int {
        return if (shouldRender()) size * count + deltaF + 5 else 0
    }

    fun shouldRender(): Boolean {
        return component.stack.`is`(IFDItemTags.FLUID_DISPLAYER) && component.isTankValid
    }

    override fun renderImage(font: Font, x: Int, y: Int, guiGraphics: GuiGraphics) {
        if(shouldRender()) {
            val stack = component.stack
            val stackTank = component.tank
            val fluidStack = stackTank.getFluidInTank(0)
            RenderSystem.enableDepthTest()
            RenderSystem.enableBlend()
            RenderSystem.defaultBlendFunc()
            drawFluid(fluidStack, guiGraphics, stackTank, x ,y)
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F)
            //液体名称容量显示
            val poseStack = guiGraphics.pose()
            drawText(poseStack, fluidStack, font, guiGraphics, x, y)
            //渲染内容物
            drawItem(stack, poseStack, guiGraphics, x, y)
            //渲染图片
            drawGUI(stack, poseStack, guiGraphics, x, y)
            RenderSystem.disableBlend()
            RenderSystem.disableDepthTest()
        }
    }

    /**
     * 渲染液体容量描述
     */
    private fun drawText(poseStack: PoseStack, fluidStack: FluidStack, font: Font, guiGraphics: GuiGraphics, x: Int, y: Int) {
        poseStack.pushPose()
        poseStack.translate(0.0, 0.0, 300.0)
        if(!fluidStack.isEmpty) {
            val str1 = fluidStack.hoverName.string
            val str2 = "${fluidStack.amount}mB"
            val str1Length = font.width(str1)
            guiGraphics.drawString(font, str1, x + 2 + deltaF, y + 4 + heightO + hOffset, 0xFFFFFF)
            guiGraphics.drawString(font, str2, x + str1Length + 3 + deltaF, y + 4 + heightO + hOffset, 0xFFFFFF)
        } else {
            guiGraphics.drawString(font, IdyllicFoodDiary.TRANSLATOR.set("tooltip", "fluid_empty"), x + 2 + deltaF, y + 4 + heightO + hOffset, 0xFFFFFF)
        }
        poseStack.popPose()
    }

    /**
     * 渲染物品
     */
    fun drawItem(stack: ItemStack, poseStack: PoseStack, guiGraphics: GuiGraphics, x: Int, y: Int) {
        val level = Minecraft.getInstance().level ?: return
        TileItemContainerHelper.getInventory(stack, level.registryAccess())?.let { inventory ->
            for (i in 0 until inventory.slots) {
                poseStack.pushPose()
                val stackIn = inventory.getStackInSlot(i)
                poseStack.translate(-i * 16.0, 0.0, 2.0)
                val scale = 1f
                poseStack.scale(1f / scale, 1f / scale, 1f)
                guiGraphics.renderItem(
                    stackIn,
                    ((x + size * count - 16 + deltaF) * scale).toInt(),
                    ((y + heightO + hOffset) * scale).toInt()
                )
                poseStack.popPose()
            }
        }
    }

    /**
     * 渲染gui
     */
    fun drawGUI(stack: ItemStack, poseStack: PoseStack, guiGraphics: GuiGraphics, x: Int, y: Int) {
        poseStack.pushPose()
        val id = BuiltInRegistries.ITEM.getKey(stack.item)
        val cate = TextUtil.extractString(id.toString(), ":")
        val res = "textures/gui/fluid_displayer/$cate.png"
        val scale = 1f
        poseStack.scale(1f / scale, 1f / scale, 1f)
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(IdyllicFoodDiary.MOD_ID, res),
        (x * scale).toInt(), (y * scale).toInt() + heightO - 2,
        0, 0, 88, 24)
        poseStack.popPose()
    }

    /**
     * 渲染液体
     */
    fun drawFluid(fluidStack: FluidStack, guiGraphics: GuiGraphics, stackTank: IFluidHandlerItem, x: Int, y: Int) {
        if(!fluidStack.isEmpty) {
            val fluidStillSprite = TextureRenderHelper.getFluidTexture(fluidStack, TextureRenderHelper.FluidFlow.STILL) ?: return
            val color = TextureRenderHelper.getColor(fluidStack)

            val uMin = fluidStillSprite.u0
            var uMax = fluidStillSprite.u1
            val vMin = fluidStillSprite.v0
            val vMax = fluidStillSprite.v1
            val lu = 240
            val lv = 240

            val matrix = guiGraphics.pose().last().pose()
            val bufferBuilder = guiGraphics.bufferSource().getBuffer(RenderType.text(fluidStillSprite.atlasLocation()))

            for (i in 0 until count) {
                val percentage = FluidHandlerHelper.getScale(stackTank)

                if (percentage > i / count.toFloat()) {
                    var reduction = 0f
                    //实际长度与已渲染长度的差额
                    var difference = (i + 1) * size - count * size * percentage
                    //小于0，表明目前长度比总长度小，故而无需削减（具体到i就是渲染的第i+1个贴图长度和比总长（实际长度）小，所以自身无需应用削减）
                    //也因此削减总是在一个size范围内，因为这里的差值永远被i跟进，从而永远是一个size内的差值
                    if (difference > 0) reduction = difference
                    //偏移量，使能连续无缝跟进贴图
                    val offset = i * size

                    uMax = uMax - (reduction / 16F * (uMax - uMin))
                    bufferBuilder.addVertex(matrix, x + deltaF + offset.toFloat(), y + size + heightO + hOffset.toFloat(), 1f).setUv(uMin, vMax).setColor(color).setUv2(lu, lv)
                    bufferBuilder.addVertex(matrix, x + deltaF + size + offset.toFloat() - reduction, y + size + heightO + hOffset.toFloat(), 1f).setUv(uMax, vMax).setColor(color).setUv2(lu, lv)
                    bufferBuilder.addVertex(matrix, x + deltaF + size + offset.toFloat() - reduction, y + heightO + hOffset.toFloat(), 1f).setUv(uMax, vMin).setColor(color).setUv2(lu, lv)
                    bufferBuilder.addVertex(matrix, x + deltaF + offset.toFloat(), y + heightO + hOffset.toFloat(), 1f).setUv(uMin, vMin).setColor(color).setUv2(lu, lv)
                }
            }
        }
    }

    class Component(val stack: ItemStack): TooltipComponent {
        val tank
            get() = stack.getCapability(Capabilities.FluidHandler.ITEM)!!
        val isTankValid
            get() = stack.getCapability(Capabilities.FluidHandler.ITEM) != null
    }

}