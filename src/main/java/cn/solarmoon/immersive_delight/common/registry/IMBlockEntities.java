package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.client.block_entity_renderer.GrillRenderer;
import cn.solarmoon.immersive_delight.client.block_entity_renderer.LittleCupRenderer;
import cn.solarmoon.immersive_delight.client.block_entity_renderer.PlateRenderer;
import cn.solarmoon.immersive_delight.common.block_entity.*;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.BlockEntityEntry;

public enum IMBlockEntities implements IRegister {
    INSTANCE;

    //小容量杯子
    public static final BlockEntityEntry<LittleCupBlockEntity> LITTLE_CUP = ImmersiveDelight.REGISTRY.blockEntity()
            .id("little_cup")
            .bound(LittleCupBlockEntity::new)
            .validBlock(IMBlocks.CELADON_CUP.getObject(), IMBlocks.JADE_CHINA_CUP.getObject())
            .renderer(LittleCupRenderer::new)
            .build();

    //锡纸盒
    public static final BlockEntityEntry<TinFoilBoxBlockEntity> TIN_FOIL_BOX = ImmersiveDelight.REGISTRY.blockEntity()
            .id("tin_foil_box")
            .bound(TinFoilBoxBlockEntity::new)
            .validBlock(IMBlocks.TIN_FOIL_BOX.getObject())
            .build();

    //水壶
    public static final BlockEntityEntry<KettleBlockEntity> KETTLE = ImmersiveDelight.REGISTRY.blockEntity()
            .id("kettle")
            .bound(KettleBlockEntity::new)
            .validBlock(IMBlocks.KETTLE.getObject())
            .build();

    //汤锅
    public static final BlockEntityEntry<SoupPotBlockEntity> SOUP_POT = ImmersiveDelight.REGISTRY.blockEntity()
            .id("soup_pot")
            .bound(SoupPotBlockEntity::new)
            .validBlock(IMBlocks.SOUP_POT.getObject())
            .build();

    //砧板
    public static final BlockEntityEntry<CuttingBoardBlockEntity> CUTTING_BOARD = ImmersiveDelight.REGISTRY.blockEntity()
            .id("cutting_board")
            .bound(CuttingBoardBlockEntity::new)
            .validBlock(IMBlocks.CUTTING_BOARD.getObject())
            .build();

    //烧烤架
    public static final BlockEntityEntry<CuttingBoardBlockEntity> GRILL = ImmersiveDelight.REGISTRY.blockEntity()
            .id("grill")
            .bound(GrillBlockEntity::new)
            .validBlock(IMBlocks.GRILL.getObject())
            .renderer(GrillRenderer::new)
            .build();

    //盘子
    public static final BlockEntityEntry<PlateBlockEntity> PLATE = ImmersiveDelight.REGISTRY.blockEntity()
            .id("plate")
            .bound(PlateBlockEntity::new)
            .validBlock(IMBlocks.PLATE.getObject())
            .renderer(PlateRenderer::new)
            .build();

}
