package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.recipe.*;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.RecipeEntry;

public enum IMRecipes implements IRegister {
    INSTANCE;

    //擀面杖
    public static final RecipeEntry<RollingRecipe> ROLLING = ImmersiveDelight.REGISTRY.recipe()
            .id("rolling")
            .serializer(RollingRecipe.Serializer::new)
            .build();

    //水壶
    public static final RecipeEntry<KettleRecipe> KETTLE = ImmersiveDelight.REGISTRY.recipe()
            .id("kettle")
            .serializer(KettleRecipe.Serializer::new)
            .build();

    //水杯
    public static final RecipeEntry<CupRecipe> CUP = ImmersiveDelight.REGISTRY.recipe()
            .id("cup")
            .serializer(CupRecipe.Serializer::new)
            .build();

    //菜刀
    public static final RecipeEntry<CleaverRecipe> CLEAVER = ImmersiveDelight.REGISTRY.recipe()
            .id("cleaver")
            .serializer(CleaverRecipe.Serializer::new)
            .build();

    //汤锅
    public static final RecipeEntry<SoupPotRecipe> SOUP_POT = ImmersiveDelight.REGISTRY.recipe()
            .id("soup_pot")
            .serializer(SoupPotRecipe.Serializer::new)
            .build();

}
