package cn.solarmoon.immersive_delight.client.event;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.client.gui.IOptionalRecipeItemGui;
import cn.solarmoon.immersive_delight.common.recipe.RollingRecipe;
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
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;

import static cn.solarmoon.immersive_delight.common.registry.IMItems.ROLLING_PIN;


public class RollingPinClientEvent {

    @SubscribeEvent
    public void chooseOutPut(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ItemHelper finder = new ItemHelper(player);
        IOptionalRecipeItem<RollingRecipe> pin = finder.getItemInHand(ROLLING_PIN.get());
        Item held = finder.getItemInHand(ROLLING_PIN.get());
        if (player != null && player.isHolding(ROLLING_PIN.get()) && !Objects.requireNonNull(pin.getOptionalOutputs()).isEmpty() && player.isCrouching()) {
            IPlayerData playerData = CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA);
            RecipeSelectorData recipeSelectorData = playerData.getRecipeSelectorData();

            // 根据鼠标滚动的方向更新索引
            // 模边界算法，触底反弹
            int size = pin.getOptionalOutputs().size();
            int sizeRecipe = pin.getMatchingRecipes().size();

            int index = recipeSelectorData.getIndex(pin.getRecipeType());
            int recipeIndex = recipeSelectorData.getRecipeIndex(pin.getRecipeType());

            RollingRecipe recipe = pin.getMatchingRecipes().get(recipeIndex % sizeRecipe);

            if (event.getScrollDelta() > 0) {
                index = (index + 1) % size;
                IOptionalRecipeItemGui.goDown();
                if (recipe.getOutput() != null) {
                    ItemStack willSelect = pin.getOptionalOutputs().get(index);
                    if (!recipe.getOutput().test(willSelect)) recipeIndex = (recipeIndex + 1) % sizeRecipe;
                } else if (recipe.getOutput() == null) recipeIndex = (recipeIndex + 1) % sizeRecipe;
            } else if (event.getScrollDelta() < 0) {
                index = (index - 1 + size) % size;
                IOptionalRecipeItemGui.goUp();
                if (recipe.getOutput() != null) {
                    ItemStack willSelect = pin.getOptionalOutputs().get(index);
                    if (!recipe.getOutput().test(willSelect)) recipeIndex = (recipeIndex - 1 + sizeRecipe) % sizeRecipe;
                } else if (recipe.getOutput() == null) recipeIndex = (recipeIndex - 1 + sizeRecipe) % sizeRecipe;
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