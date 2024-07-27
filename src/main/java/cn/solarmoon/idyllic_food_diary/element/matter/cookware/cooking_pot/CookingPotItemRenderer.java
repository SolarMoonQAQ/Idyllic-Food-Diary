package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.RendererUtil;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class CookingPotItemRenderer extends BaseItemRenderer {
    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        BlockItem item = ((BlockItem) itemStack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);

        // 液体
        itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(tank -> {
            AnimHelper.Fluid.renderStaticFluid(8/16f, 6/16f, 2/16f, tank, poseStack, buffer, light);
        });

        RendererUtil.renderItemStackStack(0.4f, 45, 2, 0.35f,
                itemStack, poseStack, buffer, light, overlay);
    }
}
