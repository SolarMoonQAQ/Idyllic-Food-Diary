package cn.solarmoon.immersive_delight.common.items;


import cn.solarmoon.immersive_delight.common.items.abstract_items.DrinkableItem;
import cn.solarmoon.immersive_delight.init.Config;
import net.minecraft.world.item.Item;

import static cn.solarmoon.immersive_delight.common.RegisterBlocks.CUP;

/**
 * 小容量的杯子
 * 默认容量250，只能堆叠一个
 */
public class CupItem extends DrinkableItem {

    public CupItem() {
        super(CUP.get(), new Item.Properties().stacksTo(1));
    }

    @Override
    public int getMaxVolume() {
        return Config.maxCupVolume.get();
    }

}
