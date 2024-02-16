package cn.solarmoon.immersive_delight.data.fluid_foods.serializer;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class FluidFood {

    public static final Map<Fluid, FluidFood> MAP = new HashMap<>();
    public static final Map<Item, FluidFood> MAP2 = new HashMap<>();

    @SerializedName("fluid")
    public String fluidId;

    @SerializedName("fluid_amount")
    public int fluidAmount;

    @SerializedName("food")
    public String foodId;

    @SerializedName("container")
    public String container;

    /**
     * 根据液体获取对应fluidFood
     */
    public static FluidFood getByFluid(Fluid fluid) {
        return MAP.get(fluid);
    }

    /**
     * 根据食物item获取对应fluidFood
     */
    public static FluidFood getByFood(Item item) {
        return MAP2.get(item);
    }

    public Fluid getFluid() {
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidId));
    }

    public Item getFood() {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(foodId));
    }

    public Item getContainer() {return ForgeRegistries.ITEMS.getValue(new ResourceLocation(container));}

    /**
     * 让fluid id作为必填项
     */
    public void validate() throws JsonParseException {
        if (fluidId == null) throw new JsonParseException("Missing fluid id.");
    }

}
