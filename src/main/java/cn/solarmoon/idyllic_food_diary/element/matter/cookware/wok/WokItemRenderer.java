package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.RendererUtil;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class WokItemRenderer extends BaseItemRenderer {
    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        blockRenderer.renderSingleBlock(Block.byItem(stack.getItem()).defaultBlockState(), poseStack, buffer, light, overlay);

        // 液体
        stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(tank -> {
            AnimHelper.Fluid.renderStaticFluid(10/16f, 3/16f, 1/16f, tank, poseStack, buffer, light);
        });

        RendererUtil.renderItemStackStack(0.4f, 60, 1, 0.5f,
                stack, poseStack, buffer, light, overlay);
    }
}
