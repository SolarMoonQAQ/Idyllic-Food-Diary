package cn.solarmoon.idyllic_food_diary.element.matter.cookware.oven;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.ContainerHelper;
import cn.solarmoon.solarmoon_core.api.block_util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.phys.PoseStackHelper;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class OvenBlockRenderer extends BaseBlockEntityRenderer<OvenBlockEntity> {

    public OvenBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(OvenBlockEntity oven, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        ItemStack stack = oven.getInventory().getStackInSlot(0);
        ItemStack containerStack = ContainerHelper.getContainer(stack);
        BlockState container = Block.byItem(containerStack.getItem()).defaultBlockState();
        Block block = Block.byItem(stack.getItem());
        BlockState state = block.defaultBlockState();
        Direction direction = oven.getBlockState().getValue(IHorizontalFacingBlock.FACING);
        poseStack.pushPose();
        PoseStackHelper.rotateByDirection(direction, poseStack);
        int l = LevelRenderer.getLightColor(oven.getLevel(), oven.getBlockPos().relative(direction));
        if (state.isAir() || block instanceof CropBlock) {
            poseStack.translate(0.5, 1 / 16f + 0.5 / 16, 0.5);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            context.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, l, overlay, poseStack, buffer, oven.getLevel(), (int) oven.getBlockPos().asLong());
        } else {
            float zoom = 6 / 16f;
            poseStack.scale(zoom, zoom, zoom);
            poseStack.translate(5 / zoom / 16f, 1 / zoom / 16f, 6.5 / zoom / 16f);
            context.getBlockRenderDispatcher().renderSingleBlock(state, poseStack, buffer, l, overlay);
            context.getBlockRenderDispatcher().renderSingleBlock(container, poseStack, buffer, l, overlay);
            if (state.getValues().get(IBedPartBlock.PART) != null && state.getRenderShape() == RenderShape.MODEL) {
                poseStack.translate(0, 0, -1);
                context.getBlockRenderDispatcher().renderSingleBlock(state.setValue(IBedPartBlock.PART, BedPart.HEAD), poseStack, buffer, l, overlay);
                if (container.getValues().get(IBedPartBlock.PART) != null) {
                    context.getBlockRenderDispatcher().renderSingleBlock(container.setValue(IBedPartBlock.PART, BedPart.HEAD), poseStack, buffer, l, overlay);
                }
            }
        }
        poseStack.popPose();
    }

}
