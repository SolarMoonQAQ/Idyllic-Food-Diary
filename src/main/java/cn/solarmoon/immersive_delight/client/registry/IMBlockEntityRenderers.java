package cn.solarmoon.immersive_delight.client.registry;

import cn.solarmoon.immersive_delight.client.block_entity_renderer.FurnaceRenderer;
import cn.solarmoon.solarmoon_core.registry.base.BaseEntityRendererRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class IMBlockEntityRenderers extends BaseEntityRendererRegistry {

    @Override
    public void addRegistry() {
        //熔炉渲染
        add(BlockEntityType.FURNACE, FurnaceRenderer::new);
    }

}
