package cn.solarmoon.immersive_delight.common.registry.client;

import cn.solarmoon.immersive_delight.api.registry.core.BaseEntityRendererRegistry;
import cn.solarmoon.immersive_delight.client.EntityRenderer.DurianEntityRenderer;
import cn.solarmoon.immersive_delight.common.registry.IMEntityTypes;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class IMEntityRenderers extends BaseEntityRendererRegistry {

    @Override
    public void addRegistry() {
        //榴莲
        add(IMEntityTypes.DURIAN_ENTITY.get(), DurianEntityRenderer::new);
    }

}
