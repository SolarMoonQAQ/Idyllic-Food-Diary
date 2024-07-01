package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SteamerItemRenderer extends BaseItemRenderer {

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        int stackAmount = SteamerItem.getStackAmount(stack);
        BlockState state = IMBlocks.STEAMER.get().defaultBlockState();

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, buffer, light, overlay);

        // 渲染每层蒸笼
        for (int i = 1; i < stackAmount; i++) {
            double h = 4 / 16f * i;
            poseStack.pushPose();
            poseStack.translate(0, h, 0);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, buffer, light, overlay);
            poseStack.popPose();
        }

        // 渲染盖子
        ItemStack lid = SteamerItem.getLid(stack);
        if (!lid.isEmpty()) {
            double h = 4 / 16f * stackAmount;
            poseStack.pushPose();
            poseStack.translate(0, h, 0);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(IMBlocks.STEAMER_LID.get().defaultBlockState(), poseStack, buffer, light, overlay);
            poseStack.popPose();
        }

    }

}
