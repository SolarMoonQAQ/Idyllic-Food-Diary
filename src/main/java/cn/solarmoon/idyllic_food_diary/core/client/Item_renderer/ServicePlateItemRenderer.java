package cn.solarmoon.idyllic_food_diary.core.client.Item_renderer;

import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class ServicePlateItemRenderer extends BaseItemRenderer {

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        BlockItem item = ((BlockItem) itemStack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);

        ItemStackHandler inventory = ContainerUtil.getInventory(itemStack);
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            poseStack.pushPose();
            poseStack.translate(0.5, 0.03125 * i + 0.0625, 0.5);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            if (i % 2 != 0) {
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
            }
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
            poseStack.popPose();
        }
    }

}
