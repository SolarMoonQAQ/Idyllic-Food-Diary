package cn.solarmoon.immersive_delight.common.registry.client;

import cn.solarmoon.immersive_delight.api.registry.BaseTooltipRegistry;
import cn.solarmoon.immersive_delight.client.ItemRenderer.TankableTooltipRenderer;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

public class IMTooltipRenderers extends BaseTooltipRegistry {

    @Override
    public void register(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(TankableTooltipRenderer.TankTooltip.class, TankableTooltipRenderer::new);
    }

}
