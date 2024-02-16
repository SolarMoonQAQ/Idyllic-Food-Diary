package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.fluid.BlackTeaFluid;
import cn.solarmoon.immersive_delight.common.fluid.GreenTeaFluid;
import cn.solarmoon.immersive_delight.common.fluid.HotMilkFluid;
import cn.solarmoon.immersive_delight.common.fluid.MilkTeaFluid;
import cn.solarmoon.immersive_delight.common.fluid.no_bucket.CangShuMuttonSoupFluid;
import cn.solarmoon.immersive_delight.common.fluid.no_bucket.MushroomStewFluid;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.FluidEntry;


public enum IMFluids implements IRegister {
    INSTANCE;

    //热水
    public static final FluidEntry HOT_WATER = ImmersiveDelight.REGISTRY.fluid()
            .id("hot_water")
            .bound(() -> new HotMilkFluid().new FluidBlock())
            .still(() -> new HotMilkFluid().new Source())
            .flowing(() -> new HotMilkFluid().new Flowing())
            .bucket(() -> new HotMilkFluid().new Bucket())
            .build();

    //热牛奶
    public static final FluidEntry HOT_MILK = ImmersiveDelight.REGISTRY.fluid()
            .id("hot_milk")
            .bound(() -> new HotMilkFluid().new FluidBlock())
            .still(() -> new HotMilkFluid().new Source())
            .flowing(() -> new HotMilkFluid().new Flowing())
            .bucket(() -> new HotMilkFluid().new Bucket())
            .build();

    //红茶
    public static final FluidEntry BLACK_TEA = ImmersiveDelight.REGISTRY.fluid()
            .id("black_tea")
            .bound(() -> new BlackTeaFluid().new FluidBlock())
            .still(() -> new BlackTeaFluid().new Source())
            .flowing(() -> new BlackTeaFluid().new Flowing())
            .bucket(() -> new BlackTeaFluid().new Bucket())
            .build();

    //绿茶
    public static final FluidEntry GREEN_TEA = ImmersiveDelight.REGISTRY.fluid()
            .id("green_tea")
            .bound(() -> new GreenTeaFluid().new FluidBlock())
            .still(() -> new GreenTeaFluid().new Source())
            .flowing(() -> new GreenTeaFluid().new Flowing())
            .bucket(() -> new GreenTeaFluid().new Bucket())
            .build();

    //奶茶
    public static final FluidEntry MILK_TEA = ImmersiveDelight.REGISTRY.fluid()
            .id("milk_tea")
            .bound(() -> new MilkTeaFluid().new FluidBlock())
            .still(() -> new MilkTeaFluid().new Source())
            .flowing(() -> new MilkTeaFluid().new Flowing())
            .bucket(() -> new MilkTeaFluid().new Bucket())
            .build();

    //蘑菇煲
    public static final FluidEntry MUSHROOM_STEW_FLUID = ImmersiveDelight.REGISTRY.fluid()
            .id("mushroom_stew_fluid")
            .bound(() -> new MushroomStewFluid().new FluidBlock())
            .still(() -> new MushroomStewFluid().new Source())
            .flowing(() -> new MushroomStewFluid().new Flowing())
            .build();

    //藏书羊肉汤
    public static final FluidEntry CANGSHU_MUTTON_SOUP_FLUID = ImmersiveDelight.REGISTRY.fluid()
            .id("cangshu_mutton_soup_fluid")
            .bound(() -> new CangShuMuttonSoupFluid().new FluidBlock())
            .still(() -> new CangShuMuttonSoupFluid().new Source())
            .flowing(() -> new CangShuMuttonSoupFluid().new Flowing())
            .build();

}
