package cn.solarmoon.idyllic_food_diary.element.matter.cookware.fermentation_vat;

import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class FermentationVatBlockRenderer extends BaseBlockEntityRenderer<FermentationVatBlockEntity> {

    public FermentationVatBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(FermentationVatBlockEntity f, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

    }

}
