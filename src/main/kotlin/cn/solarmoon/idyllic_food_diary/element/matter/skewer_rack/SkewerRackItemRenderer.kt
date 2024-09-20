package cn.solarmoon.idyllic_food_diary.element.matter.skewer_rack

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

class SkewerRackItemRenderer: HandyItemRenderer() {

    override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        var context = Minecraft.getInstance().entityModels
        val main = context.bakeLayer(IFDLayers.SKEWER_RACK).getChild("main")
        main.setRotation(0f, -Math.PI.toFloat(), 0f)
        val vertexConsumer = buffer.getBuffer(RenderType.entitySolid(ResourceLocation.fromNamespaceAndPath(IdyllicFoodDiary.MOD_ID, "textures/block/skewer_rack.png")))
        poseStack.pushPose()
        poseStack.translate(0.5, 0.5, 0.5)
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay)
        poseStack.popPose()
    }
}