package cn.solarmoon.idyllic_food_diary.element.matter.food

import cn.solarmoon.idyllic_food_diary.registry.common.IFDAttachments
import cn.solarmoon.spark_core.api.renderer.HandyBlockEntityRenderer
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider

class FoodBlockRenderer<F: FoodBlockEntity>(context: BlockEntityRendererProvider.Context): HandyBlockEntityRenderer<F>(context) {
    override fun render(
        blockEntity: F,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        context.blockRenderDispatcher.renderSingleBlock(blockEntity.containerBlockState, poseStack, bufferSource, packedLight, packedOverlay)
    }
}