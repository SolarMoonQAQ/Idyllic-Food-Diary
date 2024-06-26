package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.RendererUtil;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CookingPotBlockRenderer<E extends CookingPotBlockEntity> extends BaseBlockEntityRenderer<E> {

    public CookingPotBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E pot, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

        TextureRenderUtil.renderAnimatedFluid(8/16f, 11/16f, 2/16f, pot, poseStack, buffer, light);

        RendererUtil.renderItemStackStack(0.4f, 0.6f, 45, 2, 0.35f,
                pot, poseStack, buffer, light, overlay, context);

    }

}
