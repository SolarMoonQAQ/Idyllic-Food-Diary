package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.AbstractCupBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.phys.PoseStackHelper;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;

public class LittleCupBlockRenderer extends BaseBlockEntityRenderer<LittleCupBlockEntity> {

    public LittleCupBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(LittleCupBlockEntity cup, float tickDelta, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        // 渲染流体过渡动画
        AnimHelper.Fluid.renderAnimatedFluid(cup, null, 3/16f, 3f/16f, 1.5f/16f, tickDelta, poseStack, buffer, light);

        //渲染物品
        //让光度和环境一致
        Direction direction = cup.getBlockState().getValue(IHorizontalFacingBlock.FACING);
        light = cup.getLevel() != null ? LevelRenderer.getLightColor(cup.getLevel(), cup.getBlockPos().above()) : 15728880;
        poseStack.pushPose();
        PoseStackHelper.rotateByDirection(direction, poseStack);
        poseStack.translate(0.5, 2/16f, 0.5);
        poseStack.scale(3/16f, 3/16f, 3/16f);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        context.getItemRenderer().renderStatic(cup.getInventory().getStackInSlot(0), ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, cup.getLevel(), (int) cup.getBlockPos().asLong());
        poseStack.popPose();
    }

}