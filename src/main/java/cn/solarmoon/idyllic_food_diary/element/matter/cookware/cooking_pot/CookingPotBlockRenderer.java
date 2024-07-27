package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareTileRenderer;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.RendererUtil;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CookingPotBlockRenderer<E extends CookingPotBlockEntity> extends CookwareTileRenderer<E> {

    public CookingPotBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void originRender(E pot, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

        AnimHelper.Fluid.renderAnimatedFluid(pot, null, 8/16f, 6/16f, 2/16f, v, poseStack, buffer, light);

        RendererUtil.renderItemStackStack(0.4f, 45, 2, 0.35f,
                pot, poseStack, buffer, light, overlay, context);

    }

}
