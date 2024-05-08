package cn.solarmoon.idyllic_food_diary.core.client.block_entity_renderer;

import cn.solarmoon.idyllic_food_diary.api.util.FluidRenderUtil;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.SoupPotBlockEntity;
import cn.solarmoon.solarmoon_core.api.client.blockEntityRenderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import cn.solarmoon.solarmoon_core.api.util.PoseStackUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class SoupPotRenderer<E extends SoupPotBlockEntity> extends BaseBlockEntityRenderer<E> {

    public SoupPotRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E pot, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

        IFluidHandler tank = FluidUtil.getTank(pot);
        if (tank == null || tank.getFluidInTank(0) == null) {
            return;
        }
        FluidStack fluidStack = tank.getFluidInTank(0);

        // 渲染流体过渡动画
        float zoom = 10f / 16f; //液体方块比例缩放
        int ticks = pot.getTicks();
        float progress = ticks * 0.01f;
        float targetScale = FluidUtil.getScale(pot.getTank());
        pot.setLast(targetScale - (targetScale - pot.getLast()) * (1 - progress));

        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation spriteLocation = fluidAttributes.getStillTexture(fluidStack);

        int targetColor = FluidRenderUtil.getColor(fluidStack);

        poseStack.pushPose();
        float posB = 3f / 16f; //坐标偏移
        poseStack.translate(posB, 0.0625f, posB);
        poseStack.scale(zoom, pot.getLast() * 0.9375f, zoom); //中间为最大高度
        if (spriteLocation != null) {
            FluidRenderUtil.renderFluid(targetColor, 0, spriteLocation, poseStack, buffer, light, overlay);
        }
        poseStack.popPose();

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
