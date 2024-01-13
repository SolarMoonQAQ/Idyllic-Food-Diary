package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.blocks.*;
import cn.solarmoon.immersive_delight.common.blocks.crops.AppleCrop;
import cn.solarmoon.immersive_delight.common.blocks.crops.BlackTeaCrop;
import cn.solarmoon.immersive_delight.common.blocks.crops.DurianCrop;
import cn.solarmoon.immersive_delight.common.blocks.crops.GreenTeaCrop;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMBlocks {

    //面团
    public static final RegistryObject<WheatDoughBlock> WHEAT_DOUGH = BLOCKS.register("wheat_dough", WheatDoughBlock::new);
    //面饼
    public static final RegistryObject<FlatbreadDoughBlock> FLATBREAD_DOUGH = BLOCKS.register("flatbread_dough", FlatbreadDoughBlock::new);
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
