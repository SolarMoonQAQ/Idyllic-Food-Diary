package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

/**
 * 面团item（农夫乐事联动）
 */
public class WheatDoughItem extends BlockItem {

    public static final FoodProperties foodProperties = new FoodProperties.Builder()
            .nutrition(2).saturationMod(0.3F)
            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).build();

    public WheatDoughItem() {
        super(IMBlocks.WHEAT_DOUGH.get(), new Item.Properties().food(foodProperties));
    }

}
