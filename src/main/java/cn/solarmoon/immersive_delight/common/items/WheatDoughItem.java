package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.common.IMBlocks;
import cn.solarmoon.immersive_delight.util.Util;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.FoodValues;

/**
 * 面团item（农夫乐事联动）
 */
public class WheatDoughItem extends BlockItem {

    /**
     * 其实就是农夫乐事的面团食物属性，只不过看农夫乐事加不加载来选择是否用农夫乐事的词条
     */
    public static final FoodProperties foodProperties = Util.isLoad(FarmersDelight.MODID)
            ? FoodValues.WHEAT_DOUGH
            : (new FoodProperties.Builder()).nutrition(2).saturationMod(0.3F).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).build();

    public WheatDoughItem() {
        super(IMBlocks.WHEAT_DOUGH.get(), new Item.Properties().food(foodProperties));
    }

}
