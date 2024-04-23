package cn.solarmoon.idyllic_food_diary.client.block_entity_renderer;

import cn.solarmoon.solarmoon_core.api.util.PoseStackUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class FurnaceRenderer<E extends AbstractFurnaceBlockEntity> implements BlockEntityRenderer<E> {

    private final ItemRenderer itemRenderer;

    public FurnaceRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(E e, float tickDelta, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

        Level level = e.getLevel();

        //让光度和环境一致
        light = level != null ? LevelRenderer.getLightColor(e.getLevel(), e.getBlockPos().above()) : 15728880;

        ItemStack stack0 = e.getItem(0),
                  stack1 = e.getItem(1),
                  stack2 = e.getItem(2);

        poseStack.pushPose();
        poseStack.translate(0.5f, 1.01f, 0.5f);
        poseStack.scale(1,1,1);
        PoseStackUtil.rotateByDirection(e.getBlockState().getValue(AbstractFurnaceBlock.FACING), poseStack);
        itemRenderer.renderStatic(stack0, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, level, (int) e.getBlockPos().asLong());
        poseStack.popPose();

    }

}
