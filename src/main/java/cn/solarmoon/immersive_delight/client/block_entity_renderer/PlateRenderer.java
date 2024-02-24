package cn.solarmoon.immersive_delight.client.block_entity_renderer;

import cn.solarmoon.immersive_delight.common.block_entity.PlateBlockEntity;
import cn.solarmoon.solarmoon_core.client.blockEntityRenderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class PlateRenderer<E extends PlateBlockEntity> extends BaseBlockEntityRenderer<E> {

    public PlateRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E plate, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

    }

}
