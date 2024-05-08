package cn.solarmoon.idyllic_food_diary.core.common.item.product;

import cn.solarmoon.idyllic_food_diary.api.util.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.api.common.item.simple.SimpleItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GarlicItem extends SimpleItem {

    /**
     * 剥蒜
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        super.use(level, player, hand);
        ItemStack heldItem = player.getItemInHand(hand);
        FarmerUtil.spit(IMItems.GARLIC_CLOVE.get(), 5 - player.getRandom().nextInt(3), player);
        level.playSound(null, player.getOnPos().above(), SoundEvents.AXE_STRIP, SoundSource.PLAYERS, 1F, 2F);
        if (!player.isCreative()) heldItem.shrink(1);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

}
