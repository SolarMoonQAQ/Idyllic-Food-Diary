package cn.solarmoon.idyllic_food_diary.common.block_entity.base;

import cn.solarmoon.idyllic_food_diary.common.registry.IMPacks;
import cn.solarmoon.idyllic_food_diary.util.namespace.NBTList;
import cn.solarmoon.idyllic_food_diary.util.namespace.NETList;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IIndividualTimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNBTList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static cn.solarmoon.solarmoon_core.api.common.block.ILitBlock.LIT;

/**
 * 拥有七个槽位，前六个限量1，可放任意物品，最后一个只能存入煤炭类物品，限量64
 */
public abstract class AbstractGrillBlockEntity extends BlockEntity implements IContainerBlockEntity, IIndividualTimeRecipeBlockEntity<CampfireCookingRecipe> {

    private int[] times;
    private int[] recipeTimes;
    private final ItemStackHandler inventory;
    private int burnTime;
    public int saveBurnTime;

    public AbstractGrillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.times = new int[64];
        this.recipeTimes = new int[64];
        this.inventory = new ItemStackHandler(7) {
            @Override
            public int getSlotLimit(int slot) {
                if (slot < 6) {
                    return 1;
                }
                return 64;
            }
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (slot < 6 && !stack.is(ItemTags.COALS)) {
                    return true;
                } else return slot >= 6 && stack.is(ItemTags.COALS);
            }
        };
    }

    public boolean noFuel() {
        return !inventory.getStackInSlot(6).is(ItemTags.COALS);
    }

    public int getBurnTime() {
        return burnTime;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    @Override
    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public int[] getTimes() {
        return times;
    }

    @Override
    public int[] getRecipeTimes() {
        return recipeTimes;
    }

    @Override
    public void setTimes(int[] ints) {
        times = ints;
    }

    @Override
    public void setRecipeTimes(int[] ints) {
        recipeTimes = ints;
    }

    public void tryCook() {
        ItemStackHandler inv = getInventory();
        BlockPos pos = getBlockPos();
        for (int i = 0; i < inv.getSlots(); i++) {
            Optional<CampfireCookingRecipe> recipeOp = getCheckedRecipe(i);
            if (recipeOp.isPresent()) {
                CampfireCookingRecipe recipe = recipeOp.get();
                getRecipeTimes()[i] = recipe.getCookingTime() / 3;
                getTimes()[i] = getTimes()[i]+ 1;
                if (getTimes()[i] >= recipe.getCookingTime() / 3 && level != null) {
                    ItemStack out = recipe.getResultItem(level.registryAccess());
                    inv.setStackInSlot(i, out);
                    //这里设置slot必须在服务端侧同步（不知道为什么）
                    CompoundTag nbt = new CompoundTag();
                    nbt.put(SolarNBTList.INVENTORY, inv.serializeNBT());
                    if (level.isClientSide) IMPacks.SERVER_PACK.getSender().send(NETList.SYNC_SLOT_SET, pos, nbt);
                    getTimes()[i] = 0;
                    setChanged();
                }
            } else {
                getTimes()[i] = 0;
                getRecipeTimes()[i] = 0;
            }
        }
    }

    public void tryControlLit() {
        BlockState state = getBlockState();
        BlockPos pos = getBlockPos();
        //消耗煤炭，控制lit属性
        if (state.getValue(LIT)) {
            //有燃料就保存其燃烧时间，并且消耗一个
            if (!noFuel() && saveBurnTime == 0) {
                saveBurnTime = ForgeHooks.getBurnTime(getInventory().getStackInSlot(6), null);
                getInventory().getStackInSlot(6).shrink(1);
                setChanged();
            }
            setBurnTime(getBurnTime() + 1);
            //燃烧时间超过燃料所能提供，就刷新
            if (getBurnTime() >= saveBurnTime) {
                setBurnTime(0);
                saveBurnTime = 0;
            }
            //无燃料且没有燃烧时间了就停止lit
            if (noFuel() && getBurnTime() == 0) {
                if (level != null) {
                    level.setBlock(pos, state.setValue(LIT, false), 3);
                }
                setChanged();
            }
        }
    }

    @Override
    public Optional<CampfireCookingRecipe> getCheckedRecipe(int index) {
        Level level = getLevel();
        if (level == null) return Optional.empty();
        BlockState state = getBlockState();
        List<CampfireCookingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.CAMPFIRE_COOKING);
        ItemStack stack = getInventory().getStackInSlot(index);
        return recipes.stream().filter(recipe ->
                recipe.getIngredients().get(0).test(stack) && state.getValue(LIT)).findFirst();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt(NBTList.BURNING_TIME, getBurnTime());
        nbt.putInt(NBTList.BURNING_TIME_SAVING, saveBurnTime);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        setBurnTime(nbt.getInt(NBTList.BURNING_TIME));
        saveBurnTime = nbt.getInt(NBTList.BURNING_TIME_SAVING);
    }

}
