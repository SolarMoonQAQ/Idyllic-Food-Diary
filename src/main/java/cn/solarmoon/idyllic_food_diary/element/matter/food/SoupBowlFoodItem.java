package cn.solarmoon.idyllic_food_diary.element.matter.food;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * 碗装食物类，可以绑定data的液体效果，也能放置对应方块<br/>
 * 默认16格堆叠<br/>
 * 需要自己给食物属性！
 */
public class SoupBowlFoodItem extends SimpleContainableFoodBlockItem {

    public SoupBowlFoodItem(Block block, FoodProperties foodProperties) {
        super(Items.BOWL, block, foodProperties);
    }

    public SoupBowlFoodItem(Block block, int nutrition, float saturation) {
        super(Items.BOWL, block, nutrition, saturation);
    }

    public SoupBowlFoodItem(Block block, int nutrition, float saturation, Supplier<MobEffectInstance> effectInstanceSupplier, float chance) {
        super(Items.BOWL, block, nutrition, saturation, effectInstanceSupplier, chance);
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

}
