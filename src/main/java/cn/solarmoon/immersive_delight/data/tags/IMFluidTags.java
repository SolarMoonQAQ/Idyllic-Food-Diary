package cn.solarmoon.immersive_delight.data.tags;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.IMFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class IMFluidTags extends FluidTagsProvider {

    public IMFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, ImmersiveDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        registerModTags();
    }

    protected void registerModTags() {
        //热水
        tag(HOT_FLUID).add(
                IMFluids.HotWater.FLUID_STILL.get(),
                IMFluids.HotWater.FLUID_FLOWING.get(),
                IMFluids.HotMilk.FLUID_STILL.get(),
                IMFluids.HotMilk.FLUID_FLOWING.get(),
                Fluids.LAVA.getFlowing(),
                Fluids.LAVA.getSource()
        ).replace(false);
        //温水
        tag(WARM_FLUID).add(
                IMFluids.BlackTea.FLUID_STILL.get(),
                IMFluids.BlackTea.FLUID_FLOWING.get(),
                IMFluids.GreenTea.FLUID_STILL.get(),
                IMFluids.GreenTea.FLUID_FLOWING.get(),
                IMFluids.MushroomStew.FLUID_STILL.get(),
                IMFluids.MushroomStew.FLUID_FLOWING.get()
        )
                .addTag(HOT_FLUID)
                .replace(false);
        //原版水
        tag(FluidTags.WATER).add(
                IMFluids.HotWater.FLUID_STILL.get(),
                IMFluids.HotWater.FLUID_FLOWING.get()
        ).replace(false);
    }

    public static final TagKey<Fluid> HOT_FLUID = fluidTag("hot_fluid");
    public static final TagKey<Fluid> WARM_FLUID = fluidTag("warm_fluid");

    private static TagKey<Fluid> fluidTag(String path) {
        return FluidTags.create(new ResourceLocation(ImmersiveDelight.MOD_ID, path));
    }

}
