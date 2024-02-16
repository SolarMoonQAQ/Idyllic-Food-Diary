package cn.solarmoon.immersive_delight.common.item.food_block_item;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.immersive_delight.compat.farmersdelight.FarmersDelight;
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

    /**
     * 其实就是农夫乐事的面团食物属性，只不过看农夫乐事加不加载来选择是否用农夫乐事的词条
     */
    public static final FoodProperties foodProperties = new FarmersDelight().isLoaded()
            ? FoodValues.WHEAT_DOUGH
            : (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).build();

    public FlatbreadDoughItem() {
        super(IMBlocks.FLATBREAD_DOUGH.get(), new Item.Properties().food(foodProperties));
    }

}
