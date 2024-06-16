package cn.solarmoon.idyllic_food_diary.feature.spice;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.INBTSerializable;

public class SpicesCap implements INBTSerializable<CompoundTag> {

    public static final String SPICES = "Spices";
    private final SpiceList spices;

    public SpicesCap() {
        spices = new SpiceList();
    }

    /**
     * 注意，一般不用这个方法修改内容物！
     */
    public SpiceList getSpices() {
        return spices;
    }

    public boolean isEmpty() {
        return spices.isEmpty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put(SPICES, spices.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        spices.deserializeNBT(nbt.getList(SPICES, ListTag.TAG_COMPOUND));
    }

}
