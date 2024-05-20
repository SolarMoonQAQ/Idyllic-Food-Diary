package cn.solarmoon.idyllic_food_diary.api.common.capability.serializable;

import cn.solarmoon.solarmoon_core.api.util.SerializeHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Spice implements INBTSerializable<CompoundTag> {

    public static Spice EMPTY = new Spice("", Type.EMPTY, Step.EMPTY, 0);
    public static int FLUID_STOCK = 50;
    public static int ITEM_STOCK = 1;

    private String id;
    private Type type;
    private Step step;
    private int amount;

    public Spice(Item item, Step step) {
        id = ForgeRegistries.ITEMS.getKey(item).toString();
        type = Type.ITEM;
        this.step = step;
        amount = ITEM_STOCK;
    }

    public Spice(Fluid fluid, Step step) {
        id = ForgeRegistries.FLUIDS.getKey(fluid).toString();
        type = Type.FLUID;
        this.step = step;
        amount = FLUID_STOCK;
    }

    public Spice(String id, Type type, Step step, int amount) {
        this.id = id;
        this.type = type;
        this.step = step;
        this.amount = amount;
    }

    /**
     * 添加调料量
     */
    public void add(int count) {
        if (type == Type.FLUID) {
            amount = amount + count * FLUID_STOCK;
        } else if (type == Type.ITEM) {
            amount = amount + count * ITEM_STOCK;
        }
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Step getStep() {
        return step;
    }

    /**
     * 不比较数量，其它全比较
     */
    public boolean isSame(Spice spice) {
        return Objects.equals(id, spice.id) && type == spice.type && step == spice.step;
    }

    public Component getDisplayName() {
        if (type == Type.ITEM) {
            return Component.translatable(ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)).getDescriptionId());
        }
        if (type == Type.FLUID) {
            return Component.translatable(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(id)).getFluidType().getDescriptionId());
        }
        return Component.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", id);
        tag.putString("type", type.name());
        tag.putString("step", step.name());
        tag.putInt("amount", amount);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        id = nbt.getString("id");
        type = Type.valueOf(nbt.getString("type"));
        step = Step.valueOf(nbt.getString("step"));
        amount = nbt.getInt("amount");
    }

    public static Spice readFromNBT(CompoundTag nbt) {
        String id = nbt.getString("id");
        Type type = Type.valueOf(nbt.getString("type"));
        Step step = Step.valueOf(nbt.getString("step"));
        int amount = nbt.getInt("amount");
        return new Spice(id, type, step, amount);
    }

    public static Spice readFromJson(JsonObject json, String id) {
        if (json.has(id)) {
            JsonObject j = json.getAsJsonObject(id);
            return readFromJson(j);
        }
        return EMPTY;
    }

    public static Spice readFromJson(JsonObject json) {
        String id;
        if (json.has("item")) {
            id = GsonHelper.getAsString(json, "item");
            Type type = Type.ITEM;
            Step step = Step.valueOf(GsonHelper.getAsString(json, "step").toUpperCase());
            int amount = GsonHelper.getAsInt(json, "amount");
            return new Spice(id, type, step, amount);
        } else if (json.has("fluid")) {
            id = GsonHelper.getAsString(json, "fluid");
            Type type = Type.FLUID;
            Step step = Step.valueOf(GsonHelper.getAsString(json, "step").toUpperCase());
            int amount = GsonHelper.getAsInt(json, "amount");
            if (amount % FLUID_STOCK != 0) throw new JsonParseException("Amount of spices for fluid type must be a multiple of " + FLUID_STOCK + ".");
            return new Spice(id, type, step, amount);
        }
        throw new RuntimeException("Spice id must be 'item' or 'fluid'.");
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(type.name());
        buf.writeUtf(step.name());
        buf.writeInt(amount);
    }

    public static Spice readFromNetwork(FriendlyByteBuf buf) {
        String id = buf.readUtf();
        Type type = Type.valueOf(buf.readUtf());
        Step step = Step.valueOf(buf.readUtf());
        int amount = buf.readInt();
        return new Spice(id, type, step, amount);
    }

    public static List<Spice> readSpices(JsonObject json, String id) {
        List<Spice> spices = new ArrayList<>();
        if (json.has(id)) {
            for (var element : GsonHelper.getAsJsonArray(json, id)) {
                spices.add(readFromJson(element.getAsJsonObject()));
            }
        }
        return spices;
    }

    /**
     * 必须和此类中的write配合使用
     */
    public static List<Spice> readSpices(FriendlyByteBuf buffer) {
        List<Spice> spices = new ArrayList<>();
        int itemCount = buffer.readVarInt();
        for (int i = 0; i < itemCount; i++) {
            spices.add(readFromNetwork(buffer));
        }
        return spices;
    }

    /**
     * 必须和此类中的read配合使用
     */
    public static void writeSpices(FriendlyByteBuf buffer, List<Spice> spices) {
        buffer.writeVarInt(spices.size());
        for (Spice spice : spices) {
            spice.toNetwork(buffer);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Spice spice = (Spice) object;
        return amount == spice.amount && Objects.equals(id, spice.id) && type == spice.type && step == spice.step;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, step, amount);
    }

    @Override
    public String toString() {
        return "Spice{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", step=" + step +
                ", amount=" + amount +
                '}';
    }

    public enum Type {ITEM, FLUID, EMPTY}
    public enum Step {ADD, MIX, EMPTY}

}
