package cn.solarmoon.immersive_delight.common.item.product;

import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.immersive_delight.util.FarmerUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * 榴莲肉
 */
public class DurianFleshItem extends Item {

    public DurianFleshItem() {
        super(new Properties()
                .food(new FoodProperties.Builder()
                .nutrition(4).saturationMod(0.5f).build()));
    }


    /**
     * 吃完吐子
     */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            FarmerUtil.spit(IMItems.DURIAN_CORE.get(), 1, player);
        }
        return super.finishUsingItem(stack, level, entity);
    }

}
