package cn.solarmoon.immersive_delight.common.item.food_block_item;

import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.util.LevelSummonUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * 碗装食物，能把自身放下来，一般和方块的碗装食物类绑定
 */
public class BowlFoodItem extends BlockItem {

    public BowlFoodItem(Block block, FoodProperties foodProperties) {
        super(block, new Properties()
                .food(foodProperties)
                .stacksTo(16)
                .craftRemainder(Items.BOWL));
    }

    public BowlFoodItem(Block block, int nutrition, float saturation) {
        super(block, new Properties().food(
                new FoodProperties.Builder()
                        .nutrition(nutrition).saturationMod(saturation).build()
        ).stacksTo(16));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if (living instanceof Player player) {
            LevelSummonUtil.addItemToInventory(player, new ItemStack(Items.BOWL));
        }
        return super.finishUsingItem(stack, level, living);
    }

}
