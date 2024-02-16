package cn.solarmoon.immersive_delight.client.event;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.client.gui.IOptionalRecipeItemGui;
import cn.solarmoon.immersive_delight.common.recipe.CleaverRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.immersive_delight.common.registry.IMPacks;
import cn.solarmoon.solarmoon_core.common.capability.IPlayerData;
import cn.solarmoon.solarmoon_core.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.common.item.IOptionalRecipeItem;
import cn.solarmoon.solarmoon_core.registry.SolarCapabilities;
import cn.solarmoon.solarmoon_core.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.util.ItemHelper;
import cn.solarmoon.solarmoon_core.util.namespace.SolarNETList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CleaverClientEvent {

    @SubscribeEvent
    public void chooseOutPut(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ItemHelper finder = new ItemHelper(player);
        IOptionalRecipeItem<CleaverRecipe> pin = finder.getItemInHand(IMItems.CHINESE_CLEAVER.get());
        Item held = finder.getItemInHand(IMItems.CHINESE_CLEAVER.get());
        if (player != null && player.isHolding(IMItems.CHINESE_CLEAVER.get()) && !pin.getOptionalOutputs().isEmpty() && player.isCrouching()) {
            IPlayerData playerData = CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA);
            RecipeSelectorData recipeSelectorData = playerData.getRecipeSelectorData();

            // 根据鼠标滚动的方向更新索引
            // 模边界算法，触底反弹
            int size = pin.getOptionalOutputs().size();
            int sizeRecipe = pin.getMatchingRecipes().size();

            int index = recipeSelectorData.getIndex(pin.getRecipeType());
            int recipeIndex = recipeSelectorData.getRecipeIndex(pin.getRecipeType());

            CleaverRecipe recipe = pin.getMatchingRecipes().get(recipeIndex % sizeRecipe);

            if (event.getScrollDelta() > 0) {
                index = (index + 1) % size;
                IOptionalRecipeItemGui.goDown();
                recipeIndex = (recipeIndex + 1) % sizeRecipe;
            } else if (event.getScrollDelta() < 0) {
                index = (index - 1 + size) % size;
                IOptionalRecipeItemGui.goUp();
                recipeIndex = (recipeIndex - 1 + sizeRecipe) % sizeRecipe;
            }
            IOptionalRecipeItemGui.startScale();
            recipeSelectorData.setIndex(index, recipe.getType());
            recipeSelectorData.setRecipeIndex(recipeIndex, recipe.getType());
            IMPacks.SERVER_PACK.getSender().send(SolarNETList.SYNC_INDEX, index, held.getDefaultInstance());
            IMPacks.SERVER_PACK.getSender().send(SolarNETList.SYNC_RECIPE_INDEX, recipeIndex, held.getDefaultInstance());
            ImmersiveDelight.DEBUG.send(index + "//" + recipeIndex);
            event.setCanceled(true);
        }
    }

}
