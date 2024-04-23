package cn.solarmoon.idyllic_food_diary.common.block_entity.base;

import cn.solarmoon.idyllic_food_diary.common.block_entity.inventoryHandler.SteamerInventory;
import cn.solarmoon.idyllic_food_diary.common.recipe.SteamerRecipe;
import cn.solarmoon.idyllic_food_diary.common.registry.IMItems;
import cn.solarmoon.idyllic_food_diary.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.api.common.block.IStackBlock;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IIndividualTimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static cn.solarmoon.idyllic_food_diary.common.block.base.entity_block.AbstractSteamerEntityBlock.HAS_LID;
import static cn.solarmoon.solarmoon_core.api.common.block.IStackBlock.STACK;

public abstract class AbstractSteamerBlockEntity extends BaseContainerBlockEntity implements IIndividualTimeRecipeBlockEntity<SteamerRecipe> {

    private final ItemStackHandler inventory;
    private int[] times;
    private int[] recipeTimes;

    public AbstractSteamerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, 8, 1, pos, state);
        this.inventory = new SteamerInventory();
        this.times = new int[64];
        this.recipeTimes = new int[64];
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

    /**
     * @return 获取连接底部的基座
     */
    @Nullable
    public AbstractSteamerBaseBlockEntity getBase() {
        if (getLevel() == null) return null;
        BlockEntity blockEntity = getLevel().getBlockEntity(getConnectedBelowPos());
        if (blockEntity instanceof AbstractSteamerBaseBlockEntity base) {
            return base;
        }
        return null;
    }

    /**
     * @return 获取连接顶部的蒸笼
     */
    @Nullable
    public AbstractSteamerBlockEntity getTop() {
        if (getLevel() == null) return null;
        BlockEntity blockEntity = getLevel().getBlockEntity(getConnectedTopPos());
        if (blockEntity instanceof AbstractSteamerBlockEntity top) {
            return top;
        }
        return null;
    }

    /**
     * 获取连接的蒸笼的最下方的第一个非蒸笼（或一层蒸笼）方块坐标<br/>
     * 要求是下方为蒸笼且stack为2才继续检测<br/>
     * 也就是直到检测到非蒸笼或stack为1的蒸笼为止
     */
    public BlockPos getConnectedBelowPos() {
        if (getLevel() == null) return null;
        Level level = getLevel();
        BlockPos posBelow = getBlockPos().below();
        BlockState state = level.getBlockState(posBelow);
        while (state.is(getBlockState().getBlock()) && state.getValue(IStackBlock.STACK) == 2) {
            posBelow = posBelow.below();
            state = level.getBlockState(posBelow); //这里得重设以防无限循环
        }
        return posBelow;
    }

    /**
     * 获取连接的蒸笼的最上方的第一个非蒸笼（或一层蒸笼）方块坐标<br/>
     * 要求是上方为蒸笼且stack为2才继续检测<br/>
     * 也就是直到检测到非蒸笼或stack为1的蒸笼为止
     */
    public BlockPos getConnectedTopPos() {
        if (getLevel() == null) return null;
        Level level = getLevel();
        BlockPos posAbove = getBlockPos();
        BlockState state = level.getBlockState(posAbove);
        while (state.is(getBlockState().getBlock()) && state.getValue(IStackBlock.STACK) == 2
                && level.getBlockState(posAbove.above()).is(getBlockState().getBlock())) //这里多加个上层判断以防stack1和2坐标不一致
        {
            posAbove = posAbove.above();
            state = level.getBlockState(posAbove); //这里得重设以防无限循环
        }
        return posAbove;
    }

    /**
     * 获取蒸笼的两个物品栏，前四个为inv1，后四个为inv2
     */
    public SteamerInventory getInv(int index) {
        if (index == 1) {
            SteamerInventory inv1 = new SteamerInventory();
            for (int i = 0; i < 4; i++) {
                inv1.setStackInSlot(i, getInventory().getStackInSlot(i));
            }
            return inv1;
        } else if (index == 2) {
            SteamerInventory inv2 = new SteamerInventory();
            for (int i = 4; i < 8; i++) {
                inv2.setStackInSlot(i - 4, getInventory().getStackInSlot(i)); //这里最好不要让序列和实际一致，判别比较麻烦
            }
            return inv2;
        }
        return null;
    }

    /**
     * 获取分割一半物品栏的蒸笼物品，前四个合一起为1，后四个合一起为2<br/>
     * 如果有盖子就给盖子的nbt
     */
    public ItemStack getItem(int index) {
        if (index == 1) {
            SteamerInventory inv1 = getInv(1);
            ItemStack drop1 = new ItemStack(getBlockState().getBlock());
            ContainerUtil.setInventory(drop1, inv1);
            drop1.getOrCreateTag().putBoolean(NBTList.HAS_LID, getBlockState().getValue(HAS_LID));
            return drop1;
        } else if (index == 2) {
            SteamerInventory inv2 = getInv(2);
            ItemStack drop2 = new ItemStack(getBlockState().getBlock());
            ContainerUtil.setInventory(drop2, inv2);
            drop2.getOrCreateTag().putBoolean(NBTList.HAS_LID, getBlockState().getValue(HAS_LID));
            return drop2;
        }
        return ItemStack.EMPTY;
    }

    /**
     * 清空某个槽位的内容
     */
    public void clearInv(int index) {
        if (index == 1) {
            for (int i = 0; i < 4; i++) {
                getInventory().extractItem(i, 64, false);
            }
        }
        else if (index == 2) {
            for (int i = 4; i < 8; i++) {
                getInventory().extractItem(i, 64, false);
            }
        }
    }

    /**
     * 设置后四个物品槽的开启与否
     */
    public void set2ndInv(boolean value) {
        SteamerInventory inv = (SteamerInventory) getInventory();
        inv.secondInv = value;
    }

    /**
     * 检测是否在连接状态（下方是否有stack为2的蒸笼）<br/>
     * 逻辑是找连接底端坐标，如果坐标和当前坐标的下方一致，那就说明下方没有蒸笼，上方同理
     */
    public boolean isConnected() {
        return !getConnectedBelowPos().equals(getBlockPos().below())
                || !getConnectedTopPos().equals(getBlockPos());
    }

    public boolean isDouble() {
        return getBlockState().getValue(STACK) == 2;
    }

    public boolean isTop() {
        return getBlockPos().equals(getConnectedTopPos());
    }

    public boolean isBottom() {
        return getBlockPos().below().equals(getConnectedBelowPos());
    }

    public boolean isMiddle() {
        return !isTop() && !isBottom();
    }

    public boolean hasLid() {
        return getBlockState().getValue(HAS_LID);
    }

    /**
     * @return 是否具有可工作的条件，也就是连接底部是否有一个正在消耗热水的基座，且顶部有盖子
     */
    public boolean canWork() {
        if (getBase() == null) return false;
        if (getTop() == null) return false;
        return getBase().isWorking() && getTop().hasLid();
    }

    /**
     * 配方匹配，连接底部有正在工作的基座即通过
     */
    @Override
    public Optional<SteamerRecipe> getCheckedRecipe(int index) {
        Level level = getLevel();
        if (level == null) return Optional.empty();
        List<SteamerRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.STEAMER.get());
        ItemStack stack = getInventory().getStackInSlot(index);
        return recipes.stream().filter(recipe -> recipe.input().test(stack) && canWork()).findFirst();
    }

    public void tryCook() {
        for (int i = 0; i < getInventory().getSlots(); i++) {
            Optional<SteamerRecipe> recipeOp = getCheckedRecipe(i);
            if (recipeOp.isPresent()) {
                SteamerRecipe recipe = recipeOp.get();
                getRecipeTimes()[i] = recipe.time();
                getTimes()[i] = getTimes()[i] + 1;
                if (getTimes()[i] >= recipe.time()) {
                    getInventory().setStackInSlot(i, recipe.output());
                    getTimes()[i] = 0;
                    getRecipeTimes()[i] = 0;
                    setChanged();
                }
            } else {
                getTimes()[i] = 0;
                getRecipeTimes()[i] = 0;
            }
        }
    }

    /**
     * 不让在连接中段放盖子
     */
    public void tryPreventLidSet() {
        BlockPos pos = getBlockPos();
        BlockState state = getBlockState();
        if (isConnected() && hasLid() && !isTop()) {
            if (level != null) {
                level.setBlock(pos, state.setValue(HAS_LID, false), 3);
            }
            LevelSummonUtil.summonDrop(IMItems.STEAMER_LID.get(), level, pos, 1);
        }
    }

    /**
     * 当stack为2时开放容量
     */
    public void tryOpen2ndInv() {
        BlockState state = getBlockState();
        if (state.getValue(STACK) == 1) {
            set2ndInv(false);
        } else if (state.getValue(STACK) == 2) {
            set2ndInv(true);
        }
    }

}
