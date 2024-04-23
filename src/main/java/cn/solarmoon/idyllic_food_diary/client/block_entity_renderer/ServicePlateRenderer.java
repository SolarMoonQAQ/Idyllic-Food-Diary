package cn.solarmoon.idyllic_food_diary.client.block_entity_renderer;

import cn.solarmoon.idyllic_food_diary.common.block_entity.ServicePlateBlockEntity;
import cn.solarmoon.solarmoon_core.api.client.blockEntityRenderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.util.PoseStackUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ServicePlateRenderer<E extends ServicePlateBlockEntity> extends BaseBlockEntityRenderer<E> {

    public ServicePlateRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E plate, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Direction direction = plate.getBlockState().getValue(IHorizontalFacingBlock.FACING);
        for (int i = 0; i < plate.getInventory().getSlots(); i++) {
            ItemStack stack = plate.getInventory().getStackInSlot(i);
            poseStack.pushPose();
            poseStack.translate(0.5, 0.03125 * i + 0.0625, 0.5);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            PoseStackUtil.rotateByDirection(direction, poseStack);
            if (i % 2 != 0) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(90));
            }
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, plate.getLevel(), (int) plate.getBlockPos().asLong());
            poseStack.popPose();
        }
    }

}
