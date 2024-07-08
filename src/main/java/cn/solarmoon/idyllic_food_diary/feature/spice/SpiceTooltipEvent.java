package cn.solarmoon.idyllic_food_diary.feature.spice;

import cn.solarmoon.idyllic_food_diary.registry.common.IMCapabilities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMCommonConfig;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.ContainerHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SpiceTooltipEvent {

    @SubscribeEvent
    public void onShownTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        SpicesCap spiceData = stack.getCapability(IMCapabilities.FOOD_ITEM_DATA).orElse(null).getSpicesData();
        for (var spice : spiceData.getSpices()) {
            event.getToolTip().add(spice.getDisplayName().copy().append("x" + spice.getAmount()).append(spice.getStep().name()));
        }
        if (IMCommonConfig.deBug.get() && !ContainerHelper.getContainer(stack).isEmpty()) {
            event.getToolTip().add(Component.translatable(ContainerHelper.getContainer(stack).getDescriptionId()));
        }
    }

}
