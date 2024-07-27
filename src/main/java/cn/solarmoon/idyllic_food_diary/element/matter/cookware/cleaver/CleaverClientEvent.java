package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cleaver;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.optional_gui.IOptionalRecipeItemGui;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.idyllic_food_diary.registry.common.IMPacks;
import cn.solarmoon.idyllic_food_diary.network.NETList;
import cn.solarmoon.solarmoon_core.api.item_util.ItemStackUtil;
import cn.solarmoon.solarmoon_core.api.optional_recipe_item.IOptionalRecipeItem;
import cn.solarmoon.solarmoon_core.api.optional_recipe_item.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CleaverClientEvent {

    @SubscribeEvent
    public void chooseOutPut(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
            ItemStack held = ItemStackUtil.getItemInHand(player, IMItems.CHINESE_CLEAVER.get());
            if (held != null && held.getItem() instanceof IOptionalRecipeItem<?> pin && player.isCrouching()) {
                held.getCapability(SolarCapabilities.ITEMSTACK_DATA).ifPresent(d -> {
                    RecipeSelectorData recipeSelectorData = d.getRecipeSelectorData();
                    if (!pin.getMatchingRecipes(player).isEmpty() && pin.getItemsOnGui(player).size() != 1) {
                        // 根据鼠标滚动的方向更新索引
                        // 模边界算法，触底反弹
                        int size = pin.getItemsOnGui(player).size();

                        int index = pin.getHitBlockRecipeIndex(held, player);

                        if (event.getScrollDelta() > 0) {
                            index = (index + 1) % size;
                            IOptionalRecipeItemGui.goDown();
                        } else if (event.getScrollDelta() < 0) {
                            index = (index - 1 + size) % size;
                            IOptionalRecipeItemGui.goUp();
                        }
                        IOptionalRecipeItemGui.startScale();
                        recipeSelectorData.setIndex(index, pin.getHitBlock(player));
                        IMPacks.SERVER_PACK.getSender().stack(held).tag(recipeSelectorData.serializeNBT()).send(NETList.SYNC_RECIPE_INDEX);
                        IdyllicFoodDiary.DEBUG.send(index + "//");
                        event.setCanceled(true);
                    }
                });
            }
        }
    }

}
