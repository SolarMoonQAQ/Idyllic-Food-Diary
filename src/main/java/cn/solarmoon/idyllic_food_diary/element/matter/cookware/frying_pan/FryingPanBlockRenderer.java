package cn.solarmoon.idyllic_food_diary.element.matter.cookware.frying_pan;

import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class FryingPanBlockRenderer extends BaseBlockEntityRenderer<FryingPanBlockEntity> {

    public FryingPanBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(FryingPanBlockEntity pan, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

    }

}
