package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.ability.CustomPlaceableItem;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

    private ItemStack container = ItemStack.EMPTY;

    public FoodBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.FOOD.get(), pos, state);
    }

    public boolean hasContainer() {
        return !container.isEmpty();
    }

    public ItemStack getContainer() {
        return container;
    }

    public void setContainer(ItemStack container) {
        this.container = container;
    }

    public BlockState getContainerLeft() {
        Block customPlace = CustomPlaceableItem.get().get(container.getItem());
        return customPlace != null ? customPlace.defaultBlockState() : Block.byItem(container.getItem()).defaultBlockState();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(NBTList.FOOD_CONTAINER, container.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        container.deserializeNBT(tag.getCompound(NBTList.FOOD_CONTAINER));
    }

}
