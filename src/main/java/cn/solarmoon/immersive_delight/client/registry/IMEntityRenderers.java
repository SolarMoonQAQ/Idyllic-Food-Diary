package cn.solarmoon.immersive_delight.client.registry;

import cn.solarmoon.immersive_delight.client.entity_renderer.DurianEntityRenderer;
import cn.solarmoon.immersive_delight.common.registry.IMEntityTypes;
import cn.solarmoon.solarmoon_core.registry.base.BaseEntityRendererRegistry;

public class IMEntityRenderers extends BaseEntityRendererRegistry {

    @Override
    public void addRegistry() {
        //榴莲
        add(IMEntityTypes.DURIAN_ENTITY.get(), DurianEntityRenderer::new);
    }

}
