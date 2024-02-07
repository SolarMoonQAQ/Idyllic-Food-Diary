package cn.solarmoon.immersive_delight.client.event;

import cn.solarmoon.immersive_delight.api.common.capability.IPlayerData;
import cn.solarmoon.immersive_delight.api.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.immersive_delight.api.registry.Capabilities;
import cn.solarmoon.immersive_delight.api.util.CapabilityUtil;
import cn.solarmoon.immersive_delight.api.util.ItemHelper;
import cn.solarmoon.immersive_delight.client.gui.RollingPinGui;
import cn.solarmoon.immersive_delight.common.item.RollingPinItem;
import cn.solarmoon.immersive_delight.common.recipes.RollingPinRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMPacks;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.util.CoreUtil;
import cn.solarmoon.immersive_delight.util.namespace.NETList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static cn.solarmoon.immersive_delight.common.registry.IMItems.ROLLING_PIN;


public class RollingPinClientEvent {

    @SubscribeEvent
    public void chooseOutPut(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ItemHelper finder = new ItemHelper(player);
        RollingPinItem pin = finder.getItemInHand(ROLLING_PIN.get());
        if (player != null && player.isHolding(ROLLING_PIN.get()) && !pin.getOptionalOutputs().isEmpty() && player.isCrouching()) {
            IPlayerData playerData = CapabilityUtil.getData(player, Capabilities.PLAYER_DATA);
            RecipeSelectorData recipeSelectorData = playerData.getRecipeSelectorData();

            // 根据鼠标滚动的方向更新索引
            // 模边界算法，触底反弹
            int size = pin.getOptionalOutputs().size();
            int sizeRecipe = pin.getMatchingRecipes().size();

            int index = recipeSelectorData.getIndex(IMRecipes.ROLLING_RECIPE.get());
            int recipeIndex = recipeSelectorData.getRecipeIndex(IMRecipes.ROLLING_RECIPE.get());

            RollingPinRecipe recipe = pin.getMatchingRecipes().get(recipeIndex % sizeRecipe);

            if (event.getScrollDelta() > 0) {
                index = (index + 1) % size;
                RollingPinGui.goDown();
                if (recipe.getOutput() != null) {
                    ItemStack willSelect = pin.getOptionalOutputs().get(index);
                    if (!recipe.getOutput().test(willSelect)) recipeIndex = (recipeIndex + 1) % sizeRecipe;
                } else if (recipe.getOutput() == null) recipeIndex = (recipeIndex + 1) % sizeRecipe;
            } else if (event.getScrollDelta() < 0) {
                index = (index - 1 + size) % size;
                RollingPinGui.goUp();
                if (recipe.getOutput() != null) {
                    ItemStack willSelect = pin.getOptionalOutputs().get(index);
                    if (!recipe.getOutput().test(willSelect)) recipeIndex = (recipeIndex - 1 + sizeRecipe) % sizeRecipe;
                } else if (recipe.getOutput() == null) recipeIndex = (recipeIndex - 1 + sizeRecipe) % sizeRecipe;
            }
            RollingPinGui.startScale();
            recipeSelectorData.setIndex(index, recipe.getType());
            recipeSelectorData.setRecipeIndex(recipeIndex, recipe.getType());
            IMPacks.SERVER_PACK.getSender().send(NETList.SYNC_INDEX, index, pin.getDefaultInstance());
            IMPacks.SERVER_PACK.getSender().send(NETList.SYNC_RECIPE_INDEX, recipeIndex, pin.getDefaultInstance());
            CoreUtil.deBug(index + "//" + recipeIndex);
            event.setCanceled(true);
        }
    }

}
