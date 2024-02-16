package cn.solarmoon.immersive_delight.client.entity_renderer;

import cn.solarmoon.immersive_delight.common.entity.DurianEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public class DurianEntityRenderer<T extends DurianEntity> extends EntityRenderer<T> {

    private final ItemRenderer itemRenderer;


    public DurianEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(T entity, float pEntityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        this.itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY,
                poseStack, buffer, entity.level(), 0);
        super.render(entity, pEntityYaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }


}
