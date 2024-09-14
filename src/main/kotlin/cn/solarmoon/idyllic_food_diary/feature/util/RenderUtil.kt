package cn.solarmoon.idyllic_food_diary.feature.util

import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.cap.item.TileItemContainerHelper
import cn.solarmoon.spark_core.api.util.PoseStackUtil
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.capabilities.Capabilities
import kotlin.math.sqrt

object RenderUtil {

    @JvmStatic
    fun renderItemStackStack(provider: HolderLookup.Provider, renderBoxVertexMin: Float, rotOffset: Float, yBlockOffset: Float, scale: Float, stack: ItemStack, poseStack: PoseStack, buffer: MultiBufferSource, light: Int, overlay: Int) {
        TileItemContainerHelper.getInventory(stack, provider)?.let { inv ->
            for (i in 0 until inv.slots) {
                poseStack.pushPose()
                val ins = inv.getStackInSlot(i)
                PoseStackUtil.rotateByDirection(Direction.NORTH, poseStack)
                val renderBoxVertexMax = 1-renderBoxVertexMin
                val range = renderBoxVertexMax -renderBoxVertexMin // 宽度
                // 图形的顶点坐标
                val vertices: Array<FloatArray> = arrayOf(
                    floatArrayOf(0.5f, sqrt(3.0).toFloat() / 2f),
                    floatArrayOf(1f, 0f),
                    floatArrayOf(0f, 0f)
                )
                // 按比例定位等边三角形的顶点
                val vertex = vertices[i % 3]
                val x = renderBoxVertexMin + range * vertex[0]
                val h = (yBlockOffset + scale / 2) / 16F + scale / 16F * i // 高度
                val z = renderBoxVertexMin + range * vertex[1]
                poseStack.translate(x, h, z)
                poseStack.scale(scale, scale, scale)
                poseStack.mulPose(Axis.YN.rotationDegrees(rotOffset * i))
                poseStack.mulPose(Axis.XP.rotationDegrees(90f))
                Minecraft.getInstance().itemRenderer.renderStatic(ins, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0)
                poseStack.popPose();
            }
        }
    }

    @JvmStatic
    fun renderItemStackStack(renderBoxVertexMin: Float, rotOffset: Float, yBlockOffset: Float, scale: Float, be: BlockEntity, poseStack: PoseStack, buffer: MultiBufferSource, light: Int, overlay: Int, context: BlockEntityRendererProvider.Context) {
        be.level?.let { level ->
            level.getCapability(Capabilities.ItemHandler.BLOCK, be.blockPos, be.blockState, be, Direction.NORTH)
                ?.let { inv ->
                    val direction = be.blockState.getValue(IHorizontalFacingState.FACING)
                    for (i in 0 until inv.slots) {
                        poseStack.pushPose()
                        val ins = inv.getStackInSlot(i)
                        PoseStackUtil.rotateByDirection(direction, poseStack)
                        val renderBoxVertexMax = 1 - renderBoxVertexMin
                        val range = renderBoxVertexMax - renderBoxVertexMin; // 宽度
                        // 图形的顶点坐标
                        val vertices: Array<FloatArray> = arrayOf(
                            floatArrayOf(0.5f, sqrt(3.0).toFloat() / 2f),
                            floatArrayOf(1f, 0f),
                            floatArrayOf(0f, 0f)
                        )
                        // 按比例定位等边三角形的顶点
                        val vertex = vertices[i % vertices.size]
                        val x = renderBoxVertexMin + range * vertex[0]
                        val h = (yBlockOffset + scale / 2) / 16F + scale / 16F * i // 高度
                        val z = renderBoxVertexMin + range * vertex[1]
                        poseStack.translate(x, h, z)
                        poseStack.scale(scale, scale, scale)
                        poseStack.mulPose(Axis.YN.rotationDegrees(rotOffset * i))
                        poseStack.mulPose(Axis.XP.rotationDegrees(90f))
                        var light0 = light
                        // 防止超出当前pos的部分光照不和超出的pos部分一致
                        if (be.getLevel() != null) {
                            light0 = LevelRenderer.getLightColor(be.getLevel()!!, be.blockPos.above(h.toInt()));
                        }
                        context.itemRenderer.renderStatic(ins, ItemDisplayContext.FIXED, light0, overlay, poseStack, buffer, be.getLevel(), be.blockPos.asLong().toInt())
                        poseStack.popPose()
                    }
                }
        }
    }

}