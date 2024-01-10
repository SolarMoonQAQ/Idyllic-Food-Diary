package cn.solarmoon.immersive_delight.common.items;


import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.common.IMItems;
import cn.solarmoon.immersive_delight.common.items.abstract_items.CupItem;
import cn.solarmoon.immersive_delight.init.Config;
import net.minecraft.world.item.Item;

/**
 * 青瓷杯
 * 小容量的杯子item
 * 默认容量250，只能堆叠一个
 */
public class CeladonCupItem extends CupItem {

    public CeladonCupItem() {
        super(IMEntityBlocks.CELADON_CUP.get(), new Item.Properties()

        );
    }

    @Override
    public int getMaxCapacity() {
        return Config.maxCeladonCupCapacity.get();
    }

}
