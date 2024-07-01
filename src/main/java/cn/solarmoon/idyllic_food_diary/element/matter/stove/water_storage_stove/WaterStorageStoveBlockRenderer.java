package cn.solarmoon.idyllic_food_diary.element.matter.stove.water_storage_stove;

import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class WaterStorageStoveBlockRenderer extends BaseBlockEntityRenderer<WaterStorageStoveBlockEntity> {

    public WaterStorageStoveBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(WaterStorageStoveBlockEntity ws, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {

    }

}
