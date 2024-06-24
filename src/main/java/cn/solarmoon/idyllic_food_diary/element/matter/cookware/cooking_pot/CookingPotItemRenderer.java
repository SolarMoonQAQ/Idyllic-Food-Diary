package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileItemContainerHelper;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemStackHandler;

public class CookingPotItemRenderer extends BaseItemRenderer {
    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        BlockItem item = ((BlockItem) itemStack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);

        TextureRenderUtil.renderStaticFluid(8/16f, 11/16f, 2/16f, itemStack, poseStack, buffer, light);

        TileItemContainerHelper.getInventory(itemStack).ifPresent(inv -> {
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack in = inv.getStackInSlot(i);
                double x;
                double h = 2/16F + 0.25/16F * i; // 高度
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
                poseStack.mulPose(Axis.YP.rotationDegrees(- 45 * i));
                poseStack.scale(0.35f, 0.35f, 0.35f);
                poseStack.translate(0, h, 0);
                poseStack.mulPose(Axis.XN.rotationDegrees(-90));
                poseStack.mulPose(Axis.ZN.rotationDegrees(180));
                itemRenderer.renderStatic(in, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
                poseStack.popPose();
            }
        });
    }
}
