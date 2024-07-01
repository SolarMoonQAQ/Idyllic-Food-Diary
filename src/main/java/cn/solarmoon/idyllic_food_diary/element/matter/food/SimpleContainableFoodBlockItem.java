package cn.solarmoon.idyllic_food_diary.element.matter.food;

import cn.solarmoon.idyllic_food_diary.util.ContainerHelper;
import cn.solarmoon.solarmoon_core.api.item_base.SimpleFoodBlockItem;
import cn.solarmoon.solarmoon_core.api.util.DropUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class SimpleContainableFoodBlockItem extends SimpleFoodBlockItem {

    public SimpleContainableFoodBlockItem(Block block, FoodProperties foodProperties) {
        super(block, foodProperties);
    }

    public SimpleContainableFoodBlockItem(Block block, int nutrition, float saturation, Supplier<MobEffectInstance> effectInstanceSupplier, float chance) {
        super(block, nutrition, saturation, effectInstanceSupplier, chance);
    }

    public SimpleContainableFoodBlockItem(Block block, int nutrition, float saturation) {
        super(block, nutrition, saturation);
    }

    public SimpleContainableFoodBlockItem(Block block, int stacksTo, int nutrition, float saturation) {
        super(block, stacksTo, nutrition, saturation);
    }

    public SimpleContainableFoodBlockItem(Block block, int stacksTo, FoodProperties foodProperties) {
        super(block, stacksTo, foodProperties);
    }

    public SimpleContainableFoodBlockItem(Block block, int stacksTo, int nutrition, float saturation, Supplier<MobEffectInstance> effectInstanceSupplier, float chance) {
        super(block, stacksTo, nutrition, saturation, effectInstanceSupplier, chance);
    }

    public ItemStack getContainer(ItemStack stack) {
        return ContainerHelper.getContainer(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        living.eat(level, stack);
        if (living instanceof Player player && !player.isCreative()) {
            DropUtil.addItemToInventory(player, getContainer(stack));
        }
        return stack;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return getContainer(itemStack);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 16;
    }

}
