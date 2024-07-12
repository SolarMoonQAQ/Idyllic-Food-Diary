package cn.solarmoon.idyllic_food_diary.element.matter.cookware.fermenter;

import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class FermenterBlockRenderer extends BaseBlockEntityRenderer<FermenterBlockEntity> {

    public FermenterBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(FermenterBlockEntity f, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

    }

}
