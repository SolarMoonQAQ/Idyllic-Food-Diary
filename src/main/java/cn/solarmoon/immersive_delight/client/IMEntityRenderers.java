package cn.solarmoon.immersive_delight.client;

import cn.solarmoon.immersive_delight.client.EntityRenderer.DurianEntityRenderer;
import cn.solarmoon.immersive_delight.common.IMEntityTypes;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMEntityRenderers {

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //榴莲
        event.registerEntityRenderer(IMEntityTypes.DURIAN_ENTITY.get(), DurianEntityRenderer::new);
    }

}
