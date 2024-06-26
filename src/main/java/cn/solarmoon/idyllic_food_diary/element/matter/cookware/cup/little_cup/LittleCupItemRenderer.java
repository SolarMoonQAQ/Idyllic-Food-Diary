package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup;

import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileItemContainerHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class LittleCupItemRenderer extends BaseItemRenderer {

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

        BlockItem item = ((BlockItem) stack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);

        // 物品
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5/16f, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        ItemStackHandler inventory = TileItemContainerHelper.getInventory(stack).orElse(new ItemStackHandler());
        itemRenderer.renderStatic(inventory.getStackInSlot(0), ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, null, 0);
        poseStack.popPose();

        // 液体
        TextureRenderUtil.renderStaticFluid(3/16f, 3/16f, 1.5f/16, stack, poseStack, buffer, light);

    }

}
