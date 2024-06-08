package cn.solarmoon.idyllic_food_diary.data;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.registry.common.IMFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class IMFluidTags extends FluidTagsProvider {

    public IMFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, IdyllicFoodDiary.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        registerModTags();
    }

    protected void registerModTags() {
        //热水
        tag(HOT_FLUID).add(
                IMFluids.HOT_WATER.getStill(),
                IMFluids.HOT_WATER.getFlowing(),
                IMFluids.HOT_MILK.getStill(),
                IMFluids.HOT_MILK.getFlowing(),
                Fluids.LAVA.getFlowing(),
                Fluids.LAVA.getSource()
        ).replace(false);
        //温水
        tag(WARM_FLUID).add(
                IMFluids.BLACK_TEA.getStill(),
                IMFluids.BLACK_TEA.getFlowing(),
                IMFluids.GREEN_TEA.getStill(),
                IMFluids.GREEN_TEA.getFlowing(),
                IMFluids.MILK_TEA.getStill(),
                IMFluids.MILK_TEA.getFlowing(),
                IMFluids.CANG_SHU_MUTTON_SOUP_FLUID.getStill(),
                IMFluids.CANG_SHU_MUTTON_SOUP_FLUID.getFlowing(),
                IMFluids.MUSHROOM_STEW_FLUID.getStill(),
                IMFluids.MUSHROOM_STEW_FLUID.getFlowing()
        )
                .addTag(HOT_FLUID)
                .replace(false);
        //原版水
        tag(FluidTags.WATER).add(
                IMFluids.HOT_WATER.getStill(),
                IMFluids.HOT_WATER.getFlowing()
        ).replace(false);
    }

    public static final TagKey<Fluid> HOT_FLUID = fluidTag("hot_fluid");
    public static final TagKey<Fluid> WARM_FLUID = fluidTag("warm_fluid");

    private static TagKey<Fluid> fluidTag(String path) {
        return FluidTags.create(new ResourceLocation(IdyllicFoodDiary.MOD_ID, path));
    }

}
