package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.Spice;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.SpiceList;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.ability.CustomPlaceableItem;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 所有foodBlock的基本方块实体类，目的是给食物一个容器的动态渲染
 */
public class FoodBlockEntity extends BlockEntity {

    private ItemStack container;
    private final SpiceList spices;

    public FoodBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.FOOD.get(), pos, state);
        container = ItemStack.EMPTY;
        spices = new SpiceList();
    }

    public boolean hasContainer() {
        return !container.isEmpty();
    }

    public ItemStack getContainer() {
        return container;
    }

    public void setContainer(ItemStack container) {
        this.container = container;
        setChanged();
    }

    public SpiceList getSpices() {
        return spices;
    }

    public void setSpices(SpiceList spices) {
        this.spices.clear();
        this.spices.addAll(spices);
        setChanged();
    }

    public BlockState getContainerLeft() {
        Block customPlace = CustomPlaceableItem.get().get(container.getItem());
        BlockState origin = customPlace != null ? customPlace.defaultBlockState() : Block.byItem(container.getItem()).defaultBlockState();
        origin = BlockUtil.inheritBlockWithAllState(getBlockState(), origin);
        return origin;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(NBTList.FOOD_CONTAINER, container.save(new CompoundTag()));
        tag.put(NBTList.SPICES, spices.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        container = ItemStack.of(tag.getCompound(NBTList.FOOD_CONTAINER));
        spices.deserializeNBT(tag.getList(NBTList.SPICES, ListTag.TAG_COMPOUND));
    }

}
