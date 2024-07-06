package cn.solarmoon.idyllic_food_diary.element.matter.cookware.oven;

import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class OvenBlockRenderer extends BaseBlockEntityRenderer<OvenBlockEntity> {

    public OvenBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(OvenBlockEntity oven, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {

    }

}
