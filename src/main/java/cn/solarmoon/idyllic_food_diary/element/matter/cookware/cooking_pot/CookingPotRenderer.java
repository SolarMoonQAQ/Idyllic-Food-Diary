package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.idyllic_food_diary.util.TextureRenderUtil;
import cn.solarmoon.solarmoon_core.api.client.renderer.blockEntity.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.common.capability.IBlockEntityData;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.block_entity.AnimTicker;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import cn.solarmoon.solarmoon_core.api.util.SMath;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class CookingPotRenderer<E extends CookingPotBlockEntity> extends BaseBlockEntityRenderer<E> {

    public CookingPotRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E pot, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        FluidTank tank = pot.getTank();
        FluidStack fluidStack = tank.getFluidInTank(0);
        IBlockEntityData data = pot.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).orElse(null);
        if (data == null) return;
        AnimTicker animTicker1 = data.getAnimTicker(1);
        AnimTicker animTicker2 = data.getAnimTicker(2);
        animTicker2.setStartOnChanged(false);

        if (fluidStack.isEmpty()) {
            fluidStack = animTicker1.getFixedFluid();
        } // 使得液体从有到无也能渲染过渡动画

        // 渲染流体过渡动画
        int targetColor = TextureRenderUtil.getColor(fluidStack);
        IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation spriteLocation = fluidAttributes.getStillTexture(fluidStack);

        poseStack.pushPose();
        int ticks = animTicker1.getTicks();
        float targetScale = FluidUtil.getScale(pot.getTank());
        float h0 = animTicker1.getFixedValue(); // 当前液体高度
        if (targetScale > 0 && !animTicker1.isEnabled() && h0 == 0) {
            h0 = targetScale;
            animTicker1.setFixedFluid(fluidStack);
        } // 这一段防止放置的方块没有animTicker而不显示液体 / 同时防止液体没保存而无动画
        float h1 = targetScale; // 目标液体高度
        float dh = h1 - h0; // 当前液体与目标液体的高度比例差
        animTicker1.setMaxTick(10);
        int maxTicks = animTicker1.getMaxTick();
        float progress = ((float) ticks / maxTicks);
        float pstScale = h0 + SMath.smoothInterpolation(progress, 0, dh, 0.1f); // 当前的渲染高度比例，使用平滑插值
        float H = pstScale * 11/16f;
        poseStack.translate(0, 2/16f, 0);
        if (spriteLocation != null) {
            TextureRenderUtil.render(spriteLocation,
                    4, 4, 12, 12, 8/16f, H,
                    targetColor, 1, 0, poseStack, buffer, light);
        }
        poseStack.popPose();
        animTicker1.setFixedValue(pstScale);
        if (!fluidStack.isEmpty()) {
            if (h0 < 0.05) animTicker1.setFixedFluid(FluidStack.EMPTY);
            else animTicker1.setFixedFluid(fluidStack);
        } // 使得液体从有到无也能渲染过渡动画

        //渲染物品
        //让光度和环境一致
        light = pot.getLevel() != null ? LevelRenderer.getLightColor(pot.getLevel(), pot.getBlockPos().above()) : 15728880;
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
            itemRenderer.renderStatic(pot.getInventory().getStackInSlot(i), ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, pot.getLevel(), (int) pot.getBlockPos().asLong());
            poseStack.popPose();
        }

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
