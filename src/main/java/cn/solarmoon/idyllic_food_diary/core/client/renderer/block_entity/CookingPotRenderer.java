package cn.solarmoon.idyllic_food_diary.core.client.renderer.block_entity;

import cn.solarmoon.idyllic_food_diary.api.util.TextureRenderUtil;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.CookingPotBlockEntity;
import cn.solarmoon.solarmoon_core.api.client.renderer.blockEntity.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.common.capability.IBlockEntityData;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.block_entity.AnimTicker;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import cn.solarmoon.solarmoon_core.api.util.PoseStackUtil;
import cn.solarmoon.solarmoon_core.api.util.SMath;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
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
        AnimTicker animTicker = data.getAnimTicker();

        if (fluidStack.isEmpty()) {
            fluidStack = animTicker.getFixedFluid();
        } // 使得液体从有到无也能渲染过渡动画

        // 渲染流体过渡动画
        int targetColor = TextureRenderUtil.getColor(fluidStack);
        IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation spriteLocation = fluidAttributes.getStillTexture(fluidStack);

        poseStack.pushPose();
        int ticks = animTicker.getTicks();
        float targetScale = FluidUtil.getScale(pot.getTank());
        float h0 = animTicker.getFixedValue(); // 当前液体高度
        if (targetScale > 0 && !animTicker.isEnabled() && h0 == 0) {
            h0 = targetScale;
            animTicker.setFixedFluid(fluidStack);
        } // 这一段防止放置的方块没有animTicker而不显示液体 / 同时防止液体没保存而无动画
        float h1 = targetScale; // 目标液体高度
        float dh = h1 - h0; // 当前液体与目标液体的高度比例差
        animTicker.setMaxTick(10);
        int maxTicks = animTicker.getMaxTick();
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
        animTicker.setFixedValue(pstScale);
        if (!fluidStack.isEmpty()) {
            if (h0 < 0.05) animTicker.setFixedFluid(FluidStack.EMPTY);
            else animTicker.setFixedFluid(fluidStack);
        } // 使得液体从有到无也能渲染过渡动画

        //渲染物品
        //让光度和环境一致
        light = pot.getLevel() != null ? LevelRenderer.getLightColor(pot.getLevel(), pot.getBlockPos().above()) : 15728880;
        for (int i = 0; i < pot.getInventory().getSlots(); i++) {
            poseStack.pushPose();
            Direction direction = pot.getBlockState().getValue(IHorizontalFacingBlock.FACING);
            poseStack.translate(0.5, 0.0625 + 0.0625 * i, 0.5);
            PoseStackUtil.rotateByDirection(direction, poseStack);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            itemRenderer.renderStatic(pot.getInventory().getStackInSlot(i), ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, pot.getLevel(), (int) pot.getBlockPos().asLong());
            poseStack.popPose();
        }

    }

}
