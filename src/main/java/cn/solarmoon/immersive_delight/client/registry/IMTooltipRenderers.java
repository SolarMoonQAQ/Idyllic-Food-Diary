package cn.solarmoon.immersive_delight.client.registry;

import cn.solarmoon.immersive_delight.client.tooltip.TankableTooltipRenderer;
import cn.solarmoon.solarmoon_core.registry.base.BaseTooltipRegistry;

public class IMTooltipRenderers extends BaseTooltipRegistry {

    @Override
    public void addRegistry() {
        add(TankableTooltipRenderer.TankTooltip.class, TankableTooltipRenderer::new);
    }

    @Override
    public void gatherComponents() {
        gather(new TankableTooltipRenderer.TankTooltip(itemStack), Math.max(gatherEvent.getTooltipElements().size() - 1, 1));
    }

}
