package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.capability.anim_ticker.AnimTicker;
import cn.solarmoon.solarmoon_core.api.phys.SMath;
import cn.solarmoon.solarmoon_core.api.phys.VecUtil;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import cn.solarmoon.solarmoon_core.feature.capability.IBlockEntityData;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;

public class CookingPotBlockRenderer<E extends CookingPotBlockEntity> extends BaseBlockEntityRenderer<E> {

    public CookingPotBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E pot, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

        TextureRenderUtil.renderAnimatedFluid(8/16f, 11/16f, 2/16f, pot, poseStack, buffer, light);

        ItemStackHandler inv = pot.getInventory();
        Direction direction = pot.getBlockState().getValue(IHorizontalFacingBlock.FACING);
        for (int i = 0; i < inv.getSlots(); i++) {
            poseStack.pushPose();
            ItemStack in = inv.getStackInSlot(i);
            float minX = 0.4f; // 三角形绘制范围的最小值
            float maxX = 0.6f; // 绘制范围的最大值
            float range = maxX - minX; // 宽度
            // 图形的顶点坐标
            float[][] vertices = {
                    {0.5f, 0},
                    {0, (float) Math.sqrt(3) / 2},
                    {1, (float) Math.sqrt(3) / 2},
            };
            // 按比例定位等边三角形的顶点
            float[] vertex = vertices[i % 3];
            double x = minX + range * vertex[0];
            double h = 2/16F + 0.5/16F * i; // 高度
            double z = minX + range * vertex[1];
            Vec3 actV = VecUtil.rotateVec(new Vec3(x, 0, z), new Vec3(0.5, 0, 0.5), direction);
            poseStack.translate(actV.x, h, actV.z);
            poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot() - 45 * i));
            poseStack.scale(0.35f, 0.35f, 0.35f);
            poseStack.translate(0, h, 0);
            poseStack.mulPose(Axis.XN.rotationDegrees(rotFix(direction, poseStack)));
            itemRenderer.renderStatic(in, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
            poseStack.popPose();
        }

    }

    public static int rotFix(Direction direction, PoseStack poseStack) {
        switch (direction) {
            case EAST, WEST -> {
                return -90;
            }
            default -> {
                poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                return 90;
            }
        }
    }

}
