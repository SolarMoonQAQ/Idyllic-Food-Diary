package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.api.AnimTicker;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.phys.PoseStackHelper;
import cn.solarmoon.solarmoon_core.api.phys.VecUtil;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileItemContainerHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class RendererUtil {

    public static void renderItemStackStack(float renderBoxVertexMin, float rotOffset, float yBlockOffset, float scale, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        TileItemContainerHelper.getInventory(stack).ifPresent(inv -> {
            for (int i = 0; i < inv.getSlots(); i++) {
                poseStack.pushPose();
                ItemStack in = inv.getStackInSlot(i);
                PoseStackHelper.rotateByDirection(Direction.NORTH, poseStack);
                float renderBoxVertexMax = 1 - renderBoxVertexMin;
                float range = renderBoxVertexMax - renderBoxVertexMin; // 宽度
                // 图形的顶点坐标
                float[][] vertices = {
                        {0.5f, (float) Math.sqrt(3) / 2},
                        {1, 0},
                        {0, 0},
                };
                // 按比例定位等边三角形的顶点
                float[] vertex = vertices[i % 3];
                double x = renderBoxVertexMin + range * vertex[0];
                double h = (yBlockOffset + scale / 2) / 16F + scale / 16F * i; // 高度
                double z = renderBoxVertexMin + range * vertex[1];
                poseStack.translate(x, h, z);
                poseStack.scale(scale, scale, scale);
                poseStack.mulPose(Axis.YN.rotationDegrees(rotOffset * i));
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                Minecraft.getInstance().getItemRenderer().renderStatic(in, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
                poseStack.popPose();
            }
        });
    }

    public static void renderItemStackStack(float renderBoxVertexMin, float rotOffset, float yBlockOffset, float scale, BlockEntity be, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, BlockEntityRendererProvider.Context context) {
        be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(inv -> {
            Direction direction = be.getBlockState().getValue(IHorizontalFacingBlock.FACING);
            for (int i = 0; i < inv.getSlots(); i++) {
                poseStack.pushPose();
                ItemStack in = inv.getStackInSlot(i);
                PoseStackHelper.rotateByDirection(direction, poseStack);
                float renderBoxVertexMax = 1 - renderBoxVertexMin;
                float range = renderBoxVertexMax - renderBoxVertexMin; // 宽度
                // 图形的顶点坐标
                float[][] vertices = {
                        {0.5f, (float) Math.sqrt(3) / 2},
                        {1, 0},
                        {0, 0},
                };
                // 按比例定位等边三角形的顶点
                float[] vertex = vertices[i % vertices.length];
                double x = renderBoxVertexMin + range * vertex[0];
                double h = (yBlockOffset + scale / 2) / 16F + scale / 16F * i; // 高度
                double z = renderBoxVertexMin + range * vertex[1];
                poseStack.translate(x, h, z);
                poseStack.scale(scale, scale, scale);
                poseStack.mulPose(Axis.YN.rotationDegrees(rotOffset * i));
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                int light0 = light;
                // 防止超出当前pos的部分光照不和超出的pos部分一致
                if (be.getLevel() != null) {
                    light0 = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above((int) h));
                }
                context.getItemRenderer().renderStatic(in, ItemDisplayContext.FIXED, light0, overlay, poseStack, buffer, be.getLevel(), (int) be.getBlockPos().asLong());
                poseStack.popPose();
            }
        });
    }

}
