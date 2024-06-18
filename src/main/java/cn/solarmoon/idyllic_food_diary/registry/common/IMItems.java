package cn.solarmoon.idyllic_food_diary.registry.common;


import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.bathrobe.BathrobeItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cleaver.ChineseCleaverItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot.CookingPotItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.CeladonCupItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.JadeChinaCupItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board.CuttingBoardItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.frying_pan.FryingPanItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill.GrillItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.KettleItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.service_plate.ServicePlateItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.spice_jar.SpiceJarItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerLidItem;
import cn.solarmoon.idyllic_food_diary.element.matter.durian.DurianItem;
import cn.solarmoon.idyllic_food_diary.element.matter.food.SimpleContainableFoodBlockItem;
import cn.solarmoon.idyllic_food_diary.element.matter.food.SimpleContainableFoodItem;
import cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin.RollingPinItem;
import cn.solarmoon.idyllic_food_diary.element.matter.simple_item.GarlicItem;
import cn.solarmoon.idyllic_food_diary.util.useful_data.FoodProperty;
import cn.solarmoon.solarmoon_core.api.entry.common.ItemEntry;
import cn.solarmoon.solarmoon_core.api.item_base.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class IMItems {
    public static void register() {}

    // 炒锅
    public static final ItemEntry<FryingPanItem> FRYING_PAN = IdyllicFoodDiary.REGISTRY.item()
            .id("frying_pan")
            .bound(FryingPanItem::new)
            .build();

    // 调料类
    // 盐
    public static final ItemEntry<SimpleItem> SALT = IdyllicFoodDiary.REGISTRY.item()
            .id("salt")
            .bound(SimpleItem::new)
            .build();

    public static final ItemEntry<SpiceJarItem> SPICE_JAR = IdyllicFoodDiary.REGISTRY.item()
            .id("spice_jar")
            .bound(SpiceJarItem::new)
            .build();

    //茶具
    //青瓷杯
    public static final ItemEntry<CeladonCupItem> CELADON_CUP = IdyllicFoodDiary.REGISTRY.item()
            .id("celadon_cup")
            .bound(CeladonCupItem::new)
            .build();

    //玉瓷杯
    public static final ItemEntry<JadeChinaCupItem> JADE_CHINA_CUP = IdyllicFoodDiary.REGISTRY.item()
            .id("jade_china_cup")
            .bound(JadeChinaCupItem::new)
            .build();

    //水壶
    public static final ItemEntry<KettleItem> KETTLE = IdyllicFoodDiary.REGISTRY.item()
            .id("kettle")
            .bound(KettleItem::new)
            .build();

    //炊具
    //擀面杖
    public static final ItemEntry<RollingPinItem> ROLLING_PIN = IdyllicFoodDiary.REGISTRY.item()
            .id("rolling_pin")
            .bound(RollingPinItem::new)
            .build();

    //中式菜刀
    public static final ItemEntry<ChineseCleaverItem> CHINESE_CLEAVER = IdyllicFoodDiary.REGISTRY.item()
            .id("chinese_cleaver")
            .bound(ChineseCleaverItem::new)
            .build();

    //砧板
    public static final ItemEntry<CuttingBoardItem> CUTTING_BOARD = IdyllicFoodDiary.REGISTRY.item()
            .id("cutting_board")
            .bound(CuttingBoardItem::new)
            .build();

    //煮锅
    public static final ItemEntry<CookingPotItem> COOKING_POT = IdyllicFoodDiary.REGISTRY.item()
            .id("cooking_pot")
            .bound(CookingPotItem::new)
            .build();

    //烧烤架
    public static final ItemEntry<GrillItem> GRILL = IdyllicFoodDiary.REGISTRY.item()
            .id("grill")
            .bound(GrillItem::new)
            .build();

    //蒸笼
    public static final ItemEntry<SteamerItem> STEAMER = IdyllicFoodDiary.REGISTRY.item()
            .id("steamer")
            .bound(SteamerItem::new)
            .build();

    //蒸笼盖
    public static final ItemEntry<SteamerLidItem> STEAMER_LID = IdyllicFoodDiary.REGISTRY.item()
            .id("steamer_lid")
            .bound(SteamerLidItem::new)
            .build();

    //餐具
    //餐盘
    public static final ItemEntry<ServicePlateItem> SERVICE_PLATE = IdyllicFoodDiary.REGISTRY.item()
            .id("service_plate")
            .bound(ServicePlateItem::new)
            .build();

    //普通餐具
    //木盘
    public static final ItemEntry<SimpleBlockItem> WOODEN_PLATE = IdyllicFoodDiary.REGISTRY.item()
            .id("wooden_plate")
            .bound(() -> new BlockItem(IMBlocks.WOODEN_PLATE.get(), new Item.Properties().stacksTo(16)))
            .build();

    //长木盘
    public static final ItemEntry<SimpleBlockItem> LONG_WOODEN_PLATE = IdyllicFoodDiary.REGISTRY.item()
            .id("long_wooden_plate")
            .bound(() -> new BlockItem(IMBlocks.LONG_WOODEN_PLATE.get(), new Item.Properties().stacksTo(16)))
            .build();

    //瓷盘
    public static final ItemEntry<SimpleBlockItem> PORCELAIN_PLATE = IdyllicFoodDiary.REGISTRY.item()
            .id("porcelain_plate")
            .bound(() -> new BlockItem(IMBlocks.PORCELAIN_PLATE.get(), new Item.Properties().stacksTo(16)))
            .build();

    //长瓷盘
    public static final ItemEntry<SimpleBlockItem> LONG_PORCELAIN_PLATE = IdyllicFoodDiary.REGISTRY.item()
            .id("long_porcelain_plate")
            .bound(() -> new BlockItem(IMBlocks.LONG_PORCELAIN_PLATE.get(), new Item.Properties().stacksTo(16)))
            .build();

    //一般作物及其衍生
    //葱
    public static final ItemEntry<SimpleSeedFoodBlockItem> SPRING_ONION = IdyllicFoodDiary.REGISTRY.item()
            .id("spring_onion")
            .bound(() -> new SimpleSeedFoodBlockItem(IMBlocks.SPRING_ONION_CROP.get(), FoodProperty.PRIMARY_PRODUCT))
            .build();

    //葱花
    public static final ItemEntry<SimpleFoodItem> CHOPPED_SPRING_ONION = IdyllicFoodDiary.REGISTRY.item()
            .id("chopped_spring_onion")
            .bound(() -> new SimpleFoodItem(FoodProperty.PRIMARY_PRODUCT))
            .build();

    //姜
    public static final ItemEntry<SimpleSeedFoodBlockItem> GINGER = IdyllicFoodDiary.REGISTRY.item()
            .id("ginger")
            .bound(() -> new SimpleSeedFoodBlockItem(IMBlocks.GINGER_CROP.get(), FoodProperty.PRIMARY_PRODUCT))
            .build();

    //姜片
    public static final ItemEntry<SimpleFoodItem> GINGER_SLICE = IdyllicFoodDiary.REGISTRY.item()
            .id("ginger_slice")
            .bound(() -> new SimpleFoodItem(FoodProperty.PRIMARY_PRODUCT))
            .build();

    //姜丝
    public static final ItemEntry<SimpleFoodItem> SHREDDED_GINGER = IdyllicFoodDiary.REGISTRY.item()
            .id("shredded_ginger")
            .bound(() -> new SimpleFoodItem(FoodProperty.PRIMARY_PRODUCT))
            .build();

    //姜末
    public static final ItemEntry<SimpleFoodItem> CHOPPED_GINGER = IdyllicFoodDiary.REGISTRY.item()
            .id("chopped_ginger")
            .bound(() -> new SimpleFoodItem(FoodProperty.PRIMARY_PRODUCT))
            .build();

    //大蒜
    public static final ItemEntry<GarlicItem> GARLIC = IdyllicFoodDiary.REGISTRY.item()
            .id("garlic")
            .bound(GarlicItem::new)
            .build();

    //蒜苗
    public static final ItemEntry<SimpleFoodItem> GARLIC_SPROUTS = IdyllicFoodDiary.REGISTRY.item()
            .id("garlic_sprouts")
            .bound(() -> new SimpleFoodItem(FoodProperty.PRIMARY_PRODUCT))
            .build();

    //蒜瓣
    public static final ItemEntry<SimpleSeedFoodBlockItem> GARLIC_CLOVE = IdyllicFoodDiary.REGISTRY.item()
            .id("garlic_clove")
            .bound(() -> new SimpleSeedFoodBlockItem(IMBlocks.GARLIC_CROP.get(), FoodProperty.PRIMARY_PRODUCT))
            .build();

    //蒜蓉
    public static final ItemEntry<SimpleFoodItem> CHOPPED_GARLIC = IdyllicFoodDiary.REGISTRY.item()
            .id("chopped_garlic")
            .bound(() -> new SimpleFoodItem(FoodProperty.PRIMARY_PRODUCT))
            .build();

    //南瓜片
    public static final ItemEntry<SimpleFoodItem> PUMPKIN_SLICE = IdyllicFoodDiary.REGISTRY.item()
            .id("pumpkin_slice")
            .bound(() -> new SimpleFoodItem(FoodProperty.PRIMARY_PRODUCT_2))
            .build();

    //水果及其衍生
    //苹果核
    public static final ItemEntry<SimpleSeedItem> APPLE_CORE = IdyllicFoodDiary.REGISTRY.item()
            .id("apple_core")
            .bound(() -> new SimpleSeedItem(IMBlocks.APPLE_CROP.get()))
            .build();

    //苹果树苗
    public static final ItemEntry<SimpleBlockItem> APPLE_SAPLING = IdyllicFoodDiary.REGISTRY.item()
            .id("apple_sapling")
            .bound(() -> new SimpleBlockItem(IMBlocks.APPLE_SAPLING.get()))
            .build();

    //榴莲块
    public static final ItemEntry<DurianItem> DURIAN = IdyllicFoodDiary.REGISTRY.item()
            .id("durian")
            .bound(DurianItem::new)
            .build();

    //榴莲肉
    public static final ItemEntry<SimpleFoodItem> DURIAN_FLESH = IdyllicFoodDiary.REGISTRY.item()
            .id("durian_flesh")
            .bound(() -> new SimpleFoodItem(4, 0.5f))
            .build();

    //榴莲壳
    public static final ItemEntry<SimpleItem> DURIAN_SHELL = IdyllicFoodDiary.REGISTRY.item()
            .id("durian_shell")
            .bound(SimpleItem::new)
            .build();

    //榴莲核
    public static final ItemEntry<SimpleSeedItem> DURIAN_CORE = IdyllicFoodDiary.REGISTRY.item()
            .id("durian_core")
            .bound(() -> new SimpleSeedItem(IMBlocks.DURIAN_CROP.get()))
            .build();

    //榴莲树苗
    public static final ItemEntry<SimpleBlockItem> DURIAN_SAPLING = IdyllicFoodDiary.REGISTRY.item()
            .id("durian_sapling")
            .bound(() -> new SimpleBlockItem(IMBlocks.DURIAN_SAPLING.get()))
            .build();

    //茶及其衍生
    //红茶叶
    public static final ItemEntry<SimpleItem> BLACK_TEA_LEAF = IdyllicFoodDiary.REGISTRY.item()
            .id("black_tea_leaf")
            .bound(SimpleItem::new)
            .build();

    //绿茶叶
    public static final ItemEntry<SimpleItem> GREEN_TEA_LEAF = IdyllicFoodDiary.REGISTRY.item()
            .id("green_tea_leaf")
            .bound(SimpleItem::new)
            .build();

    //绿茶种子
    public static final ItemEntry<SimpleSeedItem> GREEN_TEA_SEEDS = IdyllicFoodDiary.REGISTRY.item()
            .id("green_tea_seeds")
            .bound(() -> new SimpleSeedItem(IMBlocks.GREEN_TEA_PLANT.get()))
            .build();

    //野生作物
    //野生葱
    public static final ItemEntry<SimpleBlockItem> WILD_SPRING_ONION = IdyllicFoodDiary.REGISTRY.item()
            .id("wild_spring_onion")
            .bound(() -> new SimpleBlockItem(IMBlocks.WILD_SPRING_ONION.get()))
            .build();

    //野生姜
    public static final ItemEntry<SimpleBlockItem> WILD_GINGER = IdyllicFoodDiary.REGISTRY.item()
            .id("wild_ginger")
            .bound(() -> new SimpleBlockItem(IMBlocks.WILD_GINGER.get()))
            .build();

    //野生大蒜
    public static final ItemEntry<SimpleBlockItem> WILD_GARLIC = IdyllicFoodDiary.REGISTRY.item()
            .id("wild_garlic")
            .bound(() -> new SimpleBlockItem(IMBlocks.WILD_GARLIC.get()))
            .build();

    //预制食物-生食
    //生鸡肉丁
    public static final ItemEntry<SimpleFoodItem> CHICKEN_CUTS = IdyllicFoodDiary.REGISTRY.item()
            .id("chicken_cuts")
            .bound(() -> new SimpleFoodItem(FoodProperty.PRIMARY_HUNGER_PRODUCT))
            .build();

    //生猪肉末
    public static final ItemEntry<SimpleFoodItem> MINCED_PORK = IdyllicFoodDiary.REGISTRY.item()
            .id("minced_pork")
            .bound(() -> new SimpleFoodItem(FoodProperty.PRIMARY_HUNGER_PRODUCT))
            .build();

    //碗装蛋液
    public static final ItemEntry<SimpleContainableFoodBlockItem> EGG_LIQUID_BOWL = IdyllicFoodDiary.REGISTRY.item()
            .id("egg_liquid_bowl")
            .bound(() -> new SimpleContainableFoodBlockItem(
                    IMBlocks.EGG_LIQUID_BOWL.get(),
                    FoodProperty.PRIMARY_HUNGER_PRODUCT))
            .build();

    //预制食物-面食
    //面团
    public static final ItemEntry<SimpleFoodBlockItem> WHEAT_DOUGH = IdyllicFoodDiary.REGISTRY.item()
            .id("wheat_dough")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.WHEAT_DOUGH.get(), FoodProperty.PRIMARY_HUNGER_PRODUCT))
            .build();

    //面饼
    public static final ItemEntry<SimpleFoodBlockItem> FLATBREAD_DOUGH = IdyllicFoodDiary.REGISTRY.item()
            .id("flatbread_dough")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.FLATBREAD_DOUGH.get(), FoodProperty.PRIMARY_HUNGER_PRODUCT))
            .build();

    //开酥面团
    public static final ItemEntry<SimpleFoodBlockItem> SHORTENING_DOUGH = IdyllicFoodDiary.REGISTRY.item()
            .id("shortening_dough")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.SHORTENING_DOUGH.get(), FoodProperty.PRIMARY_HUNGER_PRODUCT_1))
            .build();

    //馅饼皮
    public static final ItemEntry<SimpleFoodBlockItem> PIE_CRUST = IdyllicFoodDiary.REGISTRY.item()
            .id("pie_crust")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.PIE_CRUST.get(), FoodProperty.PRIMARY_HUNGER_PRODUCT_1))
            .build();

    //白吉馍
    public static final ItemEntry<SimpleFoodBlockItem> BAI_JI_BUN = IdyllicFoodDiary.REGISTRY.item()
            .id("bai_ji_bun")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.BAI_JI_BUN.get(), FoodProperty.PRIMARY_HUNGER_PRODUCT_1))
            .build();

    //未烹饪的食物
    //未烹饪的肉臊子
    public static final ItemEntry<SimpleFoodItem> UNCOOKED_SAO_ZI = IdyllicFoodDiary.REGISTRY.item()
            .id("uncooked_sao_zi")
            .bound(() -> new SimpleFoodItem(FoodProperty.PRIMARY_HUNGER_PRODUCT_1))
            .build();

    //未烹饪的蛋挞
    public static final ItemEntry<SimpleFoodBlockItem> UNCOOKED_CUSTARD_TART = IdyllicFoodDiary.REGISTRY.item()
            .id("uncooked_custard_tart")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.UNCOOKED_CUSTARD_TART.get(), FoodProperty.PRIMARY_HUNGER_PRODUCT_1))
            .build();

    //未烹饪的叫花鸡
    public static final ItemEntry<SimpleContainableFoodBlockItem> UNCOOKED_BEGGARS_CHICKEN = IdyllicFoodDiary.REGISTRY.item()
            .id("uncooked_beggars_chicken")
            .bound(() -> new SimpleContainableFoodBlockItem(
                    IMBlocks.UNCOOKED_BEGGARS_CHICKEN.get(),
                    FoodProperty.PRIMARY_HUNGER_PRODUCT_1)
            )
            .build();

    //未烹饪的香菇蒸鸡
    public static final ItemEntry<SimpleContainableFoodBlockItem> UNCOOKED_STEAMED_CHICKEN_WITH_MUSHROOM = IdyllicFoodDiary.REGISTRY.item()
            .id("uncooked_steamed_chicken_with_mushroom")
            .bound(() -> new SimpleContainableFoodBlockItem(
                    IMBlocks.UNCOOKED_STEAMED_CHICKEN_WITH_MUSHROOM.get(),
                    FoodProperty.PRIMARY_HUNGER_PRODUCT_1)
            )
            .build();

    //未烹饪的蒜酥蒸南瓜
    public static final ItemEntry<SimpleContainableFoodBlockItem> UNCOOKED_STEAMED_PUMPKIN_WITH_CHOPPED_GARLIC = IdyllicFoodDiary.REGISTRY.item()
            .id("uncooked_steamed_pumpkin_with_chopped_garlic")
            .bound(() -> new SimpleContainableFoodBlockItem(
                    IMBlocks.UNCOOKED_STEAMED_PUMPKIN_WITH_CHOPPED_GARLIC.get(),
                    FoodProperty.PRIMARY_HUNGER_PRODUCT_1)
            )
            .build();

    //未烹饪的清蒸鲑鱼
    public static final ItemEntry<SimpleContainableFoodItem> UNCOOKED_STEAMED_SALMON = IdyllicFoodDiary.REGISTRY.item()
            .id("uncooked_steamed_salmon")
            .bound(() -> new SimpleContainableFoodItem(
                    LONG_PORCELAIN_PLATE.get(),
                    FoodProperty.PRIMARY_HUNGER_PRODUCT_1)
            )
            .build();

    //未烹饪的烤乳猪
    public static final ItemEntry<SimpleContainableFoodBlockItem> UNCOOKED_ROASTED_SUCKLING_PIG = IdyllicFoodDiary.REGISTRY.item()
            .id("uncooked_roasted_suckling_pig")
            .bound(() -> new SimpleContainableFoodBlockItem(
                    IMBlocks.UNCOOKED_ROASTED_SUCKLING_PIG.get(),
                    FoodProperty.PRIMARY_HUNGER_PRODUCT_1)
            )
            .build();

    //烹饪食物-无方块
    //熟鸡肉丁
    public static final ItemEntry<SimpleFoodItem> COOKED_CHICKEN_CUTS = IdyllicFoodDiary.REGISTRY.item()
            .id("cooked_chicken_cuts")
            .bound(() -> new SimpleFoodItem(3, 0.5f))
            .build();

    //熟猪肉末
    public static final ItemEntry<SimpleFoodItem> COOKED_PORK_MINCE = IdyllicFoodDiary.REGISTRY.item()
            .id("cooked_pork_mince")
            .bound(() -> new SimpleFoodItem(3, 0.5f))
            .build();

    //烤榴莲
    public static final ItemEntry<SimpleFoodItem> ROASTED_DURIAN = IdyllicFoodDiary.REGISTRY.item()
            .id("roasted_durian")
            .bound(() -> new SimpleFoodItem(6, 0.5f))
            .build();

    //肉臊子
    public static final ItemEntry<SimpleFoodItem> SAO_ZI = IdyllicFoodDiary.REGISTRY.item()
            .id("sao_zi")
            .bound(() -> new SimpleFoodItem(6, 0.5f))
            .build();

    //肉夹馍
    public static final ItemEntry<SimpleFoodItem> ROUGAMO = IdyllicFoodDiary.REGISTRY.item()
            .id("rougamo")
            .bound(() -> new SimpleFoodItem(12, 0.8f))
            .build();

    //烹饪食物-面点类
    //馒头
    public static final ItemEntry<SimpleFoodBlockItem> STEAMED_BUN = IdyllicFoodDiary.REGISTRY.item()
            .id("steamed_bun")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.STEAMED_BUN.get(), 4, 0.5f))
            .build();

    //烤馕
    public static final ItemEntry<SimpleFoodItem> ROASTED_NAAN = IdyllicFoodDiary.REGISTRY.item()
            .id("roasted_naan")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.ROASTED_NAAN.get(), 6, 0.5f))
            .build();

    //烤白吉馍
    public static final ItemEntry<SimpleFoodBlockItem> ROASTED_BAI_JI_BUN = IdyllicFoodDiary.REGISTRY.item()
            .id("roasted_bai_ji_bun")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.ROASTED_BAI_JI_BUN.get(), 6, 0.5f))
            .build();

    //蛋挞
    public static final ItemEntry<SimpleFoodBlockItem> CUSTARD_TART = IdyllicFoodDiary.REGISTRY.item()
            .id("custard_tart")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.CUSTARD_TART.get(), 8, 0.8f,
                    () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0), 1))
            .build();

    //烹饪食物-简单烹饪类
    //鸡蛋羹
    public static final ItemEntry<SimpleContainableFoodBlockItem> STEAMED_EGG_CUSTARD = IdyllicFoodDiary.REGISTRY.item()
            .id("steamed_egg_custard")
            .bound(() -> new SimpleContainableFoodBlockItem(
                    IMBlocks.STEAMED_EGG_CUSTARD.get(),
                    5, 0.75f,
                    () -> new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 1))
            .build();

    //烹饪食物-复杂烹饪类产物
    //叫花鸡肉
    public static final ItemEntry<SimpleFoodItem> BEGGARS_CHICKEN_MEAT = IdyllicFoodDiary.REGISTRY.item()
            .id("beggars_chicken_meat")
            .bound(() -> new SimpleFoodItem(14, 0.8f,
                    () -> new MobEffectInstance(MobEffects.HEAL, 1, 0, false, false, false), 1F))
            .build();

    //碗装香菇蒸鸡
    public static final ItemEntry<SimpleContainableFoodBlockItem> STEAMED_CHICKEN_WITH_MUSHROOM_BOWL = IdyllicFoodDiary.REGISTRY.item()
            .id("steamed_chicken_with_mushroom_bowl")
            .bound(() -> new SimpleContainableFoodBlockItem(
                    IMBlocks.STEAMED_CHICKEN_WITH_MUSHROOM_BOWL.get(), 14, 0.8f,
                    () -> new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 1F))
            .build();

    //蒜酥蒸南瓜片
    public static final ItemEntry<SimpleFoodItem> STEAMED_PUMPKIN_SLICE_WITH_CHOPPED_GARLIC = IdyllicFoodDiary.REGISTRY.item()
            .id("steamed_pumpkin_slice_with_chopped_garlic")
            .bound(() -> new SimpleFoodItem(6, 0.6f,
                    () -> new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 0), 1F))
            .build();

    //烤乳猪肉
    public static final ItemEntry<SimpleFoodItem> ROASTED_SUCKLING_PORK = IdyllicFoodDiary.REGISTRY.item()
            .id("roasted_suckling_pork")
            .bound(() -> new SimpleFoodItem(12, 0.8f,
                    () -> new MobEffectInstance(MobEffects.LUCK, 600, 1), 1))
            .build();

    //烤乳猪头
    public static final ItemEntry<SimpleFoodBlockItem> ROASTED_SUCKLING_PIG_HEAD = IdyllicFoodDiary.REGISTRY.item()
            .id("roasted_suckling_pig_head")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.ROASTED_SUCKLING_PIG_HEAD.get(),
                    14, 0.8f,
                    () -> new MobEffectInstance(MobEffects.LUCK, 600, 1), 1))
            .condition(false)
            .build();

    //烹饪食物-复杂烹饪类
    //叫花鸡
    public static final ItemEntry<SimpleBlockItem> BEGGARS_CHICKEN = IdyllicFoodDiary.REGISTRY.item()
            .id("beggars_chicken")
            .bound(() -> new SimpleBlockItem(IMBlocks.BEGGARS_CHICKEN.get(), 16))
            .build();

    //香菇蒸鸡
    public static final ItemEntry<SimpleBlockItem> STEAMED_CHICKEN_WITH_MUSHROOM = IdyllicFoodDiary.REGISTRY.item()
            .id("steamed_chicken_with_mushroom")
            .bound(() -> new SimpleBlockItem(IMBlocks.STEAMED_CHICKEN_WITH_MUSHROOM.get(), 16))
            .build();

    //蒜酥蒸南瓜
    public static final ItemEntry<SimpleBlockItem> STEAMED_PUMPKIN_WITH_CHOPPED_GARLIC = IdyllicFoodDiary.REGISTRY.item()
            .id("steamed_pumpkin_with_chopped_garlic")
            .bound(() -> new SimpleBlockItem(IMBlocks.STEAMED_PUMPKIN_WITH_CHOPPED_GARLIC.get(), 16))
            .build();

    //清蒸鲑鱼
    public static final ItemEntry<SimpleBlockItem> STEAMED_SALMON = IdyllicFoodDiary.REGISTRY.item()
            .id("steamed_salmon")
            .bound(() -> new SimpleBlockItem(IMBlocks.STEAMED_SALMON.get(), 16))
            .build();

    //烤乳猪
    public static final ItemEntry<SimpleBlockItem> ROASTED_SUCKLING_PIG = IdyllicFoodDiary.REGISTRY.item()
            .id("roasted_suckling_pig")
            .bound(() -> new SimpleBlockItem(IMBlocks.ROASTED_SUCKLING_PIG.get(), 16))
            .build();

    //装备
    //浴衣
    public static final ItemEntry<BathrobeItem> BATHROBE = IdyllicFoodDiary.REGISTRY.item()
            .id("bathrobe")
            .bound(BathrobeItem::new)
            .build();

}
