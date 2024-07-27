package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.registry.client.IMLayers;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MillstoneItemRenderer extends BaseItemRenderer {
    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        var context = Minecraft.getInstance().getEntityModels();
        ModelPart wheel = context.bakeLayer(IMLayers.MILLSTONE.get()).getChild("wheel");
        wheel.setRotation(0, 0, (float) -Math.PI);
        ModelPart main = context.bakeLayer(IMLayers.MILLSTONE.get()).getChild("main");
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entitySolid(new ResourceLocation(IdyllicFoodDiary.MOD_ID, "textures/block/millstone.png")));
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        wheel.render(poseStack, vertexConsumer, light, overlay);
        main.render(poseStack, vertexConsumer, light, overlay);
        poseStack.popPose();
    }
}
