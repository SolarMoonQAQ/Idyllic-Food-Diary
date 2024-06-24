package cn.solarmoon.idyllic_food_diary.registry.client;

import cn.solarmoon.idyllic_food_diary.feature.cupcake_tooltip.TankableTooltipRenderer;
import cn.solarmoon.solarmoon_core.api.entry.client.BaseTooltipRegistry;
import com.mojang.datafixers.util.Either;

public class IMTooltipRenderers extends BaseTooltipRegistry {

    @Override
    public void addRegistry() {
        add(TankableTooltipRenderer.TooltipComponent.class, TankableTooltipRenderer::new);
    }

    @Override
    public void gatherComponents() {
        gatherEvent.getTooltipElements().add(1, Either.right(new TankableTooltipRenderer.TooltipComponent(gatherEvent.getItemStack())));
    }

}
