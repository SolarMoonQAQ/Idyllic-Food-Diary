package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareTileRenderer
import cn.solarmoon.spark_core.api.attachment.animation.AnimHelper
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction

class LittleCupBlockRenderer(context: BlockEntityRendererProvider.Context): CookwareTileRenderer<LittleCupBlockEntity>(context) {
    override fun renderThis(
        blockEntity: LittleCupBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        AnimHelper.Fluid.renderAnimatedFluid(blockEntity, Direction.NORTH, 2/16f, 4/16f, 1/16f, partialTick, poseStack, bufferSource, packedLight)
    }
}