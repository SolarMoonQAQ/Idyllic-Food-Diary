package cn.solarmoon.idyllic_food_diary.feature.tea_brewing;

import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp;
import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidBoundMap {

    @SerializedName("input")
    private String in;

    @SerializedName("output")
    private String out;

    @SerializedName("temp")
    private String temp;

    public Fluid getInput() {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation(in));
    }

    public Fluid getOutput() {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation(out));
    }

    public Temp getTemp() {
        if (temp == null) temp = "any";
        return Temp.valueOf(temp.toUpperCase());
    }

    @Override
    public String toString() {
        return "FluidBoundMap{" +
                "in='" + in + '\'' +
                ", out='" + out + '\'' +
                '}';
    }
}
