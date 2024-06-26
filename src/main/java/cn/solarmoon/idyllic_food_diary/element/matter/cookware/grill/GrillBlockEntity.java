package cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.ISimpleFuelBlockEntity;
import cn.solarmoon.idyllic_food_diary.network.NETList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMPacks;
import cn.solarmoon.solarmoon_core.api.blockentity_util.IContainerBE;
import cn.solarmoon.solarmoon_core.api.blockentity_util.IIndividualTimeRecipeBE;
import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * 拥有七个槽位，前六个限量1，可放任意物品，最后一个只能存入煤炭类物品，限量64
 */
public class GrillBlockEntity extends SyncedBlockEntity implements IContainerTile,
        IIndividualTimeRecipeBE<CampfireCookingRecipe>, ISimpleFuelBlockEntity {

    private int[] times;
    private int[] recipeTimes;
    private final TileInventory inventory;
    private int burnTime;
    public int saveBurnTime;

    public GrillBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.GRILL.get(), pos, state);
        this.times = new int[64];
        this.recipeTimes = new int[64];
        this.inventory = new TileInventory(7, this) {
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

    @Override
    public ItemStack getFuel() {
        return getInventory().getStackInSlot(6);
    }

    @Override
    public int getBurnTime() {
        return burnTime;
    }

    @Override
    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    @Override
    public int getFuelTime() {
        return saveBurnTime;
    }

    @Override
    public void setFuelTime(int time) {
        saveBurnTime = time;
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
                    nbt.put(INVENTORY, inv.serializeNBT());
                    if (level.isClientSide) IMPacks.SERVER_PACK.getSender().pos(pos).tag(nbt).send(NETList.SYNC_SLOT_SET);
                    getTimes()[i] = 0;
                    setChanged();
                }
            } else {
                getTimes()[i] = 0;
                getRecipeTimes()[i] = 0;
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
                recipe.getIngredients().get(0).test(stack) && state.getValue(ILitBlock.LIT)).findFirst();
    }

}
