package cn.solarmoon.idyllic_food_diary.registry.common;


import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.level.AppleTreeFeature;
import cn.solarmoon.idyllic_food_diary.element.level.DurianTreeFeature;
import cn.solarmoon.idyllic_food_diary.element.matter.container.*;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.long_plate.LongPlateBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.plate.PlateBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot.CookingPotBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup.LittleCupBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board.CuttingBoardBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill.GrillBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.KettleBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.oven.OvenBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.spice_jar.SpiceJarBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerLidBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.winnowing_basket.WinnowingBasketBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.crop.*;
import cn.solarmoon.idyllic_food_diary.element.matter.durian.DurianBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.*;
import cn.solarmoon.idyllic_food_diary.element.matter.food.long_press_eat_block.*;
import cn.solarmoon.idyllic_food_diary.element.matter.stove.StoveBlock;
import cn.solarmoon.idyllic_food_diary.util.useful_data.BlockProperty;
import cn.solarmoon.solarmoon_core.api.entry.common.BlockEntry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;

public class IMBlocks {
    public static void register() {}

    // 烤箱
    public static final BlockEntry<OvenBlock> OVEN = IdyllicFoodDiary.REGISTRY.block()
            .id("oven")
            .bound(OvenBlock::new)
            .build();

    // 灶台
    public static final BlockEntry<StoveBlock> STOVE = IdyllicFoodDiary.REGISTRY.block()
            .id("stove")
            .bound(StoveBlock::new)
            .build();

    //簸箕
    public static final BlockEntry<WinnowingBasketBlock> WINNOWING_BASKET = IdyllicFoodDiary.REGISTRY.block()
            .id("winnowing_basket")
            .bound(WinnowingBasketBlock::new)
            .build();

    //炒锅
    public static final BlockEntry<WokBlock> WOK = IdyllicFoodDiary.REGISTRY.block()
            .id("wok")
            .bound(WokBlock::new)
            .build();

    //调料罐
    public static final BlockEntry<SpiceJarBlock> SPICE_JAR = IdyllicFoodDiary.REGISTRY.block()
            .id("spice_jar")
            .bound(SpiceJarBlock::new)
            .build();

    //面团
    public static final BlockEntry<WheatDoughBlock> WHEAT_DOUGH = IdyllicFoodDiary.REGISTRY.block()
            .id("wheat_dough")
            .bound(WheatDoughBlock::new)
            .build();

    //开酥面团
    public static final BlockEntry<ShorteningDoughBlock> SHORTENING_DOUGH = IdyllicFoodDiary.REGISTRY.block()
            .id("shortening_dough")
            .bound(ShorteningDoughBlock::new)
            .build();

    //馅饼皮
    public static final BlockEntry<PieCrustBlock> PIE_CRUST = IdyllicFoodDiary.REGISTRY.block()
            .id("pie_crust")
            .bound(PieCrustBlock::new)
            .build();

    //面饼
    public static final BlockEntry<FlatbreadDoughBlock> FLATBREAD_DOUGH = IdyllicFoodDiary.REGISTRY.block()
            .id("flatbread_dough")
            .bound(FlatbreadDoughBlock::new)
            .build();

    //碗装蛋液
    public static final BlockEntry<BowlFoodShapedBlock> EGG_LIQUID_BOWL = IdyllicFoodDiary.REGISTRY.block()
            .id("egg_liquid_bowl")
            .bound(BowlFoodShapedBlock::new)
            .build();

    //鸡蛋羹
    public static final BlockEntry<BowlFoodShapedBlock> STEAMED_EGG_CUSTARD = IdyllicFoodDiary.REGISTRY.block()
            .id("steamed_egg_custard")
            .bound(BowlFoodShapedBlock::new)
            .build();

    //馒头
    public static final BlockEntry<SteamedBunBlock> STEAMED_BUN = IdyllicFoodDiary.REGISTRY.block()
            .id("steamed_bun")
            .bound(SteamedBunBlock::new)
            .build();

