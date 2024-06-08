package cn.solarmoon.idyllic_food_diary.feature.logic.spice;

import cn.solarmoon.idyllic_food_diary.util.namespace.NBTList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.INBTSerializable;

public class SpicesCap implements INBTSerializable<CompoundTag> {

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
        tag.put(NBTList.SPICES, spices.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        spices.deserializeNBT(nbt.getList(NBTList.SPICES, ListTag.TAG_COMPOUND));
    }

}
