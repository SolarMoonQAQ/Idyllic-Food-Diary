package cn.solarmoon.idyllic_food_diary.element.matter.cookware.service_plate;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.RendererUtil;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ServicePlateBlockRenderer extends BaseBlockEntityRenderer<ServicePlateBlockEntity> {

    public ServicePlateBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ServicePlateBlockEntity plate, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        RendererUtil.renderItemStackStack(0.5f, 45, 1, 0.5f,
                plate, poseStack, buffer, light, overlay, context);
    }

    @Override
    public boolean shouldRenderOffScreen(ServicePlateBlockEntity p_112306_) {
        return true;
    }

}
