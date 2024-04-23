package cn.solarmoon.idyllic_food_diary.client.block_entity_renderer;

import cn.solarmoon.idyllic_food_diary.common.block_entity.CuttingBoardBlockEntity;
import cn.solarmoon.idyllic_food_diary.common.registry.IMItems;
import cn.solarmoon.idyllic_food_diary.data.tags.IMItemTags;
import cn.solarmoon.solarmoon_core.api.client.blockEntityRenderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.util.PoseStackUtil;
import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CuttingBoardRenderer<E extends CuttingBoardBlockEntity> extends BaseBlockEntityRenderer<E> {

    public CuttingBoardRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E board, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        ItemStack stack = board.getInventory().getStackInSlot(0);
        Direction direction = board.getBlockState().getValue(AbstractFurnaceBlock.FACING);
        poseStack.pushPose();
        poseStack.scale(1,1,1);
        Vec3 center = new Vec3(0.5, 0, 0.5);
        Vec3 base1 = center.add(0, 0.0625, -0.125);
        Vec3 v1 = VecUtil.rotateVec(base1, center, direction.getOpposite());
        poseStack.translate(v1.x, v1.y, v1.z);
        PoseStackUtil.rotateByDirection(direction.getOpposite(), poseStack);
        BlockState state = Block.byItem(stack.getItem()).defaultBlockState();
        if (!state.is(Blocks.AIR)) {
            poseStack.scale(0.35F, 0.35F, 0.35F);
            poseStack.translate(0.5, 0.15, 0);
            poseStack.mulPose(Axis.XP.rotation(-(float) Math.PI / 2));
            poseStack.mulPose(Axis.YP.rotation((float) Math.PI));
            blockRenderDispatcher.renderSingleBlock(state, poseStack, buffer, light, overlay);
        } else if (stack.is(IMItems.CHINESE_CLEAVER.get())) {
            poseStack.mulPose(Axis.XP.rotation((float) -Math.PI));
            poseStack.mulPose(Axis.YN.rotationDegrees(45));
            poseStack.mulPose(Axis.YP.rotation((float) Math.PI));
            poseStack.translate(-0.3, 0.29, -0.164);
            itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, level, (int) board.getBlockPos().asLong());
        } else if (stack.is(IMItemTags.FORGE_KNIVES) || stack.is(IMItemTags.FORGE_CLEAVERS)) {
            poseStack.mulPose(Axis.XP.rotation((float) Math.PI / 2));
            poseStack.mulPose(Axis.YP.rotation((float) Math.PI));
            poseStack.translate(-0.05, -0.3, -0.125);
            itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, level, (int) board.getBlockPos().asLong());
        } else {
            poseStack.mulPose(Axis.XP.rotation((float) Math.PI));
            itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, level, (int) board.getBlockPos().asLong());
        }
        poseStack.popPose();
    }

}