    //烤馕
    public static final BlockEntry<RoastedNaanBlock> ROASTED_NAAN = IdyllicFoodDiary.REGISTRY.block()
            .id("roasted_naan")
            .bound(RoastedNaanBlock::new)
            .build();

    //白吉馍
    public static final BlockEntry<BaiJiBunBlock> BAI_JI_BUN = IdyllicFoodDiary.REGISTRY.block()
            .id("bai_ji_bun")
            .bound(BaiJiBunBlock::new)
            .build();

    //烤白吉馍
    public static final BlockEntry<RoastedBaiJiBunBlock> ROASTED_BAI_JI_BUN = IdyllicFoodDiary.REGISTRY.block()
            .id("roasted_bai_ji_bun")
            .bound(RoastedBaiJiBunBlock::new)
            .build();

    //蛋挞
    public static final BlockEntry<CustardTartBlock> CUSTARD_TART = IdyllicFoodDiary.REGISTRY.block()
            .id("custard_tart")
            .bound(CustardTartBlock::new)
            .build();

    //------------------------------------------------------------------------------------------//

    //苹果树苗
    public static final BlockEntry<SaplingBlock> APPLE_SAPLING = IdyllicFoodDiary.REGISTRY.block()
            .id("apple_sapling")
            .bound(() -> new SaplingBlock(new AppleTreeFeature.AppleTreeGrower(), Block.Properties.copy(Blocks.OAK_SAPLING)))
            .build();

    //苹果作物
    public static final BlockEntry<AppleCropBlock> APPLE_CROP = IdyllicFoodDiary.REGISTRY.block()
            .id("apple_crop")
            .bound(AppleCropBlock::new)
            .build();

    //绿茶
    public static final BlockEntry<GreenTeaPlantCropBlock> GREEN_TEA_PLANT = IdyllicFoodDiary.REGISTRY.block()
            .id("green_tea_plant")
            .bound(GreenTeaPlantCropBlock::new)
            .build();

    //榴莲作物
    public static final BlockEntry<DurianCropBlock> DURIAN_CROP = IdyllicFoodDiary.REGISTRY.block()
            .id("durian_crop")
            .bound(DurianCropBlock::new)
            .build();

    //榴莲方块
    public static final BlockEntry<DurianBlock> DURIAN = IdyllicFoodDiary.REGISTRY.block()
            .id("durian")
            .bound(DurianBlock::new)
            .build();

    //榴莲树苗
    public static final BlockEntry<SaplingBlock> DURIAN_SAPLING = IdyllicFoodDiary.REGISTRY.block()
            .id("durian_sapling")
            .bound(() -> new SaplingBlock(new DurianTreeFeature.DurianTreeGrower() , Block.Properties.copy(Blocks.JUNGLE_SAPLING)))
            .build();

    //葱作物
    public static final BlockEntry<SpringOnionCropBlock> SPRING_ONION_CROP = IdyllicFoodDiary.REGISTRY.block()
            .id("spring_onion_crop")
            .bound(SpringOnionCropBlock::new)
            .build();

    //大蒜作物
    public static final BlockEntry<GarlicCropBlock> GARLIC_CROP = IdyllicFoodDiary.REGISTRY.block()
            .id("garlic_crop")
            .bound(GarlicCropBlock::new)
            .build();

    //姜作物
    public static final BlockEntry<GingerCropBlock> GINGER_CROP = IdyllicFoodDiary.REGISTRY.block()
            .id("ginger_crop")
            .bound(GingerCropBlock::new)
            .build();

    //————————————————————————————————————————————————————————————————————————————————//

    //野生葱
    public static final BlockEntry<WildCropBlock> WILD_SPRING_ONION = IdyllicFoodDiary.REGISTRY.block()
            .id("wild_spring_onion")
            .bound(() -> new WildCropBlock(MobEffects.BLINDNESS, 8))
            .build();

    //野生姜
    public static final BlockEntry<WildCropBlock> WILD_GINGER = IdyllicFoodDiary.REGISTRY.block()
            .id("wild_ginger")
            .bound(() -> new WildCropBlock(MobEffects.DIG_SPEED, 8))
            .build();

    //野生大蒜
    public static final BlockEntry<WildCropBlock> WILD_GARLIC = IdyllicFoodDiary.REGISTRY.block()
            .id("wild_garlic")
            .bound(() -> new WildCropBlock(MobEffects.UNLUCK, 8))
            .build();

    //————————————————————————————————————————————————————————————————————————————————//

    //青瓷杯
    public static final BlockEntry<LittleCupBlock> CELADON_CUP = IdyllicFoodDiary.REGISTRY.block()
            .id("celadon_cup")
            .bound(LittleCupBlock::new)
            .build();

    //玉瓷杯
    public static final BlockEntry<LittleCupBlock> JADE_CHINA_CUP = IdyllicFoodDiary.REGISTRY.block()
            .id("jade_china_cup")
            .bound(LittleCupBlock::new)
            .build();

    //水壶
    public static final BlockEntry<KettleBlock> KETTLE = IdyllicFoodDiary.REGISTRY.block()
            .id("kettle")
            .bound(KettleBlock::new)
            .build();

    //汤锅
    public static final BlockEntry<CookingPotBlock> COOKING_POT = IdyllicFoodDiary.REGISTRY.block()
            .id("cooking_pot")
            .bound(CookingPotBlock::new)
            .build();

    //砧板
    public static final BlockEntry<CuttingBoardBlock> CUTTING_BOARD = IdyllicFoodDiary.REGISTRY.block()
            .id("cutting_board")
            .bound(CuttingBoardBlock::new)
            .build();

    //烧烤架
    public static final BlockEntry<CuttingBoardBlock> GRILL = IdyllicFoodDiary.REGISTRY.block()
            .id("grill")
            .bound(GrillBlock::new)
            .build();

    //蒸笼
    public static final BlockEntry<SteamerBlock> STEAMER = IdyllicFoodDiary.REGISTRY.block()
            .id("steamer")
            .bound(SteamerBlock::new)
            .build();

    //蒸笼盖
    public static final BlockEntry<SteamerLidBlock> STEAMER_LID = IdyllicFoodDiary.REGISTRY.block()
            .id("steamer_lid")
            .bound(SteamerLidBlock::new)
            .build();

    //-----------------------------------------------------------------------

    //碗
    public static final BlockEntry<BowlBlock> BOWL = IdyllicFoodDiary.REGISTRY.block()
            .id("bowl")
            .bound(BowlBlock::new)
            .build();

    //木盘
    public static final BlockEntry<PlateBlock> WOODEN_PLATE = IdyllicFoodDiary.REGISTRY.block()
            .id("wooden_plate")
            .bound(() -> new PlateBlock(SoundType.WOOD))
            .build();

    //长木盘
    public static final BlockEntry<LongPlateBlock> LONG_WOODEN_PLATE = IdyllicFoodDiary.REGISTRY.block()
            .id("long_wooden_plate")
            .bound(() -> new LongPlateBlock(SoundType.WOOD))
            .build();

    //瓷盘
    public static final BlockEntry<PlateBlock> PORCELAIN_PLATE = IdyllicFoodDiary.REGISTRY.block()
            .id("porcelain_plate")
            .bound(() -> new PlateBlock(SoundType.GLASS))
            .build();

    //长瓷盘
    public static final BlockEntry<LongPlateBlock> LONG_PORCELAIN_PLATE = IdyllicFoodDiary.REGISTRY.block()
            .id("long_porcelain_plate")
            .bound(() -> new LongPlateBlock(SoundType.GLASS))
            .build();

    // 未烹饪的
    // 未烹饪的蛋挞
    public static final BlockEntry<UncookedFoodBlock> UNCOOKED_CUSTARD_TART = IdyllicFoodDiary.REGISTRY.block()
            .id("uncooked_custard_tart")
            .bound(() -> new UncookedFoodBlock(
                    CustardTartBlock.SHAPE,
                    BlockProperty.DOUGH))
            .build();

    // 未烹饪的蒜酥蒸南瓜
    public static final BlockEntry<UncookedFoodBlock> UNCOOKED_STEAMED_PUMPKIN_WITH_CHOPPED_GARLIC = IdyllicFoodDiary.REGISTRY.block()
            .id("uncooked_steamed_pumpkin_with_chopped_garlic")
            .bound(() -> new UncookedFoodBlock(
                    SteamedPumpkinWithChoppedGarlicBlock.SHAPE,
                    BlockProperty.FOOD_ON_MEDIUM_CONTAINER))
            .build();

    // 未烹饪的香菇蒸鸡
    public static final BlockEntry<UncookedFoodBlock> UNCOOKED_STEAMED_CHICKEN_WITH_MUSHROOM = IdyllicFoodDiary.REGISTRY.block()
            .id("uncooked_steamed_chicken_with_mushroom")
            .bound(() -> new UncookedFoodBlock(
                    SteamedChickenWithMushroomBlock.SHAPE,
                    BlockProperty.FOOD_ON_MEDIUM_CONTAINER))
            .build();

    // 未烹饪的叫花鸡
    public static final BlockEntry<UncookedFoodBlock> UNCOOKED_BEGGARS_CHICKEN = IdyllicFoodDiary.REGISTRY.block()
            .id("uncooked_beggars_chicken")
            .bound(() -> new UncookedFoodBlock(
                    BeggarsChickenBlock::SHAPE,
                    BlockProperty.FOOD_ON_MEDIUM_CONTAINER))
            .build();

    // 未烹饪的烤乳猪
    public static final BlockEntry<UncookedFoodDoubleBlock> UNCOOKED_ROASTED_SUCKLING_PIG = IdyllicFoodDiary.REGISTRY.block()
            .id("uncooked_roasted_suckling_pig")
            .bound(() -> new UncookedFoodDoubleBlock(
                    RoastedSucklingPigBlock::SHAPE,
                    BlockProperty.FOOD_ON_LARGE_CONTAINER))
            .build();

    //-----------------------------------------------------------------------

    //清蒸鲑鱼
    public static final BlockEntry<SteamedSalmonBlock> STEAMED_SALMON = IdyllicFoodDiary.REGISTRY.block()
            .id("steamed_salmon")
            .bound(SteamedSalmonBlock::new)
            .build();

    //烤乳猪
    public static final BlockEntry<RoastedSucklingPigBlock> ROASTED_SUCKLING_PIG = IdyllicFoodDiary.REGISTRY.block()
            .id("roasted_suckling_pig")
            .bound(RoastedSucklingPigBlock::new)
            .build();

    //烤乳猪头
    public static final BlockEntry<RoastedSucklingPigHeadBlock> ROASTED_SUCKLING_PIG_HEAD = IdyllicFoodDiary.REGISTRY.block()
            .id("roasted_suckling_pig_head")
            .bound(RoastedSucklingPigHeadBlock::new)
            .condition(false)
            .build();

    //蒜酥蒸南瓜
    public static final BlockEntry<SteamedPumpkinWithChoppedGarlicBlock> STEAMED_PUMPKIN_WITH_CHOPPED_GARLIC = IdyllicFoodDiary.REGISTRY.block()
            .id("steamed_pumpkin_with_chopped_garlic")
            .bound(SteamedPumpkinWithChoppedGarlicBlock::new)
            .build();

    //香菇蒸鸡
    public static final BlockEntry<SteamedChickenWithMushroomBlock> STEAMED_CHICKEN_WITH_MUSHROOM = IdyllicFoodDiary.REGISTRY.block()
            .id("steamed_chicken_with_mushroom")
            .bound(SteamedChickenWithMushroomBlock::new)
            .build();

    //碗装香菇蒸鸡
    public static final BlockEntry<BowlFoodShapedBlock> STEAMED_CHICKEN_WITH_MUSHROOM_BOWL = IdyllicFoodDiary.REGISTRY.block()
            .id("steamed_chicken_with_mushroom_bowl")
            .bound(BowlFoodShapedBlock::new)
            .build();

    //叫花鸡
    public static final BlockEntry<BeggarsChickenBlock> BEGGARS_CHICKEN = IdyllicFoodDiary.REGISTRY.block()
            .id("beggars_chicken")
            .bound(BeggarsChickenBlock::new)
            .build();

}
