package cn.solarmoon.immersive_delight.client.event;

import cn.solarmoon.immersive_delight.api.common.capability.IPlayerData;
import cn.solarmoon.immersive_delight.api.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.immersive_delight.api.network.serializer.ServerPackSerializer;
import cn.solarmoon.immersive_delight.api.util.CapabilityUtil;
import cn.solarmoon.immersive_delight.common.registry.IMCapabilities;
import cn.solarmoon.immersive_delight.common.event.RollingPinEvent;
import cn.solarmoon.immersive_delight.common.item.RollingPinItem;
import cn.solarmoon.immersive_delight.common.recipes.RollingPinRecipe;
import cn.solarmoon.immersive_delight.util.RollingPinHelper;
import cn.solarmoon.immersive_delight.api.util.ItemHelper;
import cn.solarmoon.immersive_delight.util.CoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

import static cn.solarmoon.immersive_delight.client.gui.rolling_pin.DrawItem.scale;
import static cn.solarmoon.immersive_delight.client.gui.rolling_pin.DrawLittleItem.scaleB;
import static cn.solarmoon.immersive_delight.common.registry.IMItems.ROLLING_PIN;

@OnlyIn(Dist.CLIENT)
public class RollingPinClientEvent {

    public static List<ItemStack> possibleOutputs;
    public static List<ItemStack> actualResults;

    //滚动鼠标动态更新数组物品指定行
    public static int currentRecipeIndex = 0;

    private int index;
    private int recipeIndex;

    public static boolean upRoll = false;
    public static boolean downRoll = false;

    @SubscribeEvent
    public void chooseOutPut(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ItemHelper finder = new ItemHelper(player);
        RollingPinItem pin = finder.getItemInHand(ROLLING_PIN.get());
        if (player != null && player.isHolding(ROLLING_PIN.get()) && !pin.getOptionalOutputs().isEmpty() && player.isCrouching()) {
            IPlayerData playerData = CapabilityUtil.getData(player, IMCapabilities.PLAYER_DATA);
            RecipeSelectorData recipeSelectorData = playerData.getRecipeSelectorData();

            // 根据鼠标滚动的方向更新索引
            // 模边界算法，触底反弹
            int size = pin.getOptionalOutputs().size();
            int sizeRecipe = pin.getMatchingRecipes().size();

            RollingPinRecipe recipe = pin.getMatchingRecipes().get(recipeIndex % sizeRecipe);

            if (event.getScrollDelta() > 0) {
                index = (index + 1) % size;
                upRoll = true;
                downRoll = false;
                scale = 1.0F;
                scaleB = 1.6f;
                if (recipe.getOutput() != null) {
                    ItemStack willSelect = pin.getOptionalOutputs().get(index);
                    if (!recipe.getOutput().test(willSelect)) recipeIndex = (recipeIndex + 1) % sizeRecipe;
                } else if (recipe.getOutput() == null) recipeIndex = (recipeIndex + 1) % sizeRecipe;
            } else if (event.getScrollDelta() < 0) {
                index = (index - 1 + size) % size;
                downRoll = true;
                upRoll = false;
                scale = 1.0F;
                scaleB = 1.6f;
                if (recipe.getOutput() != null) {
                    ItemStack willSelect = pin.getOptionalOutputs().get(index);
                    if (!recipe.getOutput().test(willSelect)) recipeIndex = (recipeIndex - 1 + sizeRecipe) % sizeRecipe;
                } else if (recipe.getOutput() == null) recipeIndex = (recipeIndex - 1 + sizeRecipe) % sizeRecipe;
            }

            recipeSelectorData.setIndex(index);
            recipeSelectorData.setRecipeIndex(recipeIndex);
            ServerPackSerializer.sendPacket(index, "updateIndex");
            ServerPackSerializer.sendPacket(recipeIndex, "updateRecipeIndex");
            CoreUtil.deBug(index + "//" + recipeIndex);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void updateMatchRecipe(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && RollingPinHelper.holdRollingCheck() && RollingPinHelper.hitResultRecipeCheck() && mc.player.isCrouching()) {
            Block block = null;
            if (mc.level != null) {
                if (mc.hitResult != null) {
                    block = mc.level.getBlockState(((BlockHitResult)mc.hitResult).getBlockPos()).getBlock();
                }
            }
            RollingPinHelper.updatePossibleOutputs(block);
        }
    }

    //擀面风暴！
    @SubscribeEvent
    public void sweepHarvest(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        if(!itemStack.is(ROLLING_PIN.get()) || !player.isCrouching()) return;
        RollingPinEvent.harvest(0, player, event.getLevel());
    }

}
