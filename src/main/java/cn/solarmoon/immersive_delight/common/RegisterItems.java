package cn.solarmoon.immersive_delight.common;


import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.events.client.ChooseOutput;
import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.events.client.UpdateMatchRecipe;
import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.renderer.DrawItem;
import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.renderer.DrawLittleItem;
import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.recipe.RollingPinRecipe;
import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.RollingPinItem;
import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.events.LeftClickGet;
import cn.solarmoon.immersive_delight.compat.apple_skin.AppleSkin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.*;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterItems {

    /**
     * 擀面杖大类
     */
    //注册物品
    public static final RegistryObject<Item> ROLLING_PIN = ITEMS.register("rolling_pin", RollingPinItem::new);
    //注册配方
    public static final RegistryObject<RecipeType<RollingPinRecipe>> ROLLING = RECIPE_TYPES.register("rolling", () -> registerRecipeType("rolling"));
    public static final RegistryObject<RecipeSerializer<?>> ROLLING_SERIALIZER = RECIPE_SERIALIZERS.register("rolling", RollingPinRecipe.Serializer::new);

    //注册GUI
    @SubscribeEvent
    public static void registerGUI(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.DEBUG_TEXT.id(), "draw_item", (gui, poseStack, partialTick, width, height) -> {
            gui.setupOverlayRenderState(true, true);
            DrawItem.drawItem(poseStack);
        });
        event.registerAbove(VanillaGuiOverlay.DEBUG_TEXT.id(), "draw_little_item", (gui, poseStack, partialTick, width, height) -> {
            gui.setupOverlayRenderState(true, false);
            DrawLittleItem.drawLittleItem(poseStack);
        });
    }

    //客户端事件订阅
    @SubscribeEvent
    public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new UpdateMatchRecipe());
        MinecraftForge.EVENT_BUS.register(new ChooseOutput());
    }

    //服务端事件订阅
    @SubscribeEvent
    public static void onFMLCommonSetupEvent(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new LeftClickGet());
    }

}
