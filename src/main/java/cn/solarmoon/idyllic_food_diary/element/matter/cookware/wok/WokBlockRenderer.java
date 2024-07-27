package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.api.AnimTicker;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareTileRenderer;
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.IBuiltInStove;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.IStirFryRecipe;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.phys.SMath;
import cn.solarmoon.solarmoon_core.api.phys.VecUtil;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Random;

public class WokBlockRenderer extends CookwareTileRenderer<WokBlockEntity> {

    public WokBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void originRender(WokBlockEntity pan, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        //渲染物品
        //让光度和环境一致
        int lt = pan.getLevel() != null ? LevelRenderer.getLightColor(pan.getLevel(), pan.getBlockPos().above()) : 15728880;
        for (int i = 0; i < pan.getStacks().size(); i++) {
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
            double h = 1.25/16F + 0.5/16F * i; // 高度
            double z = minX + range * vertex[1];
            Vec3 actV = VecUtil.rotateVec(new Vec3(x, 0, z), new Vec3(0.5, 0, 0.5), direction);
            poseStack.translate(actV.x, h, actV.z);
            poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot() - 60 * i));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            AnimTicker anim = AnimHelper.getMap(pan).get("fry" + i);
            if (anim != null && anim.getTimer().getTiming()) {
                poseStack.translate(0,
                        SMath.parabolaFunction(
                                Math.min(anim.getTimer().getProgress(v), 1),
                                1 / 2f,
                                anim.getFixedValues().getOrDefault("maxHeight", 0f),
                                0
                        ), 0);
                float randomR = anim.getFixedValues().getOrDefault("rotRandom", 0f);
                float angle = (float) (2.0f * Math.PI * randomR);
                poseStack.mulPose(Axis.of(new Vector3f((float) Math.cos(angle), 0, (float) Math.sin(angle)))
                        .rotation(Mth.rotLerp(Math.min(anim.getTimer().getProgress(v), 1), (float) 0, (float) (2*Math.PI))));
            }
            poseStack.mulPose(Axis.XN.rotationDegrees(rotFix(direction, poseStack)));
            context.getItemRenderer().renderStatic(pan.getStacks().get(i), ItemDisplayContext.FIXED, lt, overlay, poseStack, buffer, context.getBlockEntityRenderDispatcher().level, (int) pan.getBlockPos().asLong());
            poseStack.popPose();
        }

        AnimHelper.Fluid.renderAnimatedFluid(pan, null, 10/16f, 3/16f, 1/16f, v, poseStack, buffer, light);
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
