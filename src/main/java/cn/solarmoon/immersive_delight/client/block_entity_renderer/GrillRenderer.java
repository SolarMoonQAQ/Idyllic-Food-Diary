package cn.solarmoon.immersive_delight.client.block_entity_renderer;

import cn.solarmoon.immersive_delight.common.block_entity.GrillBlockEntity;
import cn.solarmoon.immersive_delight.util.PoseStackUtil;
import cn.solarmoon.immersive_delight.util.VecUtil;
import cn.solarmoon.solarmoon_core.client.blockEntityRenderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.common.block.entity_block.BasicEntityBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

public class GrillRenderer <E extends GrillBlockEntity> extends BaseBlockEntityRenderer<E> {

    public GrillRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E grill, float v, PoseStack poseStack, MultiBufferSource buffer, int overlay, int i1) {
        Direction direction = grill.getBlockState().getValue(BasicEntityBlock.FACING).getOpposite();
        ItemStackHandler inventory = grill.getInventory();
        int posLong = (int) grill.getBlockPos().asLong();
        for (int i = 1; i <= 6; i++) {
            ItemStack stack = inventory.getStackInSlot(i-1);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                int c = i > 3 ? -1 : 1; //转变竖直方向
                int index = i > 3 ? i - 3 : i; //i>3时触底反弹
                Vec3 center = new Vec3(0.5, 0, 0.5);
                Vec3 base1 = center.add(-0.5 + 1/6f + 1/3f * (index - 1), 1, -0.25 * c);
                Vec3 v1 = VecUtil.rotateVec(base1, center, direction.getOpposite());
                poseStack.translate(v1.x, v1.y, v1.z);
                Level level = grill.getLevel();
                poseStack.scale(0.375F, 0.375F, 0.375F);
                PoseStackUtil.rotateByDirection(direction.getOpposite(), poseStack);
                if (grill.getLevel() != null)
                    itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(grill.getLevel(), grill.getBlockPos().above()), overlay, poseStack, buffer, grill.getLevel(), posLong + i);
                poseStack.popPose();
            }
        }
    }

}
