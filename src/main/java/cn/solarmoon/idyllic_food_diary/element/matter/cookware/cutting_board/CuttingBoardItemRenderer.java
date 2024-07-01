package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.RendererUtil;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CuttingBoardItemRenderer extends BaseItemRenderer {

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        blockRenderer.renderSingleBlock(Block.byItem(itemStack.getItem()).defaultBlockState(), poseStack, buffer, light, overlay);
        RendererUtil.renderItemStackStack(0.4f, 45, 1, 0.5f,
                itemStack, poseStack, buffer, light, overlay);
    }

}
