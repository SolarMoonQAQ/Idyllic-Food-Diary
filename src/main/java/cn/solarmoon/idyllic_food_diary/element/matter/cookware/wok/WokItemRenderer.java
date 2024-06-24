package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok;

import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileItemContainerHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class WokItemRenderer extends BaseItemRenderer {
    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        blockRenderer.renderSingleBlock(Block.byItem(stack.getItem()).defaultBlockState(), poseStack, buffer, light, overlay);

        TextureRenderUtil.renderStaticFluid(10/16f, 3/16f, 1/16f, stack, poseStack, buffer, light);

        TileItemContainerHelper.getInventory(stack).ifPresent(inv -> {
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack in = inv.getStackInSlot(i);
                double x;
                double h = 1/16F + 0.25/16F * i; // 高度
                double z;
                float minX = 0.4f; // 三角形绘制范围的最小值
                float maxX = 0.6f; // 绘制范围的最大值
                float range = maxX - minX; // 宽度
                // 图形的顶点坐标
                float[][] vertices = {
                        {0.5f, 0},
                        {0, (float) Math.sqrt(3) / 2},
                        {1, (float) Math.sqrt(3) / 2},
                };
                // 按比例定位等边三角形的顶点
                float[] vertex = vertices[i % 3];
                x = minX + range * vertex[0];
                z = minX + range * vertex[1];
                poseStack.pushPose();
                poseStack.translate(x, h, z);
                poseStack.mulPose(Axis.YP.rotationDegrees(- 60 * i));
                poseStack.scale(0.5f, 0.5f, 0.5f);
                poseStack.translate(0, h, 0);
                poseStack.mulPose(Axis.XN.rotationDegrees(-90));
                poseStack.mulPose(Axis.ZN.rotationDegrees(180));
                itemRenderer.renderStatic(in, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
                poseStack.popPose();
            }
        });
    }
}
