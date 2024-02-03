package cn.solarmoon.immersive_delight.api.common.capability.serializable;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class RecipeSelectorData implements INBTSerializable<CompoundTag> {

    private int index;
    private int recipeIndex;

    public RecipeSelectorData() {

    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setRecipeIndex(int recipeIndex) {
        this.recipeIndex = recipeIndex;
    }

    public int getRecipeIndex() {
        return recipeIndex;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("index", index);
        tag.putInt("recipeIndex", recipeIndex);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

        index = nbt.getInt("index");
        recipeIndex = nbt.getInt("recipeIndex");

    }

}
