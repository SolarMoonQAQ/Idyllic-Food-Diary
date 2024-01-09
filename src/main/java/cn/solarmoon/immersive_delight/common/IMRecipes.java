package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.recipes.CupRecipe;
import cn.solarmoon.immersive_delight.common.recipes.KettleRecipe;
import cn.solarmoon.immersive_delight.common.recipes.RollingPinRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.*;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMRecipes {

    //注册配方
    //擀面杖
    public static final String ROLLING_ID = "rolling";
    public static final RegistryObject<RecipeType<RollingPinRecipe>> ROLLING_RECIPE = RECIPE_TYPES.register(ROLLING_ID, () -> registerRecipeType(ROLLING_ID));
    public static final RegistryObject<RecipeSerializer<?>> ROLLING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(ROLLING_ID, RollingPinRecipe.Serializer::new);

    //水壶
    public static final String KETTLE_ID = "kettle";
    public static final RegistryObject<RecipeType<KettleRecipe>> KETTLE_RECIPE = RECIPE_TYPES.register(KETTLE_ID, () -> registerRecipeType(KETTLE_ID));
    public static final RegistryObject<RecipeSerializer<?>> KETTLE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(KETTLE_ID, KettleRecipe.Serializer::new);

    //水杯
    public static final String CUP_ID = "cup";
    public static final RegistryObject<RecipeType<CupRecipe>> CUP_RECIPE = RECIPE_TYPES.register(CUP_ID, () -> registerRecipeType(CUP_ID));
    public static final RegistryObject<RecipeSerializer<?>> CUP_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(CUP_ID, CupRecipe.Serializer::new);

}
