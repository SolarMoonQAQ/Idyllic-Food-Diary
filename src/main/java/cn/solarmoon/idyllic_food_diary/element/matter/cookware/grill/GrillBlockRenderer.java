package cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill;

import cn.solarmoon.solarmoon_core.api.client.renderer.blockEntity.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.common.block.ILitBlock;
import cn.solarmoon.solarmoon_core.api.util.PoseStackUtil;
import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;

import static cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock.FACING;

public class GrillBlockRenderer<E extends GrillBlockEntity> extends BaseBlockEntityRenderer<E> {

    public GrillBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E grill, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        renderFood(grill, poseStack, buffer, light, overlay);
        renderCoal(grill, poseStack, buffer, overlay);
        renderFire(grill, poseStack, buffer, light, overlay);
    }

    private void renderFood(E grill, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Direction direction = grill.getBlockState().getValue(FACING).getOpposite();
        ItemStackHandler inventory = grill.getInventory();
        Level level = grill.getLevel();
        int posLong = (int) grill.getBlockPos().asLong();
        for (int i = 1; i <= 6; i++) {
            ItemStack stack = inventory.getStackInSlot(i - 1);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                int c = i > 3 ? -1 : 1; //转变竖直方向
                int index = i > 3 ? i - 3 : i; //i>3时触底反弹
                int blockScale = 14;
                double scale = blockScale / 16d;
                Vec3 center = new Vec3(0.5, 0, 0.5);
                Vec3 base1 = center.add((-0.5 + 1 / 6f + 1 / 3f * (index - 1)) * scale, 0.9375, -0.25 * scale * c);
                Vec3 v1 = VecUtil.rotateVec(base1, center, direction.getOpposite());
                poseStack.translate(v1.x, v1.y, v1.z);
                poseStack.scale(0.325F, 0.325F, 0.325F);
                PoseStackUtil.rotateByDirection(direction.getOpposite(), poseStack);
                if (level != null) {
                    BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
                    if (!state.is(Blocks.AIR)) {
                        poseStack.scale(0.9F, 0.9F, 0.9F);
                        poseStack.translate(-0.5, -0.5, 0);
                        poseStack.mulPose(Axis.XP.rotation(-(float) Math.PI / 2));
                        blockRenderDispatcher.renderSingleBlock(state, poseStack, buffer, light, overlay);
                    } else {
                        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(level, grill.getBlockPos().above()), overlay, poseStack, buffer, level, posLong + i);
                    }
                }
                poseStack.popPose();
            }
        }
    }

    private void renderCoal(E grill, PoseStack poseStack, MultiBufferSource buffer, int overlay) {
        Direction direction = grill.getBlockState().getValue(FACING).getOpposite();
        ItemStackHandler inventory = grill.getInventory();
        int posLong = (int) grill.getBlockPos().asLong();
        ItemStack coal = inventory.getStackInSlot(6);

        int renderCount = Math.min(((coal.getCount() / 8) + 1), 8);
        for (int i = 0; i < renderCount; i++) {
            poseStack.pushPose();
            Vec3 center = new Vec3(0.5, 0, 0.5);
            int c = i > 3 ? -1 : 1;
            int index = i > 3 ? i - 4 : i;
            Vec3 base1 = center.add(-0.5 + 1 / 8f + 1 / 4f * index, 0.875, -0.2 * c);
            Vec3 v1 = VecUtil.rotateVec(base1, center, direction.getOpposite());
            poseStack.translate(v1.x, v1.y, v1.z);
            poseStack.scale(0.275F, 0.275F, 0.275F);
            PoseStackUtil.rotateByDirection(direction.getOpposite(), poseStack);
            if (grill.getLevel() != null)
                itemRenderer.renderStatic(coal, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(grill.getLevel(), grill.getBlockPos().above()), overlay, poseStack, buffer, grill.getLevel(), posLong + i);
            poseStack.popPose();
        }
    }

    private void renderFire(E grill, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (grill.getBlockState().getValue(ILitBlock.LIT)) {
            poseStack.pushPose();
            poseStack.translate(0, 0.875, 0);
            poseStack.scale(0.9f, 0.05f, 0.9f);
            blockRenderDispatcher.renderSingleBlock(Blocks.FIRE.defaultBlockState(), poseStack, buffer, light, overlay);
            poseStack.popPose();
        }
    }

}
