package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.items.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.common.IMEntityBlocks.*;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.ITEMS;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMItems {

    //注册物品

    //擀面杖
    public static final RegistryObject<RollingPinItem> ROLLING_PIN = ITEMS.register("rolling_pin", RollingPinItem::new);
    //青瓷杯
    public static final RegistryObject<CeladonCupItem> CELADON_CUP = ITEMS.register(CELADON_CUP_ID, CeladonCupItem::new);
    //玉瓷杯
    public static final RegistryObject<JadeChinaCupItem> JADE_CHINA_CUP = ITEMS.register(JADE_CHINA_CUP_ID, JadeChinaCupItem::new);
    //水壶
    public static final RegistryObject<KettleItem> KETTLE = ITEMS.register(KETTLE_ID, KettleItem::new);

    //食物————————————————————————————————————————

    //面饼
    public static final RegistryObject<FlatbreadDoughItem> FLATBREAD_DOUGH = ITEMS.register("flatbread_dough", FlatbreadDoughItem::new);

    //作物————————————————————————————————————————

    //红茶叶
    public static final RegistryObject<BlackTeaLeavesItem> BLACK_TEA_LEAVES = ITEMS.register("black_tea_leaves", BlackTeaLeavesItem::new);
    //红茶种子
    public static final RegistryObject<BlackTeaSeedsItem> BLACK_TEA_SEEDS = ITEMS.register("black_tea_seeds", BlackTeaSeedsItem::new);
    //绿茶叶
    public static final RegistryObject<GreenTeaLeavesItem> GREEN_TEA_LEAVES = ITEMS.register("green_tea_leaves", GreenTeaLeavesItem::new);
    //绿茶种子
    public static final RegistryObject<GreenTeaSeedsItem> GREEN_TEA_SEEDS = ITEMS.register("green_tea_seeds", GreenTeaSeedsItem::new);
    //苹果核
    public static final RegistryObject<AppleCoreItem> APPLE_SEEDS = ITEMS.register("apple_core", AppleCoreItem::new);
    //苹果树苗
    public static final RegistryObject<AppleSaplingItem> APPLE_TREE_SAPLING = ITEMS.register("apple_sapling", AppleSaplingItem::new);
    //榴莲块
    public static final RegistryObject<DurianBlockItem> DURIAN_BLOCK = ITEMS.register("durian_block", DurianBlockItem::new);
    //榴莲种子
    public static final RegistryObject<DurianCoreItem> DURIAN_CORE = ITEMS.register("durian_core", DurianCoreItem::new);
    //榴莲树苗
    public static final RegistryObject<DurianSaplingItem> DURIAN_SAPLING = ITEMS.register("durian_sapling", DurianSaplingItem::new);
    //榴莲肉
    public static final RegistryObject<DurianFleshItem> DURIAN_FLESH = ITEMS.register("durian_flesh", DurianFleshItem::new);
    //榴莲壳
    public static final RegistryObject<DurianShellItem> DURIAN_SHELL = ITEMS.register("durian_shell", DurianShellItem::new);

}
