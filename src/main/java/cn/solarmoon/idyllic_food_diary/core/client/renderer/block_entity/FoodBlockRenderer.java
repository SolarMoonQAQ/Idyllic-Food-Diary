package cn.solarmoon.idyllic_food_diary.core.client.renderer.block_entity;

import cn.solarmoon.idyllic_food_diary.core.common.block_entity.FoodBlockEntity;
import cn.solarmoon.solarmoon_core.api.client.renderer.blockEntity.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class FoodBlockRenderer<E extends FoodBlockEntity> extends BaseBlockEntityRenderer<E> {

    public FoodBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E fb, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        BlockState container = fb.getContainerLeft();
        blockRenderDispatcher.renderSingleBlock(container, poseStack, buffer, light, overlay);
    }

}
