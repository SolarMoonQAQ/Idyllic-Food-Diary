package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.evaporation.IEvaporationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.steaming.ISteamingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.DropUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SteamerBlockEntity extends SyncedBlockEntity implements ISteamingRecipe {

    public static final String STACK = "Stack";
    public static final String LID = "Lid";

    private int stack;
    private final SteamerInventoryList invList;
    private ItemStack lid;
    private int[] times;
    private int[] recipeTimes;

    public SteamerBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.STEAMER.get(), pos, state);
        invList = new SteamerInventoryList(this);
        stack = 1;
        lid = ItemStack.EMPTY;
        this.times = new int[64];
        this.recipeTimes = new int[64];
    }

    /**
     * 增加蒸笼（如果增加的量没有超出最大蒸笼堆栈，则直接叠加，反之不进行操作）
     * @return 增长成功返回true
     */
    public boolean grow(ItemStack steamerItemStack) {
        if (steamerItemStack.getItem() instanceof SteamerItem) {
            int itemStackAmount = SteamerItem.getStackAmount(steamerItemStack);
            if (itemStackAmount > 0 && stack + itemStackAmount <= getMaxStack()) {
                pushInv(SteamerItem.getInvFromItem(steamerItemStack));
                pushLid(steamerItemStack);
                setChanged();
                return true;
            } else if (itemStackAmount == 0 && stack < getMaxStack()) {
                pushInv(newInventory());
                pushLid(steamerItemStack);
                setChanged();
                return true;
            }
        }
        return false;
    }

    /**
     * 削减蒸笼
     * @return 削减的物品栏的复制
     */
    public Optional<SteamerBlockEntity> shrink(@Nullable ItemStack steamerItem) {
        if (level == null) return Optional.empty();
        // 如果item给到null，直接强行削减
        // 如果item不为null，但是是empty，也能削减
        // item真实存在，则需要检测是否有效（是否为蒸笼且层数足够添加一层）
        int mainStack = SteamerItem.getStackAmount(steamerItem);
        boolean itemValid = steamerItem != null && steamerItem.getItem() instanceof SteamerItem && mainStack < getMaxStack();
        if (steamerItem != null && !steamerItem.isEmpty() && !itemValid) return Optional.empty();
        if (stack > 0) {
            int index = stack - 1;
            SteamerInventory inv = invList.get(index);
            setStack(index);

            SteamerBlockEntity copy = copy();
            copy.resetToEmpty();
            copy.setLevel(level);
            if (steamerItem != null) copy.grow(steamerItem); // 先增加蒸笼物品层作为基础
            copy.pushInv(inv); // 然后在此基础上加入削减的那层

            // 削减到0的话就删除方块
            if (stack == 0 && getLevel() != null) {
                getLevel().removeBlock(getBlockPos(), true);
                setChanged();
                level.playSound(null, getBlockPos(), SoundEvents.BAMBOO_WOOD_HIT, SoundSource.BLOCKS);
            } else {
                setLid(ItemStack.EMPTY); // 每次削减总是从最上层开始，而最上层总是可能有lid，故而同时削减lid
                invList.remove(index);
                setChanged();
            }

            return Optional.of(copy);
        }
        return Optional.empty();
    }

    public void pushLid(ItemStack steamerItemStack) {
        ItemStack lid = SteamerItem.getLid(steamerItemStack);
        if (!lid.isEmpty()) {
            if (level != null && hasLid()) DropUtil.summonDrop(getLid().copy(), level, getBlockPos().getCenter()); // 如果都有盖子先把方块的盖子丢出来
            setLid(lid); // 然后再设置盖子为物品所带的盖子
        }
    }

    public void resetToEmpty() {
        setStack(0);
        invList.clear();
    }

    private void setStack(int stack) {
        this.stack = stack;
    }

    public SteamerBlockEntity copy() {
        SteamerBlockEntity steamer = new SteamerBlockEntity(getBlockPos(), getBlockState());
        steamer.deserializeNBT(serializeNBT());
        return steamer;
    }

    public void pushInv(ItemStackHandler inv) {
        SteamerInventory s = newInventory();
        s.deserializeNBT(inv.serializeNBT());
        invList.add(s);
        setStack(invList.size());
    }

    public void pushInv(List<ItemStackHandler> invList) {
        invList.forEach(this::pushInv);
    }

    public boolean returnLid(Player player) {
        if (hasLid()) {
            if (!player.isCreative()) DropUtil.addItemToInventory(player, lid);
            setLid(ItemStack.EMPTY);
            setChanged();
            return true;
        }
        return false;
    }

    public boolean hasLid() {
        return !lid.isEmpty();
    }

    /**
     * @param lid 只能设置Lid类型或空物品
     */
    public void setLid(ItemStack lid) {
        if (lid.getItem() instanceof SteamerLidItem || lid.isEmpty()) {
            this.lid = lid;
            setChanged();
        }
    }

    public int getStack() {
        return stack;
    }

    public ItemStack getLid() {
        return lid;
    }

    public int getMaxStack() {
        return 4;
    }

    public SteamerInventory newInventory() {
        return new SteamerInventory(4, this);
    }

    public SteamerInventoryList getInvList() {
        return invList;
    }

    /**
     * @return 获取连接底部的基座
     */
    @Nullable
    public IEvaporationRecipe getBase() {
        if (getLevel() == null) return null;
        BlockEntity blockEntity = getLevel().getBlockEntity(getConnectedBelowPos());
        if (blockEntity instanceof IEvaporationRecipe base) {
            return base;
        }
        return null;
    }

    /**
     * @return 获取连接顶部的蒸笼
     */
    @Nullable
    public SteamerBlockEntity getTop() {
        if (getLevel() == null) return null;
        BlockEntity blockEntity = getLevel().getBlockEntity(getConnectedTopPos());
        if (blockEntity instanceof SteamerBlockEntity top) {
            return top;
        }
        return null;
    }

    /**
     * 获取连接的蒸笼的最下方的第一个非蒸笼（或一层蒸笼）方块坐标<br/>
     * 要求是上方为蒸笼且stack为最大堆叠量才继续检测<br/>
     * 也就是直到检测到非蒸笼或stack不满的蒸笼为止
     */
    public BlockPos getConnectedBelowPos() {
        if (getLevel() == null) return null;
        Level level = getLevel();
        BlockPos posBelow = getBlockPos().below(); // 不用检测自己直接从下一格开始
        BlockEntity be = level.getBlockEntity(posBelow);
        // 当前坐标是蒸笼，并且栈最大，检测坐标就下移一格
        while (be instanceof SteamerBlockEntity s && s.getStack() == getMaxStack()) {
            posBelow = posBelow.below();
            be = level.getBlockEntity(posBelow); // 重设防止无限循环
        }
        // 当前坐标不是蒸笼，或者蒸笼层数不满就返回
        return posBelow;
    }

    /**
     * 获取连接的蒸笼的最上方的第一个非蒸笼（或一层蒸笼）方块坐标<br/>
     * 要求是上方为蒸笼且stack为最大堆叠量才继续检测<br/>
     * 也就是直到检测到非蒸笼或stack不满的蒸笼为止
     */
    public BlockPos getConnectedTopPos() {
        if (getLevel() == null) return null;
        Level level = getLevel();
        BlockPos posAbove = getBlockPos();
        BlockEntity be = level.getBlockEntity(posAbove);
        // 当前坐标是蒸笼，并且栈最大，检测坐标就上移一格
        while (be instanceof SteamerBlockEntity s && s.getStack() == getMaxStack()) {
            posAbove = posAbove.above();
            be = level.getBlockEntity(posAbove); // 重设防止无限循环
        }
        // 循环结束，如果当前坐标不是蒸笼，那么实际坐标就在之前一格
        // 如果是，就返回当前坐标
        return be instanceof SteamerBlockEntity ? posAbove : posAbove.below();
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

    /**
     * @return 是否具有可工作的条件，也就是连接底部是否有一个正在消耗热水的基座，且顶部有盖子
     */
    public boolean canWork() {
        if (getBase() == null) return false;
        if (getTop() == null) return false;
        return getBase().isValidForSteamer() && getTop().hasLid();
    }

    @Override
    public int[] getSteamTimes() {
        return times;
    }

    @Override
    public int[] getSteamRecipeTimes() {
        return recipeTimes;
    }

    @Override
    public void setSteamTimes(int[] var1) {
        times = var1;
    }

    @Override
    public void setSteamRecipeTimes(int[] var1) {
        recipeTimes = var1;
    }

    /**
     * 检测是否在连接状态（下方是否有stack为最大的蒸笼）<br/>
     * 逻辑是找连接底端坐标，如果坐标和当前坐标的下方一致，那就说明下方没有蒸笼，上方同理
     */
    public boolean isConnected() {
        return !getConnectedBelowPos().equals(getBlockPos().below())
                || !getConnectedTopPos().equals(getBlockPos());
    }

    /**
     * 不让在连接中段放盖子
     */
    public void tryPreventLidSet() {
        BlockPos pos = getBlockPos();
        if (isConnected() && hasLid() && !isTop()) {
            if (level != null) {
                DropUtil.summonDrop(getLid().copy(), level, pos.getCenter());
            }
            setLid(ItemStack.EMPTY);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(STACK, stack);
        tag.put(LID, lid.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        stack = tag.getInt(STACK);
        lid = ItemStack.of(tag.getCompound(LID));
    }

}
