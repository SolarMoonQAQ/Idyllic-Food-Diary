package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareTileRenderer
import cn.solarmoon.spark_core.api.attachment.animation.AnimHelper
import cn.solarmoon.spark_core.api.attachment.animation.Timer
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.phys.SMath
import cn.solarmoon.spark_core.api.util.BlockUtil
import cn.solarmoon.spark_core.api.util.PoseStackUtil
import cn.solarmoon.spark_core.api.util.VecUtil
import cn.solarmoon.spark_core.registry.common.SparkAttachments
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class WokBlockRenderer(context: BlockEntityRendererProvider.Context): CookwareTileRenderer<WokBlockEntity>(context) {
    override fun renderThis(
        pan: WokBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val direction = pan.blockState.getValue(IHorizontalFacingState.FACING);
        //渲染物品
        //让光度和环境一致
        val lt = if (pan.getLevel() != null) LevelRenderer.getLightColor(pan.getLevel()!!, pan.blockPos.above()) else 15728880;
        for (i in 0 until ItemStackHandlerHelper.getStacks(pan.inventory).size) {
            poseStack.pushPose();
            val minX = 0.4f; // 三角形绘制范围的最小值
            val maxX = 0.6f; // 绘制范围的最大值
            val range = maxX - minX; // 宽度
            // 图形的顶点坐标
            val vertices = arrayOf(
                floatArrayOf(0.5f, 0f),
                floatArrayOf(0f, (sqrt(3f) / 2).toFloat()),
                floatArrayOf(1f, (sqrt(3f) / 2).toFloat())
            )
            // 按比例定位等边三角形的顶点
            val vertex = vertices[i % 3];
            val x = minX + range * vertex[0];
            val h = 1.25/16F + 0.5/16F * i; // 高度
            val z = minX + range * vertex[1];
            val actV = VecUtil.rotateVec(Vec3(x.toDouble(), 0.0, z.toDouble()), Vec3(0.5, 0.0, 0.5), direction)
            poseStack.translate(actV.x, h, actV.z);
            poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot() - 60 * i));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            val name = "fry$i"
            val anim = pan.getData(SparkAttachments.ANIMTICKER)
            val fryTimer = anim.timers.getOrDefault(name, Timer())
            if (fryTimer.isTiming) {
                poseStack.translate(0.0, SMath.parabolaFunction(min(fryTimer.getProgress(partialTick).toDouble(), 1.0), 1.0 / 2.0, anim.fixedValues.getOrDefault("maxHeight$i", 0f).toDouble(), 0.0), 0.0)
                val randomR = anim.fixedValues.getOrDefault("rotRandom$i", 0f)
                val angle = (2.0f * Math.PI * randomR).toFloat()
                poseStack.mulPose(Axis.of(Vector3f(cos(angle), 0f, sin(angle)))
                    .rotation(Mth.rotLerp(min(fryTimer.getProgress(partialTick).toDouble(), 1.0), 0.0, 2*Math.PI).toFloat()))
            }
            poseStack.mulPose(Axis.XN.rotationDegrees(rotFix(direction, poseStack)))
            context.itemRenderer.renderStatic(ItemStackHandlerHelper.getStacks(pan.inventory)[i], ItemDisplayContext.FIXED, lt, packedOverlay, poseStack, bufferSource, context.blockEntityRenderDispatcher.level, pan.blockPos.asLong().toInt())
            poseStack.popPose()
        }

        // 产出渲染
        val rI = pan.fry.result
        var result = Block.byItem(rI.item).defaultBlockState()
        result = BlockUtil.inheritBlockWithAllState(pan.blockState, result)
        if (!result.isAir /*&& result.block is FoodEntityBlock*/) {
            poseStack.pushPose()
            poseStack.translate(0f, -1/16f, 0f)
            context.blockRenderDispatcher.renderSingleBlock(result, poseStack, bufferSource, packedLight, packedOverlay)
            poseStack.popPose()
        } else if (!rI.isEmpty) {
            poseStack.pushPose()
            poseStack.translate(0.5, 1.25/16f, 0.5)
            PoseStackUtil.rotateByDirection(direction, poseStack)
            poseStack.scale(0.5f, 0.5f, 0.5f)
            poseStack.mulPose(Axis.XP.rotationDegrees(90f))
            context.itemRenderer.renderStatic(rI, ItemDisplayContext.FIXED, lt, packedOverlay, poseStack, bufferSource, context.blockEntityRenderDispatcher.level, pan.blockPos.asLong().toInt())
            poseStack.popPose()
        }

        AnimHelper.Fluid.renderAnimatedFluid(pan, Direction.NORTH, 10/16f, 3/16f, 1/16f, partialTick, poseStack, bufferSource, packedLight)
    }

    private fun rotFix(direction: Direction, poseStack: PoseStack): Float {
        when (direction) {
            Direction.EAST, Direction.WEST -> {
                return -90f
            }
            else -> {
                poseStack.mulPose(Axis.ZP.rotationDegrees(180f))
                return 90f
            }
        }
    }

}