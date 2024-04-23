package cn.solarmoon.idyllic_food_diary.client.Item_renderer;

import cn.solarmoon.idyllic_food_diary.common.registry.IMItems;
import cn.solarmoon.idyllic_food_diary.data.tags.IMItemTags;
import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CuttingBoardItemRenderer extends BaseItemRenderer {

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        blockRenderer.renderSingleBlock(Block.byItem(itemStack.getItem()).defaultBlockState(), poseStack, buffer, light, overlay);
        ItemStack stack = ContainerUtil.getInventory(itemStack).getStackInSlot(0);
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.0625, 0.625f);
        poseStack.scale(1,1,1);
        BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
        poseStack.mulPose(Axis.XP.rotationDegrees(-90));
        if (!state.is(Blocks.AIR)) {
            poseStack.translate(-0.2, 0.3, 0);
            poseStack.scale(0.35F, 0.35F, 0.35F);
            poseStack.mulPose(Axis.XP.rotation((float) Math.PI / 2));
            blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);
        } else if (stack.is(IMItems.CHINESE_CLEAVER.get())) {
            poseStack.mulPose(Axis.XP.rotation((float) -Math.PI));
            poseStack.mulPose(Axis.YN.rotationDegrees(45));
            poseStack.translate(-0.3, 0.05, -0.164);
            itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, null, 0);
        } else if (stack.is(IMItemTags.FORGE_KNIVES) || stack.is(IMItemTags.FORGE_CLEAVERS)) {
            poseStack.mulPose(Axis.XP.rotation((float) -Math.PI / 2));
            poseStack.translate(-0.05, -0.3, 0.1);
            itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, null, 0);
        } else {
            poseStack.mulPose(Axis.XP.rotation((float) Math.PI));
            poseStack.translate(0, -0.2, 0);
            itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, null, 0);
        }
        poseStack.popPose();
    }

}
