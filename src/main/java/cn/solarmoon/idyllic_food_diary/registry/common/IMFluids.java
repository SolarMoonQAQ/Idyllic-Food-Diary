package cn.solarmoon.idyllic_food_diary.registry.common;


import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.fluid.BlackTeaFluid;
import cn.solarmoon.idyllic_food_diary.element.matter.fluid.GreenTeaFluid;
import cn.solarmoon.idyllic_food_diary.element.matter.fluid.MilkTeaFluid;
import cn.solarmoon.idyllic_food_diary.element.matter.fluid.SoybeanOilFluid;
import cn.solarmoon.idyllic_food_diary.element.matter.fluid.no_bucket.*;
import cn.solarmoon.solarmoon_core.api.entry.common.FluidEntry;


public class IMFluids {
    public static void register() {}

    public static final FluidEntry SOYBEAN_OIL = IdyllicFoodDiary.REGISTRY.fluid()
            .id("soybean_oil")
            .bound(() -> new SoybeanOilFluid().new FluidBlock())
            .still(() -> new SoybeanOilFluid().new Source())
            .flowing(() -> new SoybeanOilFluid().new Flowing())
            .bucket(() -> new SoybeanOilFluid().new Bucket())
            .waterLike()
            .color(0xFFEC8B)
            .build();

    //红茶
    public static final FluidEntry BLACK_TEA = IdyllicFoodDiary.REGISTRY.fluid()
            .id("black_tea")
            .bound(() -> new BlackTeaFluid().new FluidBlock())
            .still(() -> new BlackTeaFluid().new Source())
            .flowing(() -> new BlackTeaFluid().new Flowing())
            .bucket(() -> new BlackTeaFluid().new Bucket())
            .waterLike()
            .color(0xFF8B0000)
            .build();

    //绿茶
    public static final FluidEntry GREEN_TEA = IdyllicFoodDiary.REGISTRY.fluid()
            .id("green_tea")
            .bound(() -> new GreenTeaFluid().new FluidBlock())
            .still(() -> new GreenTeaFluid().new Source())
            .flowing(() -> new GreenTeaFluid().new Flowing())
            .bucket(() -> new GreenTeaFluid().new Bucket())
            .waterLike()
            .color(0xFF7CCD7C)
            .build();

    //奶茶
    public static final FluidEntry MILK_TEA = IdyllicFoodDiary.REGISTRY.fluid()
            .id("milk_tea")
            .bound(() -> new MilkTeaFluid().new FluidBlock())
            .still(() -> new MilkTeaFluid().new Source())
            .flowing(() -> new MilkTeaFluid().new Flowing())
            .bucket(() -> new MilkTeaFluid().new Bucket())
            .build();

    //蘑菇煲
    public static final FluidEntry MUSHROOM_STEW_FLUID = IdyllicFoodDiary.REGISTRY.fluid()
            .id("mushroom_stew_fluid")
            .bound(() -> new MushroomStewFluid().new FluidBlock())
            .still(() -> new MushroomStewFluid().new Source())
            .flowing(() -> new MushroomStewFluid().new Flowing())
            .build();

    //甜菜汤
    public static final FluidEntry BEETROOT_SOUP_FLUID = IdyllicFoodDiary.REGISTRY.fluid()
            .id("beetroot_soup_fluid")
            .bound(() -> new BeetrootSoupFluid().new FluidBlock())
            .still(() -> new BeetrootSoupFluid().new Source())
            .flowing(() -> new BeetrootSoupFluid().new Flowing())
            .build();

    //南瓜汤
    public static final FluidEntry PUMPKIN_SOUP_FLUID = IdyllicFoodDiary.REGISTRY.fluid()
            .id("pumpkin_soup_fluid")
            .bound(() -> new PumpkinSoupFluid().new FluidBlock())
            .still(() -> new PumpkinSoupFluid().new Source())
            .flowing(() -> new PumpkinSoupFluid().new Flowing())
            .build();

    //藏书羊肉汤
    public static final FluidEntry CANG_SHU_MUTTON_SOUP_FLUID = IdyllicFoodDiary.REGISTRY.fluid()
            .id("cang_shu_mutton_soup_fluid")
            .bound(() -> new CangShuMuttonSoupFluid().new FluidBlock())
            .still(() -> new CangShuMuttonSoupFluid().new Source())
            .flowing(() -> new CangShuMuttonSoupFluid().new Flowing())
            .build();

    //紫菜蛋花汤
    public static final FluidEntry SEAWEED_EGG_DROP_SOUP_FLUID = IdyllicFoodDiary.REGISTRY.fluid()
            .id("seaweed_egg_drop_soup_fluid")
            .bound(() -> new SeaweedEggDropSoupFluid().new FluidBlock())
            .still(() -> new SeaweedEggDropSoupFluid().new Source())
            .flowing(() -> new SeaweedEggDropSoupFluid().new Flowing())
            .build();

}
