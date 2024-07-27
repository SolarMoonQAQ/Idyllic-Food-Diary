package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import net.minecraft.world.item.ItemStack;

/**
 * 水壶item
 */
public class KettleItem extends AbstractKettleItem {

    public KettleItem() {
        super(IMBlocks.KETTLE.get(), new Properties());
    }

    @Override
    public int getMaxCapacity() {
        return 1000;
    }

}
