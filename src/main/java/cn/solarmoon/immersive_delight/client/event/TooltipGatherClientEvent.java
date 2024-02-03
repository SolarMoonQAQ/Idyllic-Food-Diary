package cn.solarmoon.immersive_delight.client.event;

import cn.solarmoon.immersive_delight.client.ItemRenderer.TankableTooltipRenderer;
import com.mojang.datafixers.util.Either;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TooltipGatherClientEvent {

    /**
     * tooltip渲染实际应用
     */
    @SubscribeEvent
    public void gatherTooltips(RenderTooltipEvent.GatherComponents event) {
        TankableTooltipRenderer.TankTooltip tankTooltip = new TankableTooltipRenderer.TankTooltip(event.getItemStack());
        //位置限定在系列名之上
        event.getTooltipElements().add(Math.max(event.getTooltipElements().size() - 1, 1) , Either.right(tankTooltip));
    }

}
