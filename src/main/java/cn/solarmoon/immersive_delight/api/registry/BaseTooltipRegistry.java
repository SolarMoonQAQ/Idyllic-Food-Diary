package cn.solarmoon.immersive_delight.api.registry;

import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

/**
 * 基本的tooltip渲染层注册表
 */
public abstract class BaseTooltipRegistry {

    public abstract void register(RegisterClientTooltipComponentFactoriesEvent event);

}
