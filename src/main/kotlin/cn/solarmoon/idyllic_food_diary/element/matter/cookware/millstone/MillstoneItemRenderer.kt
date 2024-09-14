package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.registry.client.IFDLayers
import cn.solarmoon.spark_core.api.renderer.HandyItemRenderer
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class MillstoneItemRenderer: HandyItemRenderer() {
    override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        var context = Minecraft.getInstance().entityModels
        val wheel = context.bakeLayer(IFDLayers.MILLSTONE).getChild("wheel")
        wheel.setRotation(0f, 0f, -Math.PI.toFloat())
        val main = context.bakeLayer(IFDLayers.MILLSTONE).getChild("main")
        val vertexConsumer = buffer.getBuffer(RenderType.entitySolid(ResourceLocation.fromNamespaceAndPath(IdyllicFoodDiary.MOD_ID, "textures/block/millstone.png")))
        poseStack.pushPose()
        poseStack.translate(0.5, 0.0, 0.5)
        wheel.render(poseStack, vertexConsumer, packedLight, packedOverlay)
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay)
        poseStack.popPose()
    }
}