package cn.solarmoon.immersive_delight.network;


import cn.solarmoon.immersive_delight.api.network.Pack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMPack {

    //注册数据包
    @SubscribeEvent
    public static void packRegister(final FMLCommonSetupEvent event) {
        Pack.ToServer packServer = new Pack.ToServer();
        packServer.register();
        Pack.ToClient packClient = new Pack.ToClient();
        packClient.register();
    }

}
