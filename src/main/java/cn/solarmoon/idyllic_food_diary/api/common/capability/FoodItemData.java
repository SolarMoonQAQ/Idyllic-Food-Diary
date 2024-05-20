package cn.solarmoon.idyllic_food_diary.api.common.capability;

import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.SpicesData;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMCapabilities;
import cn.solarmoon.solarmoon_core.api.common.capability.ItemStackData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class FoodItemData extends ItemStackData implements IFoodItemData {

    private final LazyOptional<FoodItemData> foodItemData;
    private final SpicesData spicesData;

    public FoodItemData(ItemStack stack) {
        super(stack);
        this.foodItemData = LazyOptional.of(() -> this);
        this.spicesData = new SpicesData();
    }

    @Override
    public SpicesData getSpicesData() {
        return spicesData;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.put("SpicesData", spicesData.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        spicesData.deserializeNBT(nbt.getCompound("SpicesData"));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == IMCapabilities.FOOD_ITEM_DATA) {
            return foodItemData.cast();
        }
        return super.getCapability(cap, side);
    }
}
