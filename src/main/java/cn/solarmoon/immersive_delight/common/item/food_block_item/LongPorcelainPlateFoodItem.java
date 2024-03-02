package cn.solarmoon.immersive_delight.common.item.food_block_item;

import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.common.item.SimpleFoodBlockItem;
import cn.solarmoon.solarmoon_core.util.LevelSummonUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class LongPorcelainPlateFoodItem extends SimpleFoodBlockItem {

    public LongPorcelainPlateFoodItem(Block block, FoodProperties foodProperties) {
        super(block, foodProperties);
    }

    public LongPorcelainPlateFoodItem(Block block, int nutrition, float saturation) {
        super(block, nutrition, saturation);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if (living instanceof Player player && !player.isCreative()) {
            LevelSummonUtil.addItemToInventory(player, new ItemStack(IMItems.LONG_PORCELAIN_PLATE.get()));
        }
        return super.finishUsingItem(stack, level, living);
    }
}
