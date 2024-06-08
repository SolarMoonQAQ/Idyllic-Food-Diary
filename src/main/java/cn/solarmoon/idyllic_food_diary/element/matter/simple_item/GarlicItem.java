package cn.solarmoon.idyllic_food_diary.element.matter.simple_item;

import cn.solarmoon.idyllic_food_diary.feature.logic.spit_item.SpitUtil;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
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
        SpitUtil.spit(IMItems.GARLIC_CLOVE.get(), 5 - player.getRandom().nextInt(3), player);
        level.playSound(null, player.getOnPos().above(), SoundEvents.AXE_STRIP, SoundSource.PLAYERS, 1F, 2F);
        if (!player.isCreative()) heldItem.shrink(1);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

}
