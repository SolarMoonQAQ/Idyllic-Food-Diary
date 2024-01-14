package cn.solarmoon.immersive_delight.client;

import cn.solarmoon.immersive_delight.client.BlockEntityRenderer.FurnaceRenderer;
import cn.solarmoon.immersive_delight.client.BlockEntityRenderer.LittleCupRenderer;
import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
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
        //杯子渲染
        event.registerBlockEntityRenderer(IMEntityBlocks.CELADON_CUP_ENTITY.get(), LittleCupRenderer::new);
        event.registerBlockEntityRenderer(IMEntityBlocks.JADE_CHINA_CUP_ENTITY.get(), LittleCupRenderer::new);
    }

}
