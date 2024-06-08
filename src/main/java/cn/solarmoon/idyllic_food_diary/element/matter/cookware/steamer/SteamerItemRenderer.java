package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import cn.solarmoon.idyllic_food_diary.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.api.client.renderer.Item.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;

import static cn.solarmoon.solarmoon_core.api.common.block.IStackBlock.STACK;

public class SteamerItemRenderer extends BaseItemRenderer {

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        BlockItem item = ((BlockItem) stack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        boolean hasLid = stack.getOrCreateTag().getBoolean(NBTList.HAS_LID);
        int stackValue = Math.max(stack.getOrCreateTag().getInt(NBTList.STACK), 1);
        state = state.setValue(SteamerBlock.HAS_LID, hasLid).setValue(STACK, stackValue);
        blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);
        if (!hasLid) {
            ItemStackHandler inv = ContainerUtil.getInventory(stack);
            boolean valid1 = true;
            boolean valid2 = true;
            for (int i = 1; i <= inv.getSlots(); i++) {
                ItemStack stackIn = inv.getStackInSlot(i - 1);
                if (Block.byItem(stackIn.getItem()).defaultBlockState().getValues().get(IBedPartBlock.PART) != null) {
                    if (i < 4) {
                        renderDoubleFood(stackIn, 1, poseStack, buffer, light, overlay);
                        valid1 = false;
                    }
                    else {
                        renderDoubleFood(stackIn, 2, poseStack, buffer, light, overlay);
                        valid2 = false;
                    }
                }
            }
            if (valid1) renderFood(stack, poseStack, buffer, light, overlay);
            if (valid2) renderFood2(stack, poseStack, buffer, light, overlay);
        }
    }

    private void renderDoubleFood(ItemStack stack, int layer, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = Block.byItem(stack.getItem()).defaultBlockState();

        poseStack.pushPose();
        Vec3 center = new Vec3(0.5, 0, 0.5);
        double xOS = !state.getRenderShape().equals(RenderShape.MODEL) ? 0 : 0.375;
        Vec3 v1 = center.add(xOS, 0.4375f + (layer-1) * 0.5, -0.375 / 2);
        poseStack.translate(v1.x, v1.y, v1.z);
        poseStack.scale(0.375F, 0.375F, 0.375F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        blockRenderer.renderSingleBlock(state.setValue(IBedPartBlock.PART, BedPart.HEAD), poseStack, buffer, light, overlay);
        poseStack.popPose();

        if (state.getRenderShape().equals(RenderShape.MODEL)) {
            poseStack.pushPose();
            Vec3 center2 = new Vec3(0.5, 0, 0.5);
            Vec3 v1_2 = center2.add(0, 0.4375f + (layer - 1) * 0.5, -0.375 / 2);
            poseStack.translate(v1_2.x, v1_2.y, v1_2.z);
            poseStack.scale(0.375F, 0.375F, 0.375F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90));
            blockRenderer.renderSingleBlock(state.setValue(IBedPartBlock.PART, BedPart.FOOT), poseStack, buffer, light, overlay);
            poseStack.popPose();
        }
    }

    private void renderFood(ItemStack steamer, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        ItemStackHandler inventory = ContainerUtil.getInventory(steamer);
        for (int i = 1; i <= Math.min(4, inventory.getSlots() - 1); i++) {
            ItemStack stack = inventory.getStackInSlot(i - 1);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                int c = i > 2 ? -1 : 1; //转变竖直方向
                int index = i > 2 ? i - 2 : i; //i>3时触底反弹
                int blockScale = 12;
                double scale = blockScale / 16d;
                Vec3 center = new Vec3(0.5, 0, 0.5);
                Vec3 base1 = center.add((-0.5 + 1 / 4f + 1 / 2f * (index - 1)) * scale, 0.4375, -0.25 * scale * c);
                poseStack.translate(base1.x, base1.y, base1.z);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                if (!state.is(Blocks.AIR)) {
                    poseStack.scale(0.9F, 0.9F, 0.9F);
                    poseStack.translate(-0.5, -0.5, 0);
                    poseStack.mulPose(Axis.XP.rotation(-(float) Math.PI / 2));
                    blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);
                } else {
                    itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, null, 0);
                }
                poseStack.popPose();
            }
        }
    }

    private void renderFood2(ItemStack steamer, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        ItemStackHandler inventory = ContainerUtil.getInventory(steamer);
        for (int i = 1; i <= Math.min(4, inventory.getSlots() - 1); i++) {
            ItemStack stack = inventory.getStackInSlot(i + 4 - 1);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                int c = i > 2 ? -1 : 1; //转变竖直方向
                int index = i > 2 ? i - 2 : i; //i>3时触底反弹
                int blockScale = 12;
                double scale = blockScale / 16d;
                Vec3 center = new Vec3(0.5, 0, 0.5);
                Vec3 base1 = center.add((-0.5 + 1 / 4f + 1 / 2f * (index - 1)) * scale, 0.9375, -0.25 * scale * c);
                poseStack.translate(base1.x, base1.y, base1.z);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                if (!state.is(Blocks.AIR)) {
                    poseStack.scale(0.9F, 0.9F, 0.9F);
                    poseStack.translate(-0.5, -0.5, 0);
                    poseStack.mulPose(Axis.XP.rotation(-(float) Math.PI / 2));
                    blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);
                } else {
                    itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, null, 0);
                }
                poseStack.popPose();
            }
        }
    }

}
