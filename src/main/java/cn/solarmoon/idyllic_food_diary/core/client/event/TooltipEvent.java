package cn.solarmoon.idyllic_food_diary.core.client.event;

import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.Spice;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.SpicesData;
import cn.solarmoon.idyllic_food_diary.api.util.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMCapabilities;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMCommonConfig;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class TooltipEvent {

    @SubscribeEvent
    public void onShownTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        SpicesData spiceData = CapabilityUtil.getData(stack, IMCapabilities.FOOD_ITEM_DATA).getSpicesData();
        for (var spice : spiceData.getSpices()) {
            event.getToolTip().add(spice.getDisplayName().copy().append("x" + spice.getAmount()).append(spice.getStep().name()));
        }
        if (IMCommonConfig.deBug.get() && !FarmerUtil.getContainer(stack).isEmpty()) {
            event.getToolTip().add(Component.translatable(FarmerUtil.getContainer(stack).getDescriptionId()));
        }
    }

}
