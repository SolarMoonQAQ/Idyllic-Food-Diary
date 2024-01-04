package cn.solarmoon.immersive_delight.compat.farmersdelight.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.FoodValues;

import static cn.solarmoon.immersive_delight.common.RegisterBlocks.WHEAT_DOUGH;

/**
 * 面团（农夫乐事联动）
 */
public class WheatDoughItem extends BlockItem {
    public WheatDoughItem() {
        super(WHEAT_DOUGH.get(), new Item.Properties().food(FoodValues.WHEAT_DOUGH));
    }
}
