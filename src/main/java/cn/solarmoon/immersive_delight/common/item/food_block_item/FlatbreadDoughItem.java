package cn.solarmoon.immersive_delight.common.item.food_block_item;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.immersive_delight.compat.farmersdelight.FarmersDelight;
import cn.solarmoon.immersive_delight.util.useful_data.FoodProperty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.FoodValues;

/**
 * 面饼item
 */
public class FlatbreadDoughItem extends BlockItem {

    public FlatbreadDoughItem() {
        super(IMBlocks.FLATBREAD_DOUGH.get(), new Item.Properties().food(FoodProperty.PRIMARY_HUNGER_PRODUCT));
    }

}
