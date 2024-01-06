package cn.solarmoon.immersive_delight.init;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

import static net.minecraftforge.fml.config.ModConfig.Type.COMMON;


@Mod.EventBusSubscriber(modid = ImmersiveDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    public static final String PATH =  ImmersiveDelight.MOD_ID + ".toml";
    public static ForgeConfigSpec common;

    //===

    public static ForgeConfigSpec.ConfigValue<Boolean> deBug;

    //

    public static String SYSTEM_ROLLING = "擀面杖系统";

    public static ForgeConfigSpec.ConfigValue<Integer> areaHarvestRadius;

    //

    public static String SYSTEM_DRINKING = "饮用系统";

    public static String SETTING_CAPACITY = "容量设置";
    public static ForgeConfigSpec.ConfigValue<Integer> maxCeladonCupCapacity;
    public static ForgeConfigSpec.ConfigValue<Integer> maxKettleCapacity;

    public static String SETTING_DRINKING = "饮用设置";
    public static ForgeConfigSpec.ConfigValue<Integer> drinkingConsumption;
    public static ForgeConfigSpec.ConfigValue<Integer> useAmountForDrinking;

    //===

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        deBug = builder.comment("Used for test")
                .comment("用于调试")
                .define("deBug", false);

        //

        builder.comment("Rolling Pin System")
                .push(SYSTEM_ROLLING);
        areaHarvestRadius = builder.comment("Radius of the area harvesting skill. (B)")
                .comment("范围收割技能的半径（块）")
                .defineInRange("areaHarvestRadius", 2, 0, 50);
        builder.pop();

        //

        builder.comment("Drinking System")
                .push(SYSTEM_DRINKING);

        builder.comment("Capacity Settings")
                .push(SETTING_CAPACITY);
        maxCeladonCupCapacity = builder.comment("The maximum capacity of celadon cup. (mB)")
                .comment("青瓷杯的最大容量 (mB)")
                .defineInRange("maxCeladonCupVolume", 250, 50, 1000);
        maxKettleCapacity = builder.comment("The maximum capacity of Kettle. (mB)")
                .comment("烧水壶的最大容量 (mB)")
                .defineInRange("maxKettleCapacity", 1000, 1000, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Drinking Settings")
                .push(SETTING_DRINKING);
        drinkingConsumption = builder.comment("Amount of liquid consumed per drink. (mB)")
                .comment("每次饮用需要消耗的液体量 (mB)")
                .defineInRange("drinkingConsumption", 50, 0, Integer.MAX_VALUE);
        useAmountForDrinking = builder.comment("Number of uses(right click) required for drinking from cup block. (mB)")
                .comment("对着杯子方块右键多少次后才能饮用 (mB)")
                .defineInRange("useAmountForDrinking", 2, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.pop();

        //

        common = builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(COMMON, Config.common,
                FMLPaths.CONFIGDIR.get().resolve(PATH).toString());
    }
}
