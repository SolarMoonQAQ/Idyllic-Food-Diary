package cn.solarmoon.immersive_delight.api.registry;

import net.minecraftforge.client.event.EntityRenderersEvent;

/**
 * 基本的实体渲染注册表（包括方块实体等）
 */
public abstract class BaseEntityRendererRegistry {

    public abstract void register(EntityRenderersEvent.RegisterRenderers event);

}
