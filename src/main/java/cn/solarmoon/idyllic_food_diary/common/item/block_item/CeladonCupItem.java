package cn.solarmoon.idyllic_food_diary.common.item.block_item;


import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.idyllic_food_diary.common.item.base.AbstractCupItem;
import net.minecraft.world.item.Item;

/**
 * 青瓷杯
 * 小容量的杯子item
 * 默认容量250，只能堆叠一个
 */
public class CeladonCupItem extends AbstractCupItem {

    public CeladonCupItem() {
        super(IMBlocks.CELADON_CUP.get(),new Item.Properties());
    }

    @Override
    public int getMaxCapacity() {
        return 250;
    }

}
