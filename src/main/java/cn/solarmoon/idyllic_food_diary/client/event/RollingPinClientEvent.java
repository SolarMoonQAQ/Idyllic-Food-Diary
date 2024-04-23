package cn.solarmoon.idyllic_food_diary.client.event;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.client.gui.IOptionalRecipeItemGui;
import cn.solarmoon.idyllic_food_diary.common.registry.IMPacks;
import cn.solarmoon.solarmoon_core.api.common.capability.IItemStackData;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.api.common.item.IOptionalRecipeItem;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.api.util.ItemStackUtil;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNETList;
import cn.solarmoon.solarmoon_core.common.registry.SolarCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static cn.solarmoon.idyllic_food_diary.common.registry.IMItems.ROLLING_PIN;


public class RollingPinClientEvent {

    @SubscribeEvent
    public void chooseOutPut(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
            ItemStack held = ItemStackUtil.getItemInHand(player, ROLLING_PIN.get());
            if (held != null && held.getItem() instanceof IOptionalRecipeItem<?> pin && player.isCrouching()) {
                IItemStackData itemStackData = CapabilityUtil.getData(held, SolarCapabilities.ITEMSTACK_DATA);
                RecipeSelectorData selector = itemStackData.getRecipeSelectorData();
                if (!pin.getMatchingRecipes(player).isEmpty()) {
                    // 根据鼠标滚动的方向更新索引
                    // 模边界算法，触底反弹
                    int size = pin.getItemsOnGui(player).size();

                    int index = selector.getIndex(pin.getHitBlock(player));
                    IdyllicFoodDiary.DEBUG.send(index + "//" + size);

                    if (event.getScrollDelta() > 0) {
                        index = (index + 1) % size;
                        IOptionalRecipeItemGui.goDown();
                    } else if (event.getScrollDelta() < 0) {
                        index = (index - 1 + size) % size;
                        IOptionalRecipeItemGui.goUp();
                    }
                    IOptionalRecipeItemGui.startScale();
                    selector.setIndex(index, pin.getHitBlock(player));
                    IMPacks.SERVER_PACK.getSender().send(SolarNETList.SYNC_RECIPE_INDEX, held, selector.serializeNBT());
                    IdyllicFoodDiary.DEBUG.send(index + "//" + size);
                    event.setCanceled(true);
                }
            }
        }
    }

}