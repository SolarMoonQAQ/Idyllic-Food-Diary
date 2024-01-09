package cn.solarmoon.immersive_delight.client;

import cn.solarmoon.immersive_delight.client.events.RollingPinClientEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;


@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMClientEvents {

    //客户端事件订阅
    @SubscribeEvent
    public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
        //擀面杖
        r(new RollingPinClientEvent());
    }

    public static void r(Object target) {
        MinecraftForge.EVENT_BUS.register(target);
    }

}
