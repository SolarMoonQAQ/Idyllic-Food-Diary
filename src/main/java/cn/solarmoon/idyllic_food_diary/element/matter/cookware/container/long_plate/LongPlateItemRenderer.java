package cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.long_plate;

import cn.solarmoon.solarmoon_core.api.blockstate_access.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.phys.PoseStackHelper;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileItemContainerHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.items.ItemStackHandler;

import static cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.long_plate.LongPlateBlockRenderer.ROW;

public class LongPlateItemRenderer extends BaseItemRenderer {

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = Block.byItem(itemStack.getItem()).defaultBlockState();
        blockRenderer.renderSingleBlock(state.setValue(IBedPartBlock.PART, BedPart.FOOT), poseStack, buffer, light, overlay);
        poseStack.pushPose();
        poseStack.translate(0, 0, -1);
        blockRenderer.renderSingleBlock(state.setValue(IBedPartBlock.PART, BedPart.HEAD), poseStack, buffer, light, overlay);
        poseStack.popPose();

        TileItemContainerHelper.getInventory(itemStack).ifPresent(inv -> {
            Direction direction = Direction.NORTH;
            //    1     - 1 n
            //   2 3    - 2 n + (n-1)
            //  4 5 6   - 3 n + (n-1) + (n-2)
            // 7 8 9 10 - 4 n + (n-1) + (n-2) + (n-3) 等差数列
            for (int r = 0; r < ROW; r++) {
                int Sn = (ROW - r) * ((ROW - r) + 1) / 2;
                int Sn_1 = (ROW - r - 1) * ((ROW - r - 1) + 1) / 2;
                float scale = 0.5f;
                float interval = scale / 2; // 物品间隔
                float x = r * interval / 2;
                int n = 0;
                for (int i = 0; i < inv.getSlots(); i++) {
                    if (inv.getSlots() - i - 1 > Sn_1 - 1 && inv.getSlots() - i - 1 < Sn) {
                        ItemStack stack = inv.getStackInSlot(i);
                        poseStack.pushPose();
                        PoseStackHelper.rotateByDirection(direction, poseStack);
                        poseStack.translate(0, 0, 1);
                        poseStack.mulPose(Axis.YP.rotationDegrees(90));

                        // 计算物品位置
                        float d = 7.5f;
                        double rotateH = (scale / 16f) * Math.sin(Math.toDegrees(d));
                        poseStack.translate(scale, 2 / 16f + scale / 2 / 16f + (n == 0 ? 0 : rotateH), 0.5f);
                        poseStack.translate(x, r * (scale / 16f + rotateH), 0);
                        x = x + interval;

                        poseStack.scale(scale, scale, scale);
                        poseStack.mulPose(Axis.XP.rotationDegrees(90));
                        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                        poseStack.mulPose(Axis.YP.rotationDegrees(n == 0 ? 0 : d));
                        n++;

                        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
                        poseStack.popPose();
                    }
                }
            }
        });
    }

}