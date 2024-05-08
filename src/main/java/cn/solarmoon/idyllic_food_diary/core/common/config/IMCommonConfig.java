package cn.solarmoon.idyllic_food_diary.core.common.config;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.solarmoon_core.api.config.SolarConfigBuilder;
import cn.solarmoon.solarmoon_core.api.util.RegisterHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class IMCommonConfig {

    public static final SolarConfigBuilder builder = SolarConfigBuilder.create()
            .modId(IdyllicFoodDiary.MOD_ID)
            .side(ModConfig.Type.COMMON);

    public static final ForgeConfigSpec.ConfigValue<Boolean> deBug;

    static {
        deBug = builder.comment("Used for test")
                .comment("用于调试")
                .define("deBug", false);
    }

    public static void register() {
        RegisterHelper.register(builder);
    }

}
