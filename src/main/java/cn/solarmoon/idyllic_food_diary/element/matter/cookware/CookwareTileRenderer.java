package cn.solarmoon.idyllic_food_diary.element.matter.cookware;

import cn.solarmoon.idyllic_food_diary.element.matter.stove.IBuiltInStove;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CookwareTileRenderer<E extends BlockEntity> extends BaseBlockEntityRenderer<E> {

    public CookwareTileRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public abstract void originRender(E be, float deltaTick, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay);

    @Override
    public void render(E be, float deltaTick, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = be.getBlockState();

        if (state.getBlock() instanceof IBuiltInStove bis && bis.isNestedInStove(be.getBlockState())) {
            Direction direction = state.getValue(IHorizontalFacingBlock.FACING);
            bis.translateContent(direction, poseStack, buffer, light, overlay);
        }

        context.getBlockRenderDispatcher().renderSingleBlock(state, poseStack, buffer, light, overlay);
        originRender(be, deltaTick, poseStack, buffer, light, overlay);
    }

}
