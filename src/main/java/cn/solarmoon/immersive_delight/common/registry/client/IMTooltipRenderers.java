package cn.solarmoon.immersive_delight.common.registry.client;

import cn.solarmoon.immersive_delight.api.registry.core.BaseTooltipRegistry;
import cn.solarmoon.immersive_delight.client.ItemRenderer.TankableTooltipRenderer;

public class IMTooltipRenderers extends BaseTooltipRegistry {

    @Override
    public void addRegistry() {
        add(TankableTooltipRenderer.TankTooltip.class, TankableTooltipRenderer::new);
    }

}
