package cn.solarmoon.immersive_delight.common.item.block_item;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.immersive_delight.util.FarmerUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GarlicItem extends BlockItem {

    public GarlicItem() {
        super(IMBlocks.GARLIC.get(), new Properties());
    }

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
