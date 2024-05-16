package cn.solarmoon.idyllic_food_diary.api.data.serializer;

import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidBoundMap {

    @SerializedName("input")
    private String in;

    @SerializedName("output")
    private String out;

    public Fluid getInput() {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation(in));
    }

    public Fluid getOutput() {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation(out));
    }

    @Override
    public String toString() {
        return "FluidBoundMap{" +
                "in='" + in + '\'' +
                ", out='" + out + '\'' +
                '}';
    }
}
