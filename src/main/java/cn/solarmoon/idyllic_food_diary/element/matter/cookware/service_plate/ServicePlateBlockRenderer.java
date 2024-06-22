package cn.solarmoon.idyllic_food_diary.element.matter.cookware.service_plate;

import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ServicePlateBlockRenderer<E extends ServicePlateBlockEntity> extends BaseBlockEntityRenderer<E> {

    public ServicePlateBlockRenderer(BlockEntityRendererProvider.Context context) {
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
            if (i % 2 != 0) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(90));
            }
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, plate.getLevel(), (int) plate.getBlockPos().asLong());
            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(E p_112306_) {
        return true;
    }

    @Override
    public boolean shouldRender(ServicePlateBlockEntity p_173568_, Vec3 p_173569_) {
        return true;
    }

}
