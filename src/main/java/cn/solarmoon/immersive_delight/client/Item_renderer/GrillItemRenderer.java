package cn.solarmoon.immersive_delight.client.Item_renderer;

import cn.solarmoon.solarmoon_core.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.common.block.entity_block.BasicEntityBlock;
import cn.solarmoon.solarmoon_core.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.util.PoseStackUtil;
import cn.solarmoon.solarmoon_core.util.VecUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;

public class GrillItemRenderer extends BaseItemRenderer {
    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BlockItem item = ((BlockItem) stack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);
        renderFood(ContainerUtil.getInventory(stack), poseStack, buffer, light, overlay);
        renderCoal(ContainerUtil.getInventory(stack), poseStack, buffer, light, overlay);
    }

    private void renderFood(ItemStackHandler inventory, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        for (int i = 1; i <= inventory.getSlots() - 1; i++) {
            ItemStack stack = inventory.getStackInSlot(i - 1);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                int c = i > 3 ? -1 : 1; //转变竖直方向
                int index = i > 3 ? i - 3 : i; //i>3时触底反弹
                Vec3 center = new Vec3(0.5, 0, 0.5);
                Vec3 base1 = center.add(-0.5 + 1 / 6f + 1 / 3f * (index - 1), 0.9375, -0.2 * c);
                poseStack.translate(base1.x, base1.y, base1.z);
                poseStack.scale(0.65F, 0.65F, 0.65F);
                poseStack.mulPose(Axis.XP.rotationDegrees(-90));
                itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, null, 0);
                poseStack.popPose();
            }
        }
    }

    private void renderCoal(ItemStackHandler inventory, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (inventory.getSlots() == 7) {
            ItemStack coal = inventory.getStackInSlot(6);
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            for (int i = 0; i < coal.getCount() / 8; i++) {
                poseStack.pushPose();
                Vec3 center = new Vec3(0.5, 0, 0.5);
                int c = i > 3 ? -1 : 1;
                int index = i > 3 ? i - 4 : i;
                Vec3 base1 = center.add(-0.5 + 1 / 8f + 1 / 4f * index, 0.875, -0.2 * c);
                poseStack.translate(base1.x, base1.y, base1.z);
                poseStack.mulPose(Axis.XP.rotationDegrees(-90));
                poseStack.scale(0.6F, 0.6F, 0.6F);
                itemRenderer.renderStatic(coal, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, null, 0);
                poseStack.popPose();
            }
        }
    }
}
