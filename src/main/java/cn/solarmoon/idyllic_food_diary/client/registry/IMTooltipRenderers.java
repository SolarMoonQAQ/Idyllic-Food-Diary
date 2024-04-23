package cn.solarmoon.idyllic_food_diary.client.registry;

import cn.solarmoon.idyllic_food_diary.client.tooltip.TankableTooltipRenderer;
import cn.solarmoon.solarmoon_core.api.registry.base.BaseTooltipRegistry;

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
