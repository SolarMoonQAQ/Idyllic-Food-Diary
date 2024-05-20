package cn.solarmoon.idyllic_food_diary.api.common.capability.serializable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpicesData implements INBTSerializable<CompoundTag> {

    private final List<Spice> spices;

    public SpicesData() {
        spices = new ArrayList<>();
    }

    /**
     * 注意，一般不用这个方法修改内容物！
     */
    public List<Spice> getSpices() {
        return spices;
    }

    /**
     * 如果只是添加调料，必须用此方法添加，否则不会在原有的基础上添加而是添加一个新的调料元素
     */
    public void add(Spice spice) {
        boolean findSame = false;
        for (Spice origin : spices) {
            if (spice.isSame(origin)) {
                origin.add(spice.getAmount());
                findSame = true;
                break;
            }
        }
        if (!findSame) {
            spices.add(spice);
        }
    }

    public void addAll(List<Spice> spices) {
        for (var spice : spices) {
            add(spice);
        }
    }

    public boolean isEmpty() {
        return spices.isEmpty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (Spice spices : spices) {
            listTag.add(spices.serializeNBT());
        }
        tag.put("Spices", listTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag listTag = nbt.getList("Spices", ListTag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag tag = listTag.getCompound(i);
            spices.add(Spice.readFromNBT(tag));
        }
    }

}
