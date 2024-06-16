package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.capability.anim_ticker.AnimTicker;
import cn.solarmoon.solarmoon_core.api.phys.SMath;
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

public class CookingPotBlockRenderer<E extends CookingPotBlockEntity> extends BaseBlockEntityRenderer<E> {

    public CookingPotBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E pot, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

        TextureRenderUtil.renderAnimatedFluid(8/16f, 11/16f, 2/16f, pot, poseStack, buffer, light);

        pot.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(data -> {
            AnimTicker animTicker2 = data.getAnimTicker(2);
            animTicker2.setStartOnChanged(false);
            //渲染物品
            //让光度和环境一致
            int lt = pot.getLevel() != null ? LevelRenderer.getLightColor(pot.getLevel(), pot.getBlockPos().above()) : 15728880;
            for (int i = 0; i < pot.getInventory().getSlots(); i++) {
                poseStack.pushPose();
                Direction direction = pot.getBlockState().getValue(IHorizontalFacingBlock.FACING);
                double normal = 0.0625 + 0.0625 * (i+1);
                poseStack.translate(0.5, normal, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));
                poseStack.scale(0.5f, 0.5f, 0.5f);
                if (animTicker2.isEnabled() && pot.canStirFry()) {
                    int fps = 1000; // 帧时间
                    animTicker2.setFixedValue(animTicker2.getFixedValue() + v);
                    if (animTicker2.getFixedValue() > fps) {
                        animTicker2.setFixedValue(0);
                        animTicker2.stop();
                    }
                    poseStack.translate(0, SMath.parabolaFunction(animTicker2.getFixedValue(), fps / 2f, normal + 2, normal), 0);
                    poseStack.mulPose(Axis.ZP.rotation(Mth.rotLerp(animTicker2.getFixedValue() / fps, (float) 0, (float) (2*Math.PI))));
                }
                poseStack.mulPose(Axis.XP.rotationDegrees(rotFix(direction)));
                itemRenderer.renderStatic(pot.getInventory().getStackInSlot(i), ItemDisplayContext.FIXED, lt, overlay, poseStack, buffer, pot.getLevel(), (int) pot.getBlockPos().asLong());
                poseStack.popPose();
            }
        });

    }

    public static int rotFix(Direction direction) {
        switch (direction) {
            case EAST, WEST -> {
                return 90;
            }
            default -> {
                return -90;
            }
        }
    }

}
