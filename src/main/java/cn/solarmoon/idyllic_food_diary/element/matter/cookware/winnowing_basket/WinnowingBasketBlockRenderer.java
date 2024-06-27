package cn.solarmoon.idyllic_food_diary.element.matter.cookware.winnowing_basket;

import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class WinnowingBasketBlockRenderer extends BaseBlockEntityRenderer<WinnowingBasketBlockEntity> {

    public WinnowingBasketBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(WinnowingBasketBlockEntity wb, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

    }

}
