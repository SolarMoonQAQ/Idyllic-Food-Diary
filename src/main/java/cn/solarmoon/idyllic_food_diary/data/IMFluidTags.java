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
        //温水
        tag(WARM_FLUID).add(
                IMFluids.BLACK_TEA.getStill(),
                IMFluids.BLACK_TEA.getFlowing(),
                IMFluids.GREEN_TEA.getStill(),
                IMFluids.GREEN_TEA.getFlowing(),
                IMFluids.MILK_TEA.getStill(),
                IMFluids.MILK_TEA.getFlowing(),
                IMFluids.CANG_SHU_MUTTON_SOUP.getStill(),
                IMFluids.CANG_SHU_MUTTON_SOUP.getFlowing(),
                IMFluids.MUSHROOM_STEW.getStill(),
                IMFluids.MUSHROOM_STEW.getFlowing()
        )
                .replace(false);
    }

    public static final TagKey<Fluid> WARM_FLUID = fluidTag("warm_fluid");

    private static TagKey<Fluid> fluidTag(String path) {
        return FluidTags.create(new ResourceLocation(IdyllicFoodDiary.MOD_ID, path));
    }

}
