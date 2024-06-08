package cn.solarmoon.idyllic_food_diary.element.matter.cookware.stove;

import cn.solarmoon.solarmoon_core.api.client.renderer.blockEntity.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.common.block.ILitBlock;
import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StoveBlockRenderer extends BaseBlockEntityRenderer<StoveBlockEntity> {

    public StoveBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(StoveBlockEntity stove, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        BlockPos pos = stove.getBlockPos();
        BlockState state = stove.getBlockState();
        Direction direction = state.getValue(IHorizontalFacingBlock.FACING);

        if (!stove.getPotItem().isEmpty()) {
            light = level != null ? LevelRenderer.getLightColor(level, stove.getBlockPos().above()) : 15728880;
            poseStack.pushPose();
            poseStack.translate(0, 10/16f, 0);
            if (stove.getPot() != null) {
                BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(stove.getPot());
                if (renderer != null) renderer.render(stove.getPot(), v, poseStack, buffer, light, overlay);
            }
            blockRenderDispatcher.renderSingleBlock(Block.byItem(stove.getPotItem().getItem()).defaultBlockState()
                    .setValue(IHorizontalFacingBlock.FACING, stove.getBlockState().getValue(IHorizontalFacingBlock.FACING)),
                    poseStack, buffer, light, overlay);
            poseStack.popPose();
        }

        // 火！
        if (state.getValue(ILitBlock.LIT)) {
            poseStack.pushPose();
            Vec3 location = VecUtil.rotateVec(new Vec3(5/16f, 2/16f, 4/16f),
                    new Vec3(0.5, 2/16f, 0.5), direction);
            poseStack.translate(location.x, location.y, location.z);
            poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot()));
            poseStack.scale(6/16f, 7/16f, 12/16f);
            blockRenderDispatcher.renderSingleBlock(Blocks.FIRE.defaultBlockState(), poseStack, buffer, light, overlay);
            poseStack.popPose();
        }

    }

}
