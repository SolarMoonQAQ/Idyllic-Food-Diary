package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.ISimpleFuelBlockEntity;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.inventory.StoveInventory;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class StoveBlockEntity extends BlockEntity implements IContainerBlockEntity, ISimpleFuelBlockEntity {

    private CookingPotBlockEntity pot;
    private final ItemStackHandler inventory;

    private int burnTime;
    private int fuelTime;

    public StoveBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.STOVE.get(), pos, state);
        this.inventory = new StoveInventory(this);
    }

    public void updatePot() {
        ItemStack potStack = getPotItem();
        if (!potStack.isEmpty()) {
            this.pot = new CookingPotBlockEntity(getBlockPos(), getBlockState());
            if (getLevel() != null) {
                pot.setLevel(getLevel());
                CompoundTag tag = potStack.getTag();
                if (tag != null) {
                    if (tag.contains(NBTList.RESULT)) pot.setResult(ItemStack.of(tag.getCompound(NBTList.RESULT)));
                    if (tag.contains(NBTList.CONTAINER)) pot.setContainer(Ingredient.fromJson(JsonParser.parseString(tag.getString(NBTList.CONTAINER))));
                    if (tag.contains(NBTList.EXP)) pot.setExp(tag.getInt(NBTList.EXP));
                    pot.setInventory(potStack);
                    pot.setFluid(FluidUtil.getFluidStack(potStack));
                }
            }
        } else pot = null;
    }

    public void updatePotItem() {
        if (!getPotItem().isEmpty() && getPot() != null) {
            CompoundTag tag = getPotItem().getOrCreateTag();
            if (getPot() != null) {
                tag.put(NBTList.RESULT, getPot().getResult().save(new CompoundTag()));
                tag.putString(NBTList.CONTAINER, getPot().getContainer().toJson().toString());
                tag.putInt(NBTList.EXP, getPot().getExp());
                ContainerUtil.setInventory(getPotItem(), getPot());
                FluidUtil.setTank(getPotItem(), getPot());
            }
        }
    }

    /**
     * 改为了不会提取锅
     */
    public ItemStack extractItem(int count) {
        int maxSlots = this.getInventory().getSlots();
        ItemStack stack = ItemStack.EMPTY;

        for(int i = 0; i < maxSlots; ++i) {
            int slot = maxSlots - i - 1;
            if (slot != 0) {
                stack = this.getInventory().extractItem(slot, count, false);
                if (!stack.isEmpty()) {
                    break;
                }
            }
        }

        return stack;
    }

    public void resetPot() {
        pot = null;
    }

    @Nullable
    public CookingPotBlockEntity getPot() {
        return pot;
    }

    public ItemStack getPotItem() {
        return inventory.getStackInSlot(0);
    }

    public boolean hasFirewood() {
        return !getInventory().getStackInSlot(1).isEmpty();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (pot != null) {
            tag.put("Pot", pot.serializeNBT());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag potTag = tag.getCompound("Pot");
        if (!potTag.isEmpty()) {
            pot = new CookingPotBlockEntity(getBlockPos(), getBlockState());
            pot.deserializeNBT(potTag);
        }
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public ItemStack getFuel() {
        return getInventory().getStackInSlot(1);
    }

    @Override
    public int getBurnTime() {
        return burnTime;
    }

    @Override
    public void setBurnTime(int time) {
        burnTime = time;
    }

    @Override
    public int getFuelTime() {
        return fuelTime;
    }

    @Override
    public void setFuelTime(int time) {
        fuelTime = time;
    }

}
