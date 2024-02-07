package cn.solarmoon.immersive_delight.common.registry.client;

import cn.solarmoon.immersive_delight.api.registry.core.BaseEntityRendererRegistry;
import cn.solarmoon.immersive_delight.client.BlockEntityRenderer.FurnaceRenderer;
import cn.solarmoon.immersive_delight.client.BlockEntityRenderer.LittleCupRenderer;
import cn.solarmoon.immersive_delight.common.registry.IMEntityBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;


public class IMBlockEntityRenderers extends BaseEntityRendererRegistry {

    @Override
    public void addRegistry() {
        //熔炉渲染
        add(BlockEntityType.FURNACE, FurnaceRenderer::new);
        //杯子渲染
        add(IMEntityBlocks.CELADON_CUP_ENTITY.get(), LittleCupRenderer::new);
        add(IMEntityBlocks.JADE_CHINA_CUP_ENTITY.get(), LittleCupRenderer::new);
    }

}
