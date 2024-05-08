package cn.solarmoon.idyllic_food_diary.core.data.fluid_effects.serializer;

import cn.solarmoon.solarmoon_core.api.data.serializable.FoodValue;
import cn.solarmoon.solarmoon_core.api.data.serializable.PotionEffect;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 液体综合属性
 * 包含了药水效果、食物属性等
 */
public class FluidEffect {

    public static final Map<String, FluidEffect> MAP = new HashMap<>();

    @SerializedName("fluid")
    public String fluidId;

    @SerializedName("effects")
    @Nullable
    public List<PotionEffect> effects;

    @SerializedName("clear")
    public boolean clear;

    @SerializedName("fire")
    public int fire;

    @SerializedName("extinguishing")
    public boolean extinguishing;

    @SerializedName("food_value")
    public FoodValue foodValue;

    @SerializedName("can_always_drink")
    public boolean canAlwaysDrink;

    /**
     * 根据id获取对应药水效果
     */
    public static FluidEffect get(String fluidId) {
        return MAP.get(fluidId);
    }

    /**
     * 从stack中获取对应液体效果（nullable）
     */
    public static FluidEffect getFluidEffectFromStack(ItemStack stack) {
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        return get(tankStack.getFluidInTank(0).getFluid().getFluidType().toString());
    }

    /**
     * 从blockEntity中获取对应液体效果（nullable）
     */
    public static FluidEffect getFluidEffectFromBlock(BlockEntity blockEntity) {
        IFluidHandler tankStack = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
        return get(tankStack.getFluidInTank(0).getFluid().getFluidType().toString());
    }

    /**
     * 防止foodValue返回null
     */
    public FoodValue getFoodValue() {
        if(foodValue == null) return new FoodValue(0,0);
        return foodValue;
    }

    /**
     * 让fluid id作为必填项
     */
    public void validate() throws JsonParseException {
        if(fluidId == null) throw new JsonParseException("Missing fluid id.");
    }

}

