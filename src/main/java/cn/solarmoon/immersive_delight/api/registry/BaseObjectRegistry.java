package cn.solarmoon.immersive_delight.api.registry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class BaseObjectRegistry<T> {

    protected DeferredRegister<T> objects;

    public BaseObjectRegistry(DeferredRegister<T> objects) {
        this.objects = objects;
    }

    public void register(IEventBus bus) {
        objects.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
    }

}
