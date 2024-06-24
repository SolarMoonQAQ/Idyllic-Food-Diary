package cn.solarmoon.idyllic_food_diary.feature.tea_brewing;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class Temp implements INBTSerializable<CompoundTag> {

    public static final String TEMP = "Temp";
    public static final String SCALE = "Scale";
    public static final String TIME = "Time";

    private Scale scale;
    private long gameTime;

    public Temp(long gameTime) {
        this.scale = Scale.COMMON;
        this.gameTime = gameTime;
    }

    public Temp(Scale scale, long gameTime) {
        this.scale = scale;
        this.gameTime = gameTime;
    }

    /**
     * @param scale 温度等级
     * @return 除了比较温度等级本身以外，如果任意一个为EMPTY，就可以接受任意温度等级通过
     */
    public boolean isSame(Scale scale) {
        return this.scale == scale || scale == Scale.ANY || this.scale == Scale.ANY;
    }

    public long getGameTime() {
        return gameTime;
    }

    public Temp setGameTime(long gameTime) {
        this.gameTime = gameTime;
        return this;
    }

    public Scale getScale() {
        return scale;
    }

    public Temp setScale(Scale scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public String toString() {
        return "Temp{" +
                "scale=" + scale +
                ", gameTime=" + gameTime +
                '}';
    }

    public enum Scale {
        HOT, COMMON, COLD,
        ANY;

        public Component getPrefix() {
            if (this == COMMON) return Component.empty();
            return IdyllicFoodDiary.TRANSLATOR.set("fluid", "temp." + this.toString().toLowerCase());
        }

    }

    /**
     * @return 当液体上一次变化温度的时间到现在的游戏时间一段时间后将会返回一个新的改变了温度的fluid
     */
    @Nullable
    public static FluidStack tick(FluidStack fluidStack, Level level) {
        Temp temp = getOrCreateFluidTemp(fluidStack, level);
        if (temp.getScale() != Scale.COMMON) {
            if (level.getGameTime() - temp.getGameTime() > 1200) {
                setFluidTemp(fluidStack, getOrCreateFluidTemp(fluidStack, level).setScale(Scale.COMMON), level);
                return fluidStack;
            }
        }
        return null;
    }

    /**
     * 把新温度推入液体并重置时间，注意这里只是设置了给出的fluidStack并返回
     * @return 设置好后的液体
     */
    public static FluidStack setFluidTemp(FluidStack fluidStack, Temp temp, Level level) {
        if (!fluidStack.isEmpty()) {
            fluidStack.getOrCreateTag().put(TEMP, temp.setGameTime(level.getGameTime()).serializeNBT());
        }
        return fluidStack;
    }

    public static boolean shrink(FluidStack fluidStack, Level level) {
        Temp temp = getOrCreateFluidTemp(fluidStack, level);
        switch (temp.getScale()) {
            case HOT -> {
                setFluidTemp(fluidStack, temp.setScale(Scale.COMMON), level);
                return true;
            }
            case COMMON -> {
                setFluidTemp(fluidStack, temp.setScale(Scale.COLD), level);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * 获取温度，没有就创建一个常温的当前时间的温度再返回这个创建的
     */
    public static Temp getOrCreateFluidTemp(FluidStack fluidStack, Level level) {
        if (!fluidStack.isEmpty()) {
            CompoundTag tag = fluidStack.getOrCreateTag();
            if (!tag.contains(TEMP)) tag.put(TEMP, new Temp(level.getGameTime()).serializeNBT());
            return readFromNBT(tag.getCompound(TEMP));
        }
        return new Temp(level.getGameTime());
    }

    /**
     * @return 从液体身上读取一个温度，如果没有，则默认为常温。注意没有的话不会返回当前时间
     */
    public static Temp getFluidTemp(FluidStack fluidStack) {
        if (!fluidStack.isEmpty()) {
            CompoundTag tag = fluidStack.getTag();
            if (tag != null && tag.contains(TEMP)) {
                return readFromNBT(tag.getCompound(TEMP));
            }
        }
        return new Temp(0);
    }

    public static boolean isHot(FluidStack fluidStack) {
        return getFluidTemp(fluidStack).isSame(Scale.HOT);
    }

    public static boolean isCold(FluidStack fluidStack) {
        return getFluidTemp(fluidStack).isSame(Scale.COLD);
    }

    public static boolean isCommon(FluidStack fluidStack) {
        return getFluidTemp(fluidStack).isSame(Scale.COMMON);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(SCALE, scale.toString());
        tag.putLong(TIME, gameTime);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        scale = Scale.valueOf(nbt.getString(SCALE).isEmpty() ? "COMMON" : nbt.getString(SCALE));
        gameTime = nbt.getLong(TIME);
    }

    public static Temp readFromNBT(CompoundTag tag) {
        return new Temp(
                Scale.valueOf(tag.getString(SCALE).isEmpty() ? "COMMON" : tag.getString(SCALE)),
                tag.getLong(TIME)
        );
    }

    public static Temp.Scale readScaleFromJson(JsonObject json) {
        return Temp.Scale.valueOf(GsonHelper.getAsString(json, "temp", "any").toUpperCase());
    }

}
