package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.client.block_entity_renderer.GrillRenderer;
import cn.solarmoon.immersive_delight.client.block_entity_renderer.LittleCupRenderer;
import cn.solarmoon.immersive_delight.client.block_entity_renderer.ServicePlateRenderer;
import cn.solarmoon.immersive_delight.common.block_entity.*;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.BlockEntityEntry;
import net.minecraft.world.level.block.Block;

public enum IMBlockEntities implements IRegister {
    INSTANCE;

    //小容量杯子
    public static final BlockEntityEntry<LittleCupBlockEntity> LITTLE_CUP = ImmersiveDelight.REGISTRY.blockEntity()
            .id("little_cup")
            .bound(LittleCupBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.CELADON_CUP.get(), IMBlocks.JADE_CHINA_CUP.get()})
            .renderer(LittleCupRenderer::new)
            .build();

    //水壶
    public static final BlockEntityEntry<KettleBlockEntity> KETTLE = ImmersiveDelight.REGISTRY.blockEntity()
            .id("kettle")
            .bound(KettleBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.KETTLE.get()})
            .build();

    //汤锅
    public static final BlockEntityEntry<SoupPotBlockEntity> SOUP_POT = ImmersiveDelight.REGISTRY.blockEntity()
            .id("soup_pot")
            .bound(SoupPotBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.SOUP_POT.get()})
            .build();

    //砧板
    public static final BlockEntityEntry<CuttingBoardBlockEntity> CUTTING_BOARD = ImmersiveDelight.REGISTRY.blockEntity()
            .id("cutting_board")
            .bound(CuttingBoardBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.CUTTING_BOARD.get()})
            .build();

    //烧烤架
    public static final BlockEntityEntry<CuttingBoardBlockEntity> GRILL = ImmersiveDelight.REGISTRY.blockEntity()
            .id("grill")
            .bound(GrillBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.GRILL.get()})
            .renderer(GrillRenderer::new)
            .build();

    //餐盘
    public static final BlockEntityEntry<ServicePlateBlockEntity> PLATE = ImmersiveDelight.REGISTRY.blockEntity()
            .id("service_plate")
            .bound(ServicePlateBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.SERVICE_PLATE.get()})
            .renderer(ServicePlateRenderer::new)
            .build();

    //蒸笼
    public static final BlockEntityEntry<SteamerBlockEntity> STEAMER = ImmersiveDelight.REGISTRY.blockEntity()
            .id("steamer")
            .bound(SteamerBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.STEAMER.get()})
            .build();

    //蒸笼底座
    public static final BlockEntityEntry<SteamerBaseBlockEntity> STEAMER_BASE = ImmersiveDelight.REGISTRY.blockEntity()
            .id("steamer_base")
            .bound(SteamerBaseBlockEntity::new)
            .validBlock(() -> new Block[]{IMBlocks.STEAMER_BASE.get()})
            .build();

}
