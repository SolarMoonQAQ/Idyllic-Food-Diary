package cn.solarmoon.idyllic_food_diary.feature.tea_brewing;

import cn.solarmoon.solarmoon_core.api.data.FoodValue;
import cn.solarmoon.solarmoon_core.api.data.IngredientData;
import cn.solarmoon.solarmoon_core.api.data.PotionEffect;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TeaIngredient {

    public static final String TAG_KEY = "TeaIngredient";
    public static final String CUSTOM_NAME = "CustomName";
    public static final List<TeaIngredient> ALL = new ArrayList<>();

    @SerializedName("ingredient")
    private IngredientData ingredient;

    @SerializedName("type")
    private String type;

    /**
     * 当type为BASE时需要填入此项，将会决定当此基底放入对应液体中后的输出液体
     */
    @SerializedName("fluid_bound")
    private List<FluidBoundMap> fluidBoundMap;

    @SerializedName("time")
    private int time;

    /**
     * 此类物品加入配方后的最大冲泡液体量，最终最大冲泡量会由所有茶料累加而计算，这一项将决定茶水的水量不能高于这个值
     */
    @SerializedName("max_brewing_volume")
    private int maxBrewingVolume;

    /**
     * 此类物品加入配方后的液体消耗量，最终消耗量会由所有茶料累加而计算
     */
    @SerializedName("fluid_consumption")
    private int fluidConsumption;

    /**
     * 当此项为true时，泡茶需要在原有的基础上，下方还需有热源方块才可
     */
    @SerializedName("need_heating")
    private boolean needHeating;

    @SerializedName("effects")
    private List<PotionEffect> effects;

    @SerializedName("clear")
    private boolean clear;

    @SerializedName("fire")
    private int fire;

    @SerializedName("extinguishing")
    private boolean extinguishing;

    @SerializedName("food_value")
    private FoodValue foodValue;

    /**
     * 当存在food value时，默认玩家饱食度满的话无法进食，此时使用这个可以控制是否总是可以进食。
     */
    @SerializedName("can_always_drink")
    private boolean canAlwaysDrink;

    public enum Type {
        BASE, SIDE, ADD;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

    }

    public Ingredient getIngredient() {
        return ingredient.get();
    }

    public Type getType() {
        return Type.valueOf(type.toUpperCase());
    }

    public List<FluidBoundMap> getFluidBoundMap() {
        return fluidBoundMap == null ? new ArrayList<>() : fluidBoundMap;
    }

    public int getTime() {
        return time;
    }

    public int getMaxBrewingVolume() {
        return maxBrewingVolume;
    }

    public int getFluidConsumption() {
        return fluidConsumption;
    }

    public boolean needHeating() {
        return needHeating;
    }

    public List<PotionEffect> getEffects() {
        return effects == null ? new ArrayList<>() : effects;
    }

    public boolean canClearAllEffect() {
        return clear;
    }

    public int getFireTime() {
        return fire;
    }

    public boolean canExtinguishing() {
        return extinguishing;
    }

    public FoodValue getFoodValue() {
        return foodValue == null ? new FoodValue(0, 0) : foodValue;
    }

    public boolean canAlwaysDrink() {
        return !getFoodValue().isValid() || canAlwaysDrink;
    }

    public boolean isBase() {
        return getType() == Type.BASE;
    }

    public void validate() {
        if (getType() == Type.BASE && getFluidBoundMap() == null) {
            throw new JsonParseException("Base tea ingredient must bind fluid that can be output and input.");
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        String json = SerializeHelper.GSON.toJson(this);
        tag.putString(TAG_KEY, json);
        return tag;
    }

    public static TeaIngredient readFromNBT(CompoundTag nbt) {
        String json = nbt.getString(TAG_KEY);
        return SerializeHelper.GSON.fromJson(json, TeaIngredient.class);
    }

    public static List<TeaIngredient> readFromFluidStack(FluidStack fluidStack) {
        if (!fluidStack.isEmpty()) {
            String json = fluidStack.getOrCreateTag().getString(TAG_KEY);
            java.lang.reflect.Type type = new TypeToken<List<TeaIngredient>>() {
            }.getType();
            List<TeaIngredient> teaIngredients = SerializeHelper.GSON.fromJson(json, type);
            if (teaIngredients != null) {
                return teaIngredients;
            }
        }
        return new ArrayList<>();
    }

    public void put() {
        ALL.add(this);
    }

    public static List<TeaIngredient> getAll() {
        return ALL;
    }

    @Override
    public String toString() {
        return "TeaIngredient{" +
                "ingredient=" + ingredient +
                ", type='" + type + '\'' +
                ", fluidBoundMap=" + fluidBoundMap +
                ", time=" + time +
                ", maxBrewingVolume=" + maxBrewingVolume +
                ", fluidConsumption=" + fluidConsumption +
                ", needHeating=" + needHeating +
                ", effects=" + effects +
                ", clear=" + clear +
                ", fire=" + fire +
                ", extinguishing=" + extinguishing +
                ", foodValue=" + foodValue +
                ", canAlwaysDrink=" + canAlwaysDrink +
                '}';
    }
}
