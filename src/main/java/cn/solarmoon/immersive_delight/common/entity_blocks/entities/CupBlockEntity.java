package cn.solarmoon.immersive_delight.common.entity_blocks.entities;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.entities.TankBlockEntity;
import cn.solarmoon.immersive_delight.init.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 除去tank外，还能存储一个物品
 */
public class CupBlockEntity extends TankBlockEntity {

    public ItemStack itemStack;

    public CupBlockEntity(BlockPos pos, BlockState state) {
        super(IMEntityBlocks.CELADON_CUP_ENTITY.get(), pos, state);
        this.itemStack = ItemStack.EMPTY;
    }

    @Override
    public int getTankMaxCapacity() {
        return Config.maxCeladonCupCapacity.get();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Item", itemStack.save(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        setItem(ItemStack.of(tag.getCompound("Item")));
    }

    /**
     * 设置容纳的物品（仅1个）
     * 当然包括nbt数据
     */
    public void setItem(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        this.itemStack = new ItemStack(itemStack.getItem(), 1);
        this.itemStack.setTag(tag);
        setChanged();
    }

}
