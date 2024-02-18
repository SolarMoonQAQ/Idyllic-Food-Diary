package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.block.DurianBlock;
import cn.solarmoon.immersive_delight.common.block.GarlicBlock;
import cn.solarmoon.immersive_delight.common.block.crop.*;
import cn.solarmoon.immersive_delight.common.block.entity_block.*;
import cn.solarmoon.immersive_delight.common.block.longPressEatBlock.CangshuMuttonSoupBlock;
import cn.solarmoon.immersive_delight.common.block.longPressEatBlock.FlatbreadDoughBlock;
import cn.solarmoon.immersive_delight.common.block.longPressEatBlock.WheatDoughBlock;
import cn.solarmoon.immersive_delight.common.block.sapling.AppleSaplingBlock;
import cn.solarmoon.immersive_delight.common.block.sapling.DurianSaplingBlock;
import cn.solarmoon.immersive_delight.common.block.test;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.BlockEntry;

public enum IMBlocks implements IRegister {
    INSTANCE;

    //面团
    public static final BlockEntry<WheatDoughBlock> WHEAT_DOUGH = ImmersiveDelight.REGISTRY.block()
            .id("wheat_dough")
            .bound(WheatDoughBlock::new)
            .build();

    //面饼
    public static final BlockEntry<FlatbreadDoughBlock> FLATBREAD_DOUGH = ImmersiveDelight.REGISTRY.block()
            .id("flatbread_dough")
            .bound(FlatbreadDoughBlock::new)
            .build();

    //藏书羊肉汤
    public static final BlockEntry<CangshuMuttonSoupBlock> CANGSHU_MUTTON_SOUP = ImmersiveDelight.REGISTRY.block()
            .id("cangshu_mutton_soup")
            .bound(CangshuMuttonSoupBlock::new)
            .build();

    //
    public static final BlockEntry<test> test = ImmersiveDelight.REGISTRY.block()
            .id("test")
            .bound(test::new)
            .build();

    //苹果树苗
    public static final BlockEntry<AppleSaplingBlock> APPLE_SAPLING = ImmersiveDelight.REGISTRY.block()
            .id("apple_sapling")
            .bound(AppleSaplingBlock::new)
            .build();

    //苹果作物
    public static final BlockEntry<AppleCropBlock> APPLE_CROP = ImmersiveDelight.REGISTRY.block()
            .id("apple_crop")
            .bound(AppleCropBlock::new)
            .build();

    //绿茶
    public static final BlockEntry<GreenTeaTreeCropBlock> GREEN_TEA_TREE = ImmersiveDelight.REGISTRY.block()
            .id("green_tea_tree")
            .bound(GreenTeaTreeCropBlock::new)
            .build();

    //红茶
    public static final BlockEntry<BlackTeaTreeCropBlock> BLACK_TEA_TREE = ImmersiveDelight.REGISTRY.block()
            .id("black_tea_tree")
            .bound(BlackTeaTreeCropBlock::new)
            .build();

    //榴莲作物
    public static final BlockEntry<DurianCropBlock> DURIAN_CROP = ImmersiveDelight.REGISTRY.block()
            .id("durian_crop")
            .bound(DurianCropBlock::new)
            .build();

    //榴莲方块
    public static final BlockEntry<DurianBlock> DURIAN = ImmersiveDelight.REGISTRY.block()
            .id("durian")
            .bound(DurianBlock::new)
            .build();

    //榴莲树苗
    public static final BlockEntry<DurianSaplingBlock> DURIAN_SAPLING = ImmersiveDelight.REGISTRY.block()
            .id("durian_sapling")
            .bound(DurianSaplingBlock::new)
            .build();

    //大蒜作物
    public static final BlockEntry<GarlicBlock> GARLIC_CROP = ImmersiveDelight.REGISTRY.block()
            .id("garlic_crop")
            .bound(GarlicCropBlock::new)
            .build();

    //大蒜
    public static final BlockEntry<GarlicBlock> GARLIC = ImmersiveDelight.REGISTRY.block()
            .id("garlic")
            .bound(GarlicBlock::new)
            .build();

    //————————————————————————————————————————————————————————————————————————————————//

    //青瓷杯
    public static final BlockEntry<CeladonCupEntityBlock> CELADON_CUP = ImmersiveDelight.REGISTRY.block()
            .id("celadon_cup")
            .bound(CeladonCupEntityBlock::new)
            .build();

    //玉瓷杯
    public static final BlockEntry<JadeChinaCupEntityBlock> JADE_CHINA_CUP = ImmersiveDelight.REGISTRY.block()
            .id("jade_china_cup")
            .bound(JadeChinaCupEntityBlock::new)
            .build();

    //水壶
    public static final BlockEntry<KettleEntityBlock> KETTLE = ImmersiveDelight.REGISTRY.block()
            .id("kettle")
            .bound(KettleEntityBlock::new)
            .build();

    //汤锅
    public static final BlockEntry<SoupPotEntityBlock> SOUP_POT = ImmersiveDelight.REGISTRY.block()
            .id("soup_pot")
            .bound(SoupPotEntityBlock::new)
            .build();

    //砧板
    public static final BlockEntry<CuttingBoardEntityBlock> CUTTING_BOARD = ImmersiveDelight.REGISTRY.block()
            .id("cutting_board")
            .bound(CuttingBoardEntityBlock::new)
            .build();

    //烧烤架
    public static final BlockEntry<CuttingBoardEntityBlock> GRILL = ImmersiveDelight.REGISTRY.block()
            .id("grill")
            .bound(GrillEntityBlock::new)
            .build();

}
