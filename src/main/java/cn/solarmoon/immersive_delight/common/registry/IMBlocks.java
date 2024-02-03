package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.api.registry.BaseObjectRegistry;
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
    public static final RegistryObject<WheatDoughBlock> WHEAT_DOUGH = BLOCKS.register("wheat_dough", WheatDoughBlock::new);
    //面饼
    public static final RegistryObject<FlatbreadDoughBlock> FLATBREAD_DOUGH = BLOCKS.register("flatbread_dough", FlatbreadDoughBlock::new);
    //藏书羊肉汤
    public static final RegistryObject<CangshuMuttonSoupBlock> CANGSHU_MUTTON_SOUP = BLOCKS.register("cangshu_mutton_soup", CangshuMuttonSoupBlock::new);

    //
    public static final RegistryObject<test> test = BLOCKS.register("test", test::new);


    //苹果树苗
    public static final RegistryObject<AppleSaplingBlock> APPLE_SAPLING = BLOCKS.register("apple_sapling", AppleSaplingBlock::new);
    //苹果
    public static final RegistryObject<AppleCrop> APPLE = BLOCKS.register("apple", AppleCrop::new);
    //绿茶
    public static final RegistryObject<GreenTeaCrop> GREEN_TEA_TREE = BLOCKS.register("green_tea_tree", GreenTeaCrop::new);
    //红茶
    public static final RegistryObject<BlackTeaCrop> BLACK_TEA_TREE = BLOCKS.register("black_tea_tree", BlackTeaCrop::new);
    //榴莲
    public static final RegistryObject<DurianCrop> DURIAN = BLOCKS.register("durian", DurianCrop::new);
    //榴莲方块
    public static final RegistryObject<DurianBlock> DURIAN_BLOCK = BLOCKS.register("durian_block", DurianBlock::new);
    //榴莲树苗
    public static final RegistryObject<DurianSaplingBlock> DURIAN_SAPLING = BLOCKS.register("durian_sapling", DurianSaplingBlock::new);
}
