package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.common.RegisterBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.FoodValues;

/**
 * 面饼
 */
public class FlatbreadDoughItem extends BlockItem {
    public FlatbreadDoughItem() {
        super(RegisterBlocks.FLATBREAD_DOUGH.get(), new Item.Properties().food(FoodValues.WHEAT_DOUGH));
    }
}
