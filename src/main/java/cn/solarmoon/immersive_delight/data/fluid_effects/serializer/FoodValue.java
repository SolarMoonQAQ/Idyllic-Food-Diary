package cn.solarmoon.immersive_delight.data.fluid_effects.serializer;

import com.google.gson.annotations.SerializedName;


/**
 * 食物属性
 */
public class FoodValue {

    public FoodValue(int hunger, float saturation) {
        this.hunger = hunger;
        this.saturation = saturation;
    }

    @SerializedName("hunger")
    public int hunger;

    @SerializedName("saturation")
    public float saturation;

    /**
     * 检测至少有一项不为0（对饥饿值有帮助）
     */
    public boolean isValid() {
        return hunger > 0 || saturation > 0;
    }

}
