package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.api.registry.BaseRecipeRegistry;
import cn.solarmoon.immersive_delight.common.recipes.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;

public class IMRecipes extends BaseRecipeRegistry {

    //配方
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public IMRecipes() {
        super(RECIPE_TYPES, RECIPE_SERIALIZERS);
    }

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

    //菜刀
    public static final String CLEAVER_ID = "cleaver";
    public static final RegistryObject<RecipeType<CleaverRecipe>> CLEAVER_RECIPE = RECIPE_TYPES.register(CLEAVER_ID, () -> registerRecipeType(CLEAVER_ID));
    public static final RegistryObject<RecipeSerializer<?>> CLEAVER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(CLEAVER_ID, CleaverRecipe.Serializer::new);

    //汤锅
    public static final String SOUP_POT_ID = "soup_pot";
    public static final RegistryObject<RecipeType<SoupPotRecipe>> SOUP_POT_RECIPE = RECIPE_TYPES.register(SOUP_POT_ID, () -> registerRecipeType(SOUP_POT_ID));
    public static final RegistryObject<RecipeSerializer<?>> SOUP_POT_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(SOUP_POT_ID, SoupPotRecipe.Serializer::new);



    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String identifier) {
        return new RecipeType<>()
        {
            public String toString() {
                return MOD_ID + ":" + identifier;
            }
        };
    }

}
