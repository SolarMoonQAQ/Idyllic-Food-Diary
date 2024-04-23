package cn.solarmoon.idyllic_food_diary.common.item.food;

import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.idyllic_food_diary.util.useful_data.FoodProperty;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

/**
 * 面团item（农夫乐事联动）
 */
public class WheatDoughItem extends BlockItem {

    public WheatDoughItem() {
        super(IMBlocks.WHEAT_DOUGH.get(), new Item.Properties().food(FoodProperty.PRIMARY_HUNGER_PRODUCT));
    }

}
