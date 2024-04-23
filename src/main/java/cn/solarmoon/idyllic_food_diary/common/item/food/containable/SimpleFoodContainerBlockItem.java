package cn.solarmoon.idyllic_food_diary.common.item.food.containable;

import cn.solarmoon.solarmoon_core.api.common.item.SimpleFoodBlockItem;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class SimpleFoodContainerBlockItem extends SimpleFoodBlockItem {

    private final ItemLike itemFinishReturn;

    public SimpleFoodContainerBlockItem(ItemLike itemFinishReturn, Block block, FoodProperties foodProperties) {
        super(block, foodProperties);
        this.itemFinishReturn = itemFinishReturn;
    }

    public SimpleFoodContainerBlockItem(ItemLike itemFinishReturn, Block block, int nutrition, float saturation) {
        super(block, nutrition, saturation);
        this.itemFinishReturn = itemFinishReturn;
    }

    public SimpleFoodContainerBlockItem(ItemLike itemFinishReturn, Block block, int nutrition, float saturation, Supplier<MobEffectInstance> effectInstanceSupplier, float chance) {
        super(block, nutrition, saturation, effectInstanceSupplier, chance);
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
