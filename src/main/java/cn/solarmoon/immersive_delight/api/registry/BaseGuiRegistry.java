package cn.solarmoon.immersive_delight.api.registry;

import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;

/**
 * 基本的gui覆盖层注册类
 */
public abstract class BaseGuiRegistry {

    public abstract void register(RegisterGuiOverlaysEvent event);

}
