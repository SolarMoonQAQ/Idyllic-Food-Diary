package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok;

import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.capability.anim_ticker.AnimTicker;
import cn.solarmoon.solarmoon_core.api.phys.SMath;
import cn.solarmoon.solarmoon_core.api.phys.VecUtil;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
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
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Random;

public class WokBlockRenderer extends BaseBlockEntityRenderer<WokBlockEntity> {

    public WokBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(WokBlockEntity pan, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        TextureRenderUtil.renderAnimatedFluid(8/16f, 4/16f, 1/16f, pan, poseStack, buffer, light);

        pan.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(data -> {
            AnimTicker animTicker2 = data.getAnimTicker(2);
            animTicker2.setStartOnChanged(false);
            //渲染物品
            //让光度和环境一致
            int lt = pan.getLevel() != null ? LevelRenderer.getLightColor(pan.getLevel(), pan.getBlockPos().above()) : 15728880;
            for (int i = 0; i < pan.getInventory().getSlots(); i++) {
                poseStack.pushPose();
                Direction direction = pan.getBlockState().getValue(IHorizontalFacingBlock.FACING);
                double x;
                double h = 1/16F + 0.5/16F * i; // 高度
                double z;
                float minX = 0.45f; // 三角形绘制范围的最小值
                float maxX = 0.55f; // 绘制范围的最大值
                float range = maxX - minX; // 宽度
                // 等边三角形的顶点坐标
                float[][] vertices = {
                        {0.5f, 0},
                        {0, (float) Math.sqrt(3) / 2},
                        {1, (float) Math.sqrt(3) / 2},
                };
                // 按比例定位等边三角形的顶点
                float[] vertex = vertices[i % 3];
                x = minX + range * vertex[0];
                z = minX + range * vertex[1];
                Vec3 actV = VecUtil.rotateVec(new Vec3(x, 0, z), new Vec3(0.5, 0, 0.5), direction);
                poseStack.translate(actV.x, h, actV.z);
                poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot() - 60 * i));
                poseStack.scale(0.5f, 0.5f, 0.5f);
                if (animTicker2.isEnabled() && pan.canStirFry()) {
                    int fps = 600; // 帧时间
                    animTicker2.setFixedValue(animTicker2.getFixedValue() + v);
                    if (animTicker2.getFixedValue() > fps) {
                        animTicker2.setFixedValue(0);
                        animTicker2.stop();
                    }
                    poseStack.translate(0, SMath.parabolaFunction(animTicker2.getFixedValue(), fps / 2f, h + 2 + 0.5 * i, h), 0);
                    float randomR = data.getAnimTicker(i+3).getFixedValue();
                    float angle = (float) (2.0f * Math.PI * randomR);
                    poseStack.mulPose(Axis.of(new Vector3f((float) Math.cos(angle), 0, (float) Math.sin(angle))).rotation(Mth.rotLerp(animTicker2.getFixedValue() / fps, (float) 0, (float) (2*Math.PI))));
                }
                poseStack.mulPose(Axis.XN.rotationDegrees(rotFix(direction)));
                itemRenderer.renderStatic(pan.getInventory().getStackInSlot(i), ItemDisplayContext.FIXED, lt, overlay, poseStack, buffer, level, (int) pan.getBlockPos().asLong());
                poseStack.popPose();
            }
        });
    }

    public static int rotFix(Direction direction) {
        switch (direction) {
            case EAST, WEST -> {
                return -90;
            }
            default -> {
                return 90;
            }
        }
    }

}
