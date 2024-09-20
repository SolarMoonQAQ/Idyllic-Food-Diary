package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareTileRenderer
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlocks
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.phys.collision.FreeCollisionBox
import cn.solarmoon.spark_core.api.phys.collision.FreeCollisionBoxRenderManager
import cn.solarmoon.spark_core.api.util.PoseStackUtil
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.phys.Vec3

class SteamerBlockRenderer(context: BlockEntityRendererProvider.Context): CookwareTileRenderer<SteamerBlockEntity>(context) {

    override fun renderThis(
        steamer: SteamerBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val stackAmount = steamer.presentLayer
        val state = steamer.blockState
        val pos = steamer.blockPos
        val level = steamer.getLevel() ?: return

        // 渲染每层蒸笼
        for (i in 0 until stackAmount) {
            val h = steamer.maxLayer / 16.0 * i
            poseStack.pushPose()
            poseStack.translate(0.0, h, 0.0)
            context.blockRenderDispatcher.renderSingleBlock(state, poseStack, bufferSource, packedLight, packedOverlay)
            poseStack.popPose()
        }

        // 渲染盖子
        if (steamer.hasLid) {
            val h = steamer.maxLayer / 16.0 * stackAmount
            poseStack.pushPose()
            poseStack.translate(0.0, h, 0.0)
            context.blockRenderDispatcher.renderSingleBlock(IFDBlocks.STEAMER_LID.get().defaultBlockState(), poseStack, bufferSource, packedLight, packedOverlay)
            poseStack.popPose()
        }

        // 渲染物品（只渲染最上层）
        val direction = state.getValue(IHorizontalFacingState.FACING)
        if (stackAmount > 0) {
            val inv = steamer.inventories.last()
            for (i in 0 until inv.slots) {
                poseStack.pushPose()
                val stack = inv.getStackInSlot(i)
                PoseStackUtil.rotateByDirection(direction, poseStack)
                // 物品大小
                val scale = 4.5f / 16f
                // 控制图形大小的矩形的始末点
                val renderBoxVertexMin = 5.75f / 16f
                val renderBoxVertexMax = 1 - renderBoxVertexMin
                // 图形长度
                val range = renderBoxVertexMax - renderBoxVertexMin
                // 图形的顶点坐标
                val vertices = arrayOf(
                    arrayOf(1, 1),
                    arrayOf(0, 1),
                    arrayOf(1, 0),
                    arrayOf(0, 0)
                )
                // 按序列定位正方形的顶点
                val vertex = vertices[i % vertices.size]
                val x = renderBoxVertexMin + range * vertex[0]
                val h = 4 / 16f * stackAmount - (1 - scale / 2) / 16F
                val z = renderBoxVertexMin + range * vertex[1]
                poseStack.translate(x, h, z)
                poseStack.scale(scale, scale, scale)
                poseStack.mulPose(Axis.XP.rotationDegrees(90f))
                context.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, level, pos.asLong().toInt())
                poseStack.popPose()
            }
        }

        // debug
        if (steamer.isConnected) {
            FreeCollisionBoxRenderManager("${steamer.blockPos} - SteamerDebug", FreeCollisionBox(steamer.blockPos.center, Vec3(1.0, 1.0, 1.0))).start()
        }

    }

}