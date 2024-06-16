package cn.solarmoon.idyllic_food_diary.feature.capability;

import cn.solarmoon.idyllic_food_diary.feature.spice.SpicesCap;
import cn.solarmoon.idyllic_food_diary.registry.common.IMCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class FoodItemData implements ICapabilitySerializable<CompoundTag>, IFoodItemData {

    private final LazyOptional<FoodItemData> foodItemData;
    private final ItemStack stack;
    private final SpicesCap spicesData;

    public FoodItemData(ItemStack stack) {
        this.stack = stack;
        this.foodItemData = LazyOptional.of(() -> this);
        this.spicesData = new SpicesCap();
    }

    @Override
    public SpicesCap getSpicesData() {
        return spicesData;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.put("SpicesData", spicesData.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        spicesData.deserializeNBT(nbt.getCompound("SpicesData"));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == IMCapabilities.FOOD_ITEM_DATA) {
            return foodItemData.cast();
        }
        return LazyOptional.empty();
    }
}
