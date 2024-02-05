package cn.solarmoon.immersive_delight.api;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.registry.Capabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = ImmersiveDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Core {

    @SubscribeEvent
    public static void onFMLCommonSetupEvent(final FMLCommonSetupEvent event) {
        //forge能力
        new Capabilities().register();
    }

}
