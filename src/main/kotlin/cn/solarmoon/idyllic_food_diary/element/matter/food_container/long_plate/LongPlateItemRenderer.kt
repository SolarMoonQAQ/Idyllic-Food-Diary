package cn.solarmoon.idyllic_food_diary.element.matter.food_container.long_plate

import cn.solarmoon.spark_core.api.blockstate.IBedPartState
import cn.solarmoon.spark_core.api.cap.item.TileItemContainerHelper
import cn.solarmoon.spark_core.api.renderer.HandyItemRenderer
import cn.solarmoon.spark_core.api.util.PoseStackUtil
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.properties.BedPart
import kotlin.math.sin

class LongPlateItemRenderer: HandyItemRenderer() {

    val row = 5

    override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val state = Block.byItem(stack.item).defaultBlockState()
        blockRenderer.renderSingleBlock(state.setValue(IBedPartState.PART, BedPart.FOOT), poseStack, buffer, packedLight, packedOverlay)
        poseStack.pushPose()
        poseStack.translate(0.0, 0.0, -1.0)
        blockRenderer.renderSingleBlock(state.setValue(IBedPartState.PART, BedPart.HEAD), poseStack, buffer, packedLight, packedOverlay)
        poseStack.popPose()

        val access = Minecraft.getInstance().level?.registryAccess() ?: return
        TileItemContainerHelper.getInventory(stack, access)?.let { inv ->
            val direction = Direction.NORTH
            //    1     - 1 n
            //   2 3    - 2 n + (n-1)
            //  4 5 6   - 3 n + (n-1) + (n-2)
            // 7 8 9 10 - 4 n + (n-1) + (n-2) + (n-3) 等差数列
            for (r in 0 until row) {
                val Sn = (row - r) * ((row - r) + 1) / 2;
                val Sn_1 = (row - r - 1) * ((row - r - 1) + 1) / 2;
                val scale = 0.5f;
                val interval = scale / 2; // 物品间隔
                var x = r * interval / 2;
                var n = 0;
                for (i in 0 until inv.slots) {
                    if (inv.slots - i - 1 > Sn_1 - 1 && inv.slots - i - 1 < Sn) {
                        val stack = inv.getStackInSlot(i);
                        poseStack.pushPose();
                        PoseStackUtil.rotateByDirection(direction, poseStack);
                        poseStack.translate(0.0, 0.0, 1.0);
                        poseStack.mulPose(Axis.YP.rotationDegrees(90f));

                        // 计算物品位置
                        val d = 7.5f;
                        val rotateH = (scale / 16f) * sin(Math.toDegrees(d.toDouble()))
                        poseStack.translate(scale, 2 / 16f + scale / 2 / 16f + (if (n == 0) 0f else rotateH.toFloat()), 0.5f)
                        poseStack.translate(x.toDouble(), r * (scale / 16f + rotateH), 0.0);
                        x = x + interval;

                        poseStack.scale(scale, scale, scale);
                        poseStack.mulPose(Axis.XP.rotationDegrees(90f));
                        poseStack.mulPose(Axis.ZP.rotationDegrees(180f));
                        poseStack.mulPose(Axis.YP.rotationDegrees(if (n == 0) 0f else d))
                        n++;

                        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, null, 0);
                        poseStack.popPose();
                    }
                }
            }
        }
    }

}