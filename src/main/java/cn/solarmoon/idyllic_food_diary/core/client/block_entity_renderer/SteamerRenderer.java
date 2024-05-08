package cn.solarmoon.idyllic_food_diary.core.client.block_entity_renderer;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.inventory.SteamerInventory;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.SteamerBlockEntity;
import cn.solarmoon.solarmoon_core.api.client.blockEntityRenderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.util.PoseStackUtil;
import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;

import static cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock.FACING;

public class SteamerRenderer<E extends SteamerBlockEntity> extends BaseBlockEntityRenderer<E> {

    public SteamerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E steamer, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (!steamer.hasLid()) {
            SteamerInventory inv = (SteamerInventory)steamer.getInventory();

            if (inv.hasDouble(2)) {
                renderDoubleFood(2, steamer, poseStack, buffer, light, overlay);
            } else if (!inv.hasDouble(2)) {
                renderFood2(steamer, poseStack, buffer, light, overlay);
            }

            if (inv.hasDouble(1)) {
                renderDoubleFood(1, steamer, poseStack, buffer, light, overlay);
            } else if (!inv.hasDouble(1)) {
                renderFood(steamer, poseStack, buffer, light, overlay);
            }

        }
    }

    private void renderDoubleFood(int layer, E steamer, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Direction direction = steamer.getBlockState().getValue(FACING);
        SteamerInventory inv = (SteamerInventory)steamer.getInventory();
        ItemStack stack = inv.getDoubleStack(layer);
        BlockState state = Block.byItem(stack.getItem()).defaultBlockState();

        poseStack.pushPose();
        Vec3 center = new Vec3(0.5, 0, 0.5);
        double xOS = !state.getRenderShape().equals(RenderShape.MODEL) ? 0 : 0.375;
        Vec3 base1 = center.add(xOS, 0.4375f + (layer-1) * 0.5, -0.375 / 2);
        Vec3 v1 = VecUtil.rotateVec(base1, center, direction);
        poseStack.translate(v1.x, v1.y, v1.z);
        poseStack.scale(0.375F, 0.375F, 0.375F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        PoseStackUtil.rotateByDirection(direction, poseStack);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90));
        blockRenderDispatcher.renderSingleBlock(state.setValue(IBedPartBlock.PART, BedPart.HEAD), poseStack, buffer, light, overlay);
        poseStack.popPose();

        if (state.getRenderShape().equals(RenderShape.MODEL)) {
            poseStack.pushPose();
            Vec3 center2 = new Vec3(0.5, 0, 0.5);
            Vec3 base1_2 = center2.add(0, 0.4375f + (layer - 1) * 0.5, -0.375 / 2);
            Vec3 v1_2 = VecUtil.rotateVec(base1_2, center2, direction);
            poseStack.translate(v1_2.x, v1_2.y, v1_2.z);
            poseStack.scale(0.375F, 0.375F, 0.375F);
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            PoseStackUtil.rotateByDirection(direction, poseStack);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            blockRenderDispatcher.renderSingleBlock(state.setValue(IBedPartBlock.PART, BedPart.FOOT), poseStack, buffer, light, overlay);
            poseStack.popPose();
        }
    }

    private void renderFood(E steamer, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Direction direction = steamer.getBlockState().getValue(FACING);
        ItemStackHandler inventory = steamer.getInventory();
        Level level = steamer.getLevel();
        int posLong = (int) steamer.getBlockPos().asLong();
        for (int i = 1; i <= 4; i++) {
            ItemStack stack = inventory.getStackInSlot(i - 1);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                int c = i > 2 ? -1 : 1; //转变竖直方向
                int index = i > 2 ? i - 2 : i; //i>3时触底反弹
                int blockScale = 12;
                double scale = blockScale / 16d;
                Vec3 center = new Vec3(0.5, 0, 0.5);
                Vec3 base1 = center.add((-0.5 + 1 / 4f + 1 / 2f * (index - 1)) * scale, 0.4375, -0.25 * scale * c);
                Vec3 v1 = VecUtil.rotateVec(base1, center, direction);
                poseStack.translate(v1.x, v1.y, v1.z);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                PoseStackUtil.rotateByDirection(direction, poseStack);
                if (level != null) {
                    int lightColor = LevelRenderer.getLightColor(level, steamer.getBlockPos().above());
                    BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
                    if (!state.is(Blocks.AIR)) {
                        poseStack.scale(0.9F, 0.9F, 0.9F);
                        poseStack.translate(-0.5, -0.5, 0);
                        poseStack.mulPose(Axis.XP.rotation(-(float) Math.PI / 2));
                        blockRenderDispatcher.renderSingleBlock(state, poseStack, buffer, light, overlay);
                    } else {
                        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, lightColor, overlay, poseStack, buffer, level, posLong + i);
                    }
                }
                poseStack.popPose();
            }
        }
    }

    private void renderFood2(E steamer, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Direction direction = steamer.getBlockState().getValue(FACING).getOpposite();
        ItemStackHandler inventory = steamer.getInventory();
        Level level = steamer.getLevel();
        int posLong = (int) steamer.getBlockPos().asLong();
        for (int i = 1; i <= 4; i++) {
            ItemStack stack = inventory.getStackInSlot(i + 4 - 1);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                int c = i > 2 ? -1 : 1; //转变竖直方向
                int index = i > 2 ? i - 2 : i; //i>3时触底反弹
                int blockScale = 12;
                double scale = blockScale / 16d;
                Vec3 center = new Vec3(0.5, 0, 0.5);
                Vec3 base1 = center.add((-0.5 + 1 / 4f + 1 / 2f * (index - 1)) * scale, 0.9375, -0.25 * scale * c);
                Vec3 v1 = VecUtil.rotateVec(base1, center, direction.getOpposite());
                poseStack.translate(v1.x, v1.y, v1.z);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                PoseStackUtil.rotateByDirection(direction.getOpposite(), poseStack);
                if (level != null) {
                    int lightColor = LevelRenderer.getLightColor(level, steamer.getBlockPos().above());
                    BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
                    if (!state.is(Blocks.AIR)) {
                        poseStack.scale(0.9F, 0.9F, 0.9F);
                        poseStack.translate(-0.5, -0.5, 0);
                        poseStack.mulPose(Axis.XP.rotation(-(float) Math.PI / 2));
                        blockRenderDispatcher.renderSingleBlock(state, poseStack, buffer, light, overlay);
                    } else {
                        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, lightColor, overlay, poseStack, buffer, level, posLong + i);
                    }
                }
                poseStack.popPose();
            }
        }
    }

}
