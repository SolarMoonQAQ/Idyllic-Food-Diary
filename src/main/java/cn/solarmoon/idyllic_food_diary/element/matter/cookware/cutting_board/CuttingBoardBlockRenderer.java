package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.RendererUtil;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CuttingBoardBlockRenderer extends BaseBlockEntityRenderer<CuttingBoardBlockEntity> {

    public CuttingBoardBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CuttingBoardBlockEntity board, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        RendererUtil.renderItemStackStack(0.4f, 45, 1, 0.5f,
                board, poseStack, buffer, light, overlay, context);
    }

}
