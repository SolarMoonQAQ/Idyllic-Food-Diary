package cn.solarmoon.idyllic_food_diary.element.matter.cookware.stove;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.common.item.IContainerItem;
import cn.solarmoon.solarmoon_core.api.common.item.ITankItem;
import net.minecraft.world.item.BlockItem;

public class StoveItem extends BlockItem implements ITankItem, IContainerItem {

    public StoveItem() {
        super(IMBlocks.STOVE.get(), new Properties().stacksTo(1));
    }

    @Override
    public int getMaxCapacity() {
        return 1000;
    }

}
