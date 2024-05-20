package cn.solarmoon.idyllic_food_diary.core.client.registry;

import cn.solarmoon.idyllic_food_diary.core.client.renderer.tooltip.TankableTooltipRenderer;
import cn.solarmoon.solarmoon_core.api.client.registry.BaseTooltipRegistry;

public class IMTooltipRenderers extends BaseTooltipRegistry {

    @Override
    public void addRegistry() {
        add(TankableTooltipRenderer.TooltipComponent.class, TankableTooltipRenderer::new);
    }

    @Override
    public void gatherComponents() {
        gatherToFirstEmpty(TankableTooltipRenderer.TooltipComponent::new);
    }

}
