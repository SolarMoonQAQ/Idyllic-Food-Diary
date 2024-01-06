package cn.solarmoon.immersive_delight.common;

import cn.solarmoon.immersive_delight.common.entity_blocks.renderer.FurnaceRenderer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;


@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMBlockEntityRenderers {

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //熔炉渲染
        event.registerBlockEntityRenderer(BlockEntityType.FURNACE, FurnaceRenderer::new);
    }

}
