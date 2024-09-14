package cn.solarmoon.idyllic_food_diary.element.matter.food_container.plate

import cn.solarmoon.idyllic_food_diary.element.matter.food_container.long_plate.LongPlateBlockEntity
import cn.solarmoon.idyllic_food_diary.feature.util.RenderUtil
import cn.solarmoon.spark_core.api.renderer.HandyBlockEntityRenderer
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.phys.Vec3

class PlateBlockRenderer(context: BlockEntityRendererProvider.Context): HandyBlockEntityRenderer<PlateBlockEntity>(context) {

    override fun render(
        blockEntity: PlateBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        RenderUtil.renderItemStackStack(0.5f, 45f, 2f, 0.5f, blockEntity, poseStack, bufferSource, packedLight, packedOverlay, context)
    }

    override fun shouldRender(blockEntity: PlateBlockEntity, cameraPos: Vec3): Boolean {
        return true
    }

    override fun shouldRenderOffScreen(blockEntity: PlateBlockEntity): Boolean {
        return true
    }

}