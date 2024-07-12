package cn.solarmoon.idyllic_food_diary.registry.common;


import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.fluid.BlackTeaFluid;
import cn.solarmoon.idyllic_food_diary.element.matter.fluid.GreenTeaFluid;
import cn.solarmoon.idyllic_food_diary.element.matter.fluid.MilkTeaFluid;
import cn.solarmoon.idyllic_food_diary.element.matter.fluid.SoybeanOilFluid;
import cn.solarmoon.idyllic_food_diary.element.matter.fluid.soup.*;
import cn.solarmoon.solarmoon_core.api.entry.common.FluidEntry;


public class IMFluids {
    public static void register() {}

    public static final FluidEntry SOYBEAN_OIL = IdyllicFoodDiary.REGISTRY.fluid()
            .id("soybean_oil")
            .waterLikeProperties(false)
            .waterLike(true, 0xFFEC8B)
            .simple(SoybeanOilFluid::new, true)
            .build();

    //红茶
    public static final FluidEntry BLACK_TEA = IdyllicFoodDiary.REGISTRY.fluid()
            .id("black_tea")
            .waterLikeProperties(false)
            .waterLike(true, 0xFF8B0000)
            .simple(BlackTeaFluid::new, true)
            .build();

    //绿茶
    public static final FluidEntry GREEN_TEA = IdyllicFoodDiary.REGISTRY.fluid()
            .id("green_tea")
            .waterLikeProperties(false)
            .waterLike(true, 0xFF7CCD7C)
            .simple(GreenTeaFluid::new, true)
            .build();

    //奶茶
    public static final FluidEntry MILK_TEA = IdyllicFoodDiary.REGISTRY.fluid()
            .id("milk_tea")
            .waterLikeProperties(false)
            .waterLike(true)
            .simple(MilkTeaFluid::new, true)
            .build();

    //蘑菇煲
    public static final FluidEntry MUSHROOM_STEW = IdyllicFoodDiary.REGISTRY.fluid()
            .id("mushroom_stew")
            .waterLikeProperties(false)
            .base()
            .simple(MushroomStewFluid::new, false)
            .build();

    //甜菜汤
    public static final FluidEntry BEETROOT_SOUP = IdyllicFoodDiary.REGISTRY.fluid()
            .id("beetroot_soup")
            .waterLikeProperties(false)
            .base()
            .simple(BeetrootSoupFluid::new, false)
            .build();

    //南瓜汤
    public static final FluidEntry PUMPKIN_SOUP = IdyllicFoodDiary.REGISTRY.fluid()
            .id("pumpkin_soup")
            .waterLikeProperties(false)
            .base()
            .simple(PumpkinSoupFluid::new, false)
            .build();

    //藏书羊肉汤
    public static final FluidEntry CANG_SHU_MUTTON_SOUP = IdyllicFoodDiary.REGISTRY.fluid()
            .id("cang_shu_mutton_soup")
            .waterLikeProperties(false)
            .base()
            .simple(CangShuMuttonSoupFluid::new, false)
            .build();

    //紫菜蛋花汤
    public static final FluidEntry SEAWEED_EGG_DROP_SOUP = IdyllicFoodDiary.REGISTRY.fluid()
            .id("seaweed_egg_drop_soup")
            .waterLikeProperties(false)
            .base()
            .simple(SeaweedEggDropSoupFluid::new, false)
            .build();

}
