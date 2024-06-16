package cn.solarmoon.idyllic_food_diary.feature.tea_brewing;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;

public class Temp implements INBTSerializable<CompoundTag> {

    public static final String TEMP = "Temp";
    public static final String LEVEL = "Level";

    private Level level;

    public Temp() {
        this.level = Level.COMMON;
    }

    public Temp(Level level) {
        this.level = level;
    }

    public boolean isSame(Level level) {
        return this.level == level;
    }

    public Level getLevel() {
        return level;
    }

    public Temp setLevel(Level level) {
        this.level = level;
        return this;
    }

    public enum Level {
        HOT, COMMON, COLD;

        public Component getPrefix() {
            if (this == COMMON) return Component.empty();
            return IdyllicFoodDiary.TRANSLATOR.set("fluid", "temp." + this.toString().toLowerCase());
        }

    }

    public static void setFluidTemp(FluidStack fluidStack, Temp temp) {
        fluidStack.getOrCreateTag().put(TEMP, temp.serializeNBT());
    }

    public static void setFluidTemp(FluidStack fluidStack, Temp.Level level) {
        setFluidTemp(fluidStack, new Temp(level));
    }

    public static boolean shrink(FluidStack fluidStack) {
        Temp temp = getOrCreateFluidTemp(fluidStack);
        switch (temp.getLevel()) {
            case HOT -> {
                setFluidTemp(fluidStack, temp.setLevel(Level.COMMON));
                return true;
            }
            case COMMON -> {
                setFluidTemp(fluidStack, temp.setLevel(Level.COLD));
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public static Temp getOrCreateFluidTemp(FluidStack fluidStack) {
        if (!fluidStack.isEmpty()) {
            CompoundTag tag = fluidStack.getOrCreateTag();
            if (!tag.contains(TEMP)) tag.put(TEMP, new Temp().serializeNBT());
            return readFromNBT(tag.getCompound(TEMP));
        }
        return new Temp();
    }

    public static boolean isHot(FluidStack fluidStack) {
        return getOrCreateFluidTemp(fluidStack).isSame(Level.HOT);
    }

    public static boolean isCold(FluidStack fluidStack) {
        return getOrCreateFluidTemp(fluidStack).isSame(Level.COLD);
    }

    public static boolean isCommon(FluidStack fluidStack) {
        return getOrCreateFluidTemp(fluidStack).isSame(Level.COMMON);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(LEVEL, level.toString());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        level = Level.valueOf(nbt.getString(LEVEL).isEmpty() ? "COMMON" : nbt.getString(LEVEL));
    }

    public static Temp readFromNBT(CompoundTag tag) {
        Temp df = new Temp();
        df.deserializeNBT(tag);
        return df;
    }

}
