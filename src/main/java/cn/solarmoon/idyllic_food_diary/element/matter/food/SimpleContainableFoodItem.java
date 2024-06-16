package cn.solarmoon.idyllic_food_diary.element.matter.food;

import cn.solarmoon.solarmoon_core.api.item_base.SimpleFoodItem;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

/**
 * 带容器的食物，会返还容器
 */
public class SimpleContainableFoodItem extends SimpleFoodItem {

    private final ItemLike itemFinishReturn;

    public SimpleContainableFoodItem(ItemLike itemFinishReturn, FoodProperties foodProperties) {
        super(foodProperties);
        this.itemFinishReturn = itemFinishReturn;
    }

    public SimpleContainableFoodItem(ItemLike itemFinishReturn, int nutrition, float saturation) {
        super(nutrition, saturation);
        this.itemFinishReturn = itemFinishReturn;
    }

    public SimpleContainableFoodItem(ItemLike itemFinishReturn, int nutrition, float saturation, Supplier<MobEffectInstance> effectInstanceSupplier, float chance) {
        super(nutrition, saturation, effectInstanceSupplier, chance);
        this.itemFinishReturn = itemFinishReturn;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        living.eat(level, stack);
        if (living instanceof Player player && !player.isCreative()) {
            LevelSummonUtil.addItemToInventory(player, new ItemStack(itemFinishReturn));
        }
        return stack;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return new ItemStack(itemFinishReturn);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 16;
    }

}
