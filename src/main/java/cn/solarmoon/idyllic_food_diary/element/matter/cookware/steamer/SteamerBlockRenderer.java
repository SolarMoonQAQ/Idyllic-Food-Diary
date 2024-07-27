package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.phys.PoseStackHelper;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.math.MatrixUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.joml.Matrix3d;
import org.joml.Matrix4f;

public class SteamerBlockRenderer extends BaseBlockEntityRenderer<SteamerBlockEntity> {

    public SteamerBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SteamerBlockEntity steamer, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        int stackAmount = steamer.getStack();
        BlockState state = steamer.getBlockState();
        BlockPos pos = steamer.getBlockPos();
        Level level = steamer.getLevel();

        // 渲染每层蒸笼
        for (int i = 1; i < stackAmount; i++) {
            double h = 4 / 16f * i;
            poseStack.pushPose();
            poseStack.translate(0, h, 0);
            context.getBlockRenderDispatcher().renderSingleBlock(state, poseStack, buffer, light, overlay);
            poseStack.popPose();
        }

        // 渲染盖子
        if (steamer.hasLid()) {
            double h = 4 / 16f * stackAmount;
            poseStack.pushPose();
            poseStack.translate(0, h, 0);
            context.getBlockRenderDispatcher().renderSingleBlock(IMBlocks.STEAMER_LID.get().defaultBlockState(), poseStack, buffer, light, overlay);
            poseStack.popPose();
        }

        // 渲染物品（只渲染最上层）
        Direction direction = steamer.getBlockState().getValue(IHorizontalFacingBlock.FACING);
        if (stackAmount - 1 >= 0) {
            ItemStackHandler inv = steamer.getInvList().get(stackAmount - 1);
            for (int i = 0; i < inv.getSlots(); i++) {
                poseStack.pushPose();
                ItemStack stack = inv.getStackInSlot(i);
                PoseStackHelper.rotateByDirection(direction, poseStack);
                // 物品大小
                float scale = 4.5f / 16f;
                // 控制图形大小的矩形的始末点
                float renderBoxVertexMin = 5.75f / 16f;
                float renderBoxVertexMax = 1 - renderBoxVertexMin;
                // 图形长度
                float range = renderBoxVertexMax - renderBoxVertexMin;
                // 图形的顶点坐标
                float[][] vertices = {
                        {1, 1},
                        {0, 1},
                        {1, 0},
                        {0, 0}
                };
                // 按序列定位正方形的顶点
                float[] vertex = vertices[i % vertices.length];
                double x = renderBoxVertexMin + range * vertex[0];
                double h = 4 / 16f * stackAmount - (1 - scale / 2) / 16F;
                double z = renderBoxVertexMin + range * vertex[1];
                poseStack.translate(x, h, z);
                poseStack.scale(scale, scale, scale);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, level, (int) pos.asLong());
                poseStack.popPose();
            }
        }

    }


}
