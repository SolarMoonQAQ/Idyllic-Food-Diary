package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.capability.anim_ticker.AnimTicker;
import cn.solarmoon.solarmoon_core.api.phys.SMath;
import cn.solarmoon.solarmoon_core.api.phys.VecUtil;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
        pan.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(data -> {
            //渲染物品
            //让光度和环境一致
            int lt = pan.getLevel() != null ? LevelRenderer.getLightColor(pan.getLevel(), pan.getBlockPos().above()) : 15728880;
            for (int i = 0; i < pan.getStacks().size(); i++) {
                AnimTicker animTicker = data.getAnimTicker(i+2);
                AnimTicker animR = data.getAnimTicker(-(i+2));
                animTicker.setStartOnChanged(false);
                poseStack.pushPose();
                Direction direction = pan.getBlockState().getValue(IHorizontalFacingBlock.FACING);
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
                double h = 1/16F + 0.5/16F * i; // 高度
                double z = minX + range * vertex[1];
                Vec3 actV = VecUtil.rotateVec(new Vec3(x, 0, z), new Vec3(0.5, 0, 0.5), direction);
                poseStack.translate(actV.x, h, actV.z);
                poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot() - 60 * i));
                poseStack.scale(0.5f, 0.5f, 0.5f);
                if (animTicker.isEnabled() && pan.canStirFry()) {
                    float aH = (float) (12/16f + 0.25 * i);
                    animTicker.setMaxTick(getFryTime(aH) + 30);
                    animTicker.setFixedValue(animTicker.getFixedValue() + Minecraft.getInstance().getDeltaFrameTime());
                    if (animTicker.getFixedValue() > getFryTime(aH)) {
                        // 每一片食材落下后产生火花
                        for (int n = 0; n < 1; n++) {
                            Random random = new Random();
                            double rInRange = 2/16f + random.nextDouble() * 12/16; // 保证粒子起始点在锅内
                            double vi = (random.nextDouble() - 0.5) / 5;
                            pan.getLevel().addParticle(ParticleTypes.SMALL_FLAME, pan.getBlockPos().getX() + rInRange, pan.getBlockPos().getY() + h, pan.getBlockPos().getZ() + rInRange, vi, 0.1, vi);
                        }
                        animTicker.stop();
                    }
                    poseStack.translate(0, SMath.parabolaFunction(animTicker.getFixedValue(), getFryTime(aH) / 2f, aH, 0), 0);

                    float randomR = animR.getFixedValue();
                    float angle = (float) (2.0f * Math.PI * randomR);
                    poseStack.mulPose(Axis.of(new Vector3f((float) Math.cos(angle), 0, (float) Math.sin(angle)))
                            .rotation(Mth.rotLerp(animTicker.getFixedValue() / getFryTime(aH), (float) 0, (float) (2*Math.PI))));
                }
                poseStack.mulPose(Axis.XN.rotationDegrees(rotFix(direction, poseStack)));
                itemRenderer.renderStatic(pan.getStacks().get(i), ItemDisplayContext.FIXED, lt, overlay, poseStack, buffer, level, (int) pan.getBlockPos().asLong());
                poseStack.popPose();
            }
        });

        TextureRenderUtil.renderAnimatedFluid(10/16f, 3/16f, 1/16f, pan, poseStack, buffer, light);
    }

    /**
     * @return 自由落体时间
     */
    public static int getFryTime(double height) {
        double g = 9.8;
        double t = Math.sqrt(height / g);
        return (int) (t * 30);
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

    @Override
    public boolean shouldRenderOffScreen(WokBlockEntity p_112306_) {
        return true;
    }

}
