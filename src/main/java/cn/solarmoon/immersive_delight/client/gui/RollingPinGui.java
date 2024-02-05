package cn.solarmoon.immersive_delight.client.gui;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.immersive_delight.api.util.CapabilityUtil;
import cn.solarmoon.immersive_delight.api.util.ItemHelper;
import cn.solarmoon.immersive_delight.common.item.RollingPinItem;
import cn.solarmoon.immersive_delight.api.registry.Capabilities;
import cn.solarmoon.immersive_delight.common.registry.IMItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;


@OnlyIn(Dist.CLIENT)
public class RollingPinGui {

    private static float alpha = 0.0F;

    public static float scale = 1.6F;
    public static float scaleB = 1.0F;
    public static boolean upRoll = false;
    public static boolean downRoll = false;

    private static int x = 325;
    private static int y = 80;

    public RollingPinGui(GuiGraphics graphics) {
        render(graphics);
    }

    public void render(GuiGraphics graphics) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        PoseStack poseStack = graphics.pose();
        ItemHelper finder = new ItemHelper(player);
        RollingPinItem pin = finder.getItemInHand(IMItems.ROLLING_PIN.get());
        RecipeSelectorData selector = CapabilityUtil.getData(player, Capabilities.PLAYER_DATA).getRecipeSelectorData();

        boolean show = player != null
                && player.isHolding(IMItems.ROLLING_PIN.get())
                && pin.recipeMatches()
                && player.isCrouching();

        // 更新alpha值
        float delta = mc.getDeltaFrameTime();
        float speed = 0.2F;  // 控制淡入淡出速度
        alpha += (show ? speed : -speed) * delta;
        alpha = Math.max(0, Math.min(alpha, 1));  // 确保alpha值在0到1之间

        float[] oldColor = RenderSystem.getShaderColor();
        float oldAlpha = oldColor[3];

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

        // 渲染贴图
        poseStack.pushPose();
        ResourceLocation texture = new ResourceLocation(ImmersiveDelight.MOD_ID, "textures/gui/rolling_pin.png");
        int textureX = 0;
        int textureY = 0;
        int width = 64;
        int height = 64;
        graphics.blit(texture, x, y, textureX, textureY, width, height);
        poseStack.popPose();

        ItemStack icon = ItemStack.EMPTY;
        ItemStack icon2 = ItemStack.EMPTY;
        ItemStack icon3 = ItemStack.EMPTY;
        if (show && !pin.getOptionalOutputs().isEmpty()) {

            poseStack.pushPose();
            icon = pin.getOptionalOutputs().get(selector.getIndex(pin.getRecipeType()));
            //渲染名称
            List<Component> components = new ArrayList<>();
            components.add(icon.getHoverName());
            graphics.renderComponentTooltip(mc.font, components, x+40, y+9);
            poseStack.popPose();

            int size = pin.getOptionalOutputs().size();
            if (size > 1) {
                icon2 = pin.getOptionalOutputs().get((selector.getIndex(pin.getRecipeType()) + 1) % size);
                icon3 = pin.getOptionalOutputs().get((selector.getIndex(pin.getRecipeType()) - 1 + size) % size);
            }
            if (upRoll) {
                poseStack.pushPose();
                float scaleSpeed = 0.1F;  // 控制放大速度
                scaleB -= scaleSpeed * delta;
                scaleB = Math.max(scaleB, 1.0F);
                // 放大倍率
                graphics.pose().scale(scaleB, scaleB, scaleB);
                // 渲染贴图
                graphics.renderItem(icon2, (int) ((x + 25) / scaleB + 6 - scaleB * (6)), (int) ((y)/scaleB - (30 - scaleB * 30 )));
                poseStack.popPose();
            }
            if (downRoll) {
                poseStack.pushPose();
                float scaleSpeed = 0.1F;  // 控制放大速度
                scaleB -= scaleSpeed * delta;
                scaleB = Math.max(scaleB, 1.0F);
                // 放大倍率
                graphics.pose().scale(scaleB, scaleB, scaleB);
                // 渲染贴图
                graphics.renderItem(icon3, (int) ((x + 25) / scaleB + 6 - scaleB * (6)), (int) ((y + 46)/scaleB + (30 - scaleB * 30 )));
                poseStack.popPose();
            }
            if (!upRoll && !downRoll) {
                poseStack.pushPose();
                graphics.renderItem(icon2, x + 25, y);
                graphics.renderItem(icon3, x + 25, y + 46);
                poseStack.popPose();
            }
        }

        // icon放大动画
        if(upRoll) {
            poseStack.pushPose();
            float scaleSpeed = 0.1F;  // 控制放大速度
            scale += scaleSpeed * delta;
            scale = Math.min(scale, 1.6F);
            if (scale >= 1.6F) {
                upRoll = false;
            }
            // 放大倍率
            graphics.pose().scale(scale, scale, scale);
            // 计算渲染位置
            int renderX = (int) ((x+22) / scale + 10 - scale*(10/1.5));
            int renderY = (int) ((y+27) / scale + (80 - scale*(80/1.5)));
            // 渲染贴图
            graphics.renderItem(icon, renderX, renderY);
            poseStack.popPose();
        }
        if(downRoll) {
            poseStack.pushPose();
            float scaleSpeed = 0.1F;  // 控制放大速度
            scale += scaleSpeed * delta;
            scale = Math.min(scale, 1.6F);
            if (scale >= 1.6F) {
                downRoll = false;
            }
            // 放大倍率
            graphics.pose().scale(scale, scale, scale);
            // 计算渲染位置
            int renderX = (int) ((x+22) / scale + 10 - scale*(10/1.5));
            int renderY = (int) ((y+12) / scale - (60 - scale*(60/1.5)));
            // 渲染贴图
            graphics.renderItem(icon, renderX, renderY);
            poseStack.popPose();
        }
        if (!upRoll && !downRoll && show){
            poseStack.pushPose();
            // 放大倍率
            graphics.pose().scale(scale, scale, scale);
            // 计算渲染位置
            int renderX = (int) ((x+22) / scale);
            int renderY = (int) ((y+19) / scale);
            // 渲染贴图
            graphics.renderItem(icon, renderX, renderY);
            poseStack.popPose();
        }

        RenderSystem.setShaderColor(oldColor[0], oldColor[1], oldColor[2], oldAlpha);
        RenderSystem.disableBlend();
    }

    public static void goUp() {
        upRoll = true;
        downRoll = false;
    }

    public static void goDown() {
        upRoll = false;
        downRoll = true;
    }

    public static void startScale() {
        scale = 1.0F;
        scaleB = 1.6f;
    }

}