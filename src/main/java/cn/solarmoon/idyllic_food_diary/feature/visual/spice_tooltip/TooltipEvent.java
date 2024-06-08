package cn.solarmoon.idyllic_food_diary.feature.visual.spice_tooltip;

import cn.solarmoon.idyllic_food_diary.feature.logic.spice.SpicesCap;
import cn.solarmoon.idyllic_food_diary.registry.common.IMCapabilities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMCommonConfig;
import cn.solarmoon.idyllic_food_diary.util.ContainerHelper;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TooltipEvent {

    @SubscribeEvent
    public void onShownTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        SpicesCap spiceData = CapabilityUtil.getData(stack, IMCapabilities.FOOD_ITEM_DATA).getSpicesData();
        for (var spice : spiceData.getSpices()) {
            event.getToolTip().add(spice.getDisplayName().copy().append("x" + spice.getAmount()).append(spice.getStep().name()));
        }
        if (IMCommonConfig.deBug.get() && !ContainerHelper.getContainer(stack).isEmpty()) {
            event.getToolTip().add(Component.translatable(ContainerHelper.getContainer(stack).getDescriptionId()));
        }
    }

}
