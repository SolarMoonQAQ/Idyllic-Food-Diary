package cn.solarmoon.idyllic_food_diary.feature.trade;

import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AddSpiceTradeEvent {

    @SubscribeEvent
    public void add(WandererTradesEvent event) {
        event.getGenericTrades().add(new ItemsForEmeralds(new ItemStack(IMItems.STEAMER.get(), 5), 3, 10, 16, 2));
    }

}
