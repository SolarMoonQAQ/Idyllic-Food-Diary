package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.api.registry.core.BaseObjectRegistry;
import cn.solarmoon.immersive_delight.common.block.*;
import cn.solarmoon.immersive_delight.common.block.crop.AppleCrop;
import cn.solarmoon.immersive_delight.common.block.crop.BlackTeaCrop;
import cn.solarmoon.immersive_delight.common.block.crop.DurianCrop;
import cn.solarmoon.immersive_delight.common.block.crop.GreenTeaCrop;
import cn.solarmoon.immersive_delight.common.block.longPressEatBlock.CangshuMuttonSoupBlock;
import cn.solarmoon.immersive_delight.common.block.longPressEatBlock.FlatbreadDoughBlock;
import cn.solarmoon.immersive_delight.common.block.longPressEatBlock.WheatDoughBlock;
import cn.solarmoon.immersive_delight.common.block.sapling.AppleSaplingBlock;
import cn.solarmoon.immersive_delight.common.block.sapling.DurianSaplingBlock;
import cn.solarmoon.immersive_delight.util.namespace.BLOCKList;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;

@SuppressWarnings("unused")
public class IMBlocks extends BaseObjectRegistry<Block> {

    //方块
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public IMBlocks() {
        super(BLOCKS);
    }

    //面团
    public static final RegistryObject<WheatDoughBlock> WHEAT_DOUGH = BLOCKS.register(BLOCKList.WHEAT_DOUGH, WheatDoughBlock::new);
    //面饼
    public static final RegistryObject<FlatbreadDoughBlock> FLATBREAD_DOUGH = BLOCKS.register(BLOCKList.FLATBREAD_DOUGH, FlatbreadDoughBlock::new);
    //藏书羊肉汤
    public static final RegistryObject<CangshuMuttonSoupBlock> CANGSHU_MUTTON_SOUP = BLOCKS.register(BLOCKList.CANGSHU_MUTTON_SOUP, CangshuMuttonSoupBlock::new);

    //
    public static final RegistryObject<test> test = BLOCKS.register("test", test::new);


    //苹果树苗
    public static final RegistryObject<AppleSaplingBlock> APPLE_SAPLING = BLOCKS.register(BLOCKList.APPLE_SAPLING, AppleSaplingBlock::new);
    //苹果
    public static final RegistryObject<AppleCrop> APPLE = BLOCKS.register("apple_crop", AppleCrop::new);
    //绿茶
    public static final RegistryObject<GreenTeaCrop> GREEN_TEA_TREE = BLOCKS.register("green_tea_tree", GreenTeaCrop::new);
    //红茶
    public static final RegistryObject<BlackTeaCrop> BLACK_TEA_TREE = BLOCKS.register("black_tea_tree", BlackTeaCrop::new);
    //榴莲
    public static final RegistryObject<DurianCrop> DURIAN_CROP = BLOCKS.register("durian_crop", DurianCrop::new);
    //榴莲方块
    public static final RegistryObject<DurianBlock> DURIAN = BLOCKS.register(BLOCKList.DURIAN, DurianBlock::new);
    //榴莲树苗
    public static final RegistryObject<DurianSaplingBlock> DURIAN_SAPLING = BLOCKS.register(BLOCKList.DURIAN_SAPLING, DurianSaplingBlock::new);
}
