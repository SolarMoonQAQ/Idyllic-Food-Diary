package cn.solarmoon.idyllic_food_diary.element.matter.stove;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class StoveItem extends BlockItem {
    public StoveItem() {
        super(IMBlocks.STOVE.get(), new Properties());
    }
}
