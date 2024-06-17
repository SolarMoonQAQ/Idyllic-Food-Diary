package cn.solarmoon.idyllic_food_diary.feature.tea_brewing;

import cn.solarmoon.solarmoon_core.api.data.FoodValue;
import cn.solarmoon.solarmoon_core.api.data.IngredientData;
import cn.solarmoon.solarmoon_core.api.data.PotionEffect;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import joptsimple.internal.Strings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TeaIngredient {

    public static final TeaIngredientList ALL = new TeaIngredientList();

    public static final String TAG_KEY = "TeaIngredient";
    public static final String CUSTOM_NAME = "CustomName";

    @SerializedName("type")
    public String type;

    /**
     * 茶成分所绑定的物品/物品类型
     */
    @SerializedName("ingredient")
    private IngredientData ingredient;

    /**
     * 冲泡的时间，累加，目前为固定时间
     */
    @SerializedName("time")
    private int time;

    /**
     * 此类物品加入烹饪后的最大冲泡液体量，最终最大冲泡量会由所有茶料累加而计算，这一项使得茶水的水量不能高于这个值
     */
    @SerializedName("max_brewing_volume")
    private int maxBrewingVolume;

    /**
     * 此类物品加入烹饪后的液体消耗量，最终消耗量会由所有茶料累加而计算
     */
    @SerializedName("fluid_consumption")
    private int fluidConsumption;

    /**
     * 当此项为true时，泡茶需要在原有的基础上，下方还需有热源方块才可
     */
    @SerializedName("need_heating")
    private boolean needHeating;

    /**
     * 自定义冲泡完成的组合名
     */
    @SerializedName("display")
    private String display;

    public TeaIngredient.Type getType() {
        return TeaIngredient.Type.valueOf(type.toUpperCase());
    }

    public Ingredient getIngredient() {
        return ingredient.get();
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

    public String getDisplay() {
        return display == null ? Strings.EMPTY : display;
    }

    public enum Type {
        BASE, SIDE, ADD
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        String json = SerializeHelper.GSON.toJson(this);
        tag.putString(TAG_KEY, json);
        return tag;
    }

    @Override
    public String toString() {
        return "TeaIngredient{" +
                "type='" + type + '\'' +
                ", ingredient=" + ingredient +
                ", time=" + time +
                ", maxBrewingVolume=" + maxBrewingVolume +
                ", fluidConsumption=" + fluidConsumption +
                ", needHeating=" + needHeating +
                ", display='" + display + '\'' +
                '}';
    }

    public static TeaIngredient readFromNBT(CompoundTag nbt) {
        String json = nbt.getString(TAG_KEY);
        Type type = SerializeHelper.GSON.fromJson(json, TeaIngredient.class).getType();
        return switch (type) {
            case BASE -> SerializeHelper.GSON.fromJson(json, Base.class);
            case ADD -> SerializeHelper.GSON.fromJson(json, Add.class);
            case SIDE -> SerializeHelper.GSON.fromJson(json, Side.class);
        };
    }

    public static TeaIngredientList readFromFluidStack(FluidStack fluidStack) {
        TeaIngredientList teaIngredients = new TeaIngredientList();
        if (!fluidStack.isEmpty() && fluidStack.getTag() != null && fluidStack.getTag().contains(TAG_KEY)) {
            String json = fluidStack.getTag().getString(TAG_KEY);
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                TeaIngredient.Type type = SerializeHelper.GSON.fromJson(jsonObject, TeaIngredient.class).getType();
                TeaIngredient teaIngredient = switch (type) {
                    case BASE -> SerializeHelper.GSON.fromJson(jsonObject, Base.class);
                    case SIDE -> SerializeHelper.GSON.fromJson(jsonObject, Side.class);
                    case ADD -> SerializeHelper.GSON.fromJson(jsonObject, Add.class);
                };
                teaIngredients.add(teaIngredient);
            }
        }
        return teaIngredients;
    }


    public static class Base extends TeaIngredient {

        public Base() {
            type = "base";
        }

        /**
         * 将会决定当此基底放入对应液体中后的输出液体
         */
        @SerializedName("fluid_bound")
        private List<FluidBoundMap> fluidBoundMap;

        public List<FluidBoundMap> getFluidBoundMap() {
            return fluidBoundMap == null ? new ArrayList<>() : fluidBoundMap;
        }

        @Override
        public String toString() {
            return super.toString() + "-" +
                    "Base{" +
                    "fluidBoundMap=" + fluidBoundMap +
                    '}';
        }
    }

    public static class Add extends TeaIngredient {

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

        @Override
        public String toString() {
            return super.toString() + "-" +
                    "Add{" +
                    "effects=" + effects +
                    ", clear=" + clear +
                    ", fire=" + fire +
                    ", extinguishing=" + extinguishing +
                    ", foodValue=" + foodValue +
                    ", canAlwaysDrink=" + canAlwaysDrink +
                    '}';
        }
    }

    public static class Side extends Add {

    }

}
