package cn.solarmoon.idyllic_food_diary.api.common.capability.serializable;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 调料专用表，add后会找到相同的添加数量，没找到才会调用原来的add
 */
public class SpiceList extends ArrayList<Spice> implements INBTSerializable<ListTag> {

    /**
     * 必须和此类中的write配合使用
     */
    public static SpiceList readSpices(FriendlyByteBuf buffer) {
        SpiceList spices = new SpiceList();
        int itemCount = buffer.readVarInt();
        for (int i = 0; i < itemCount; i++) {
            spices.add(Spice.readFromNetwork(buffer));
        }
        return spices;
    }

    /**
     * 必须和此类中的read配合使用
     */
    public static void writeSpices(FriendlyByteBuf buffer, SpiceList spices) {
        buffer.writeVarInt(spices.size());
        for (Spice spice : spices) {
            spice.toNetwork(buffer);
        }
    }

    public static SpiceList readSpices(JsonObject json, String id) {
        SpiceList spices = new SpiceList();
        if (json.has(id)) {
            for (var element : GsonHelper.getAsJsonArray(json, id)) {
                spices.add(Spice.readFromJson(element.getAsJsonObject()));
            }
        }
        return spices;
    }

    @Override
    public ListTag serializeNBT() {
        ListTag listTag = new ListTag();
        for (Spice spices : this) {
            listTag.add(spices.serializeNBT());
        }
        return listTag;
    }

    @Override
    public void deserializeNBT(ListTag listNBT) {
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundTag tag = listNBT.getCompound(i);
            add(Spice.readFromNBT(tag));
        }
    }

    @Override
    public boolean add(Spice spice) {
        for (Spice origin : this) {
            if (spice.isSame(origin)) {
                origin.add(spice.getAmount());
                return true;
            }
        }
        return super.add(spice);
    }

    @Override
    public boolean addAll(Collection<? extends Spice> spices) {
        boolean isModified = false;
        for (var spice : spices) {
            isModified |= add(spice);
        }
        return isModified;
    }

    public static SpiceList copyOf(SpiceList spices) {
        SpiceList copy = new SpiceList();
        copy.addAll(spices);
        return copy;
    }

}
