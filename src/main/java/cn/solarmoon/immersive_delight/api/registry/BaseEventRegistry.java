package cn.solarmoon.immersive_delight.api.registry;

import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

/**
 * 方便快捷的事件注册表
 */
public abstract class BaseEventRegistry {

    protected List<Object> events;

    public BaseEventRegistry() {
        this.events = new ArrayList<>();
    }

    /**
     * 用events.add()添加事件类型
     */
    public abstract void addRegistry();

    public void register() {
        addRegistry();
        for (Object event : events) {
            MinecraftForge.EVENT_BUS.register(event);
        }
    }

}
