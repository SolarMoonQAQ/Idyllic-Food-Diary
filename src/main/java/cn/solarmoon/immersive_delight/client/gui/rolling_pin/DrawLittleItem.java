package cn.solarmoon.immersive_delight.client.gui.rolling_pin;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.immersive_delight.api.util.CapabilityUtil;
import cn.solarmoon.immersive_delight.client.event.RollingPinClientEvent;
import cn.solarmoon.immersive_delight.common.registry.IMCapabilities;
import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.immersive_delight.common.item.RollingPinItem;
import cn.solarmoon.immersive_delight.api.util.ItemHelper;
import cn.solarmoon.immersive_delight.util.RollingPinHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class DrawLittleItem {

    private static float alpha = 0.0F;

    public static float scaleB = 1.0F;

    private static final int x = 325;
    private static final int y = 80;

    public static void drawLittleItem(GuiGraphics graphics) {

        Minecraft mc = Minecraft.getInstance();

        LocalPlayer player = mc.player;

        ItemHelper finder = new ItemHelper(player);
        RollingPinItem pin = finder.getItemInHand(IMItems.ROLLING_PIN.get());

        RecipeSelectorData selector = CapabilityUtil.getData(player, IMCapabilities.PLAYER_DATA).getRecipeSelectorData();

        graphics.pose().pushPose();

        boolean show = RollingPinHelper.holdRollingCheck()
                && RollingPinHelper.hitResultRecipeCheck()
                && mc.player != null
                && mc.player.isCrouching();

        // 更新alpha值
        float delta = mc.getDeltaFrameTime();
        float speed = 0.2F;  // 控制淡入淡出速度
        alpha += (show ? speed : -speed) * delta;
        alpha = Math.max(0, Math.min(alpha, 1));  // 确保alpha值在0到1之间

        // 设置透明度
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

        // 渲染贴图
        ResourceLocation texture = new ResourceLocation(ImmersiveDelight.MOD_ID, "textures/gui/rolling_pin.png");
        int textureX = 0;
        int textureY = 0;
        int width = 64;
        int height = 64;
        graphics.blit(texture, x, y, textureX, textureY, width, height);

        ItemStack icon2 = ItemStack.EMPTY;
        ItemStack icon3 = ItemStack.EMPTY;
        if (!pin.getOptionalOutputs().isEmpty()) {
            int size = pin.getOptionalOutputs().size();
            if (size > 1) {
                icon2 = pin.getOptionalOutputs().get((selector.getIndex() - 1 + size) % size);
                icon3 = pin.getOptionalOutputs().get((selector.getIndex() + 1) % size);
            }
            if (RollingPinClientEvent.upRoll) {
                float scaleSpeed = 0.1F;  // 控制放大速度
                scaleB -= scaleSpeed * delta;
                scaleB = Math.max(scaleB, 1.0F);
                // 放大倍率
                graphics.pose().scale(scaleB, scaleB, scaleB);
                // 渲染贴图
                graphics.renderItem(icon2, (int) ((x + 25) / scaleB + 6 - scaleB * (6)), (int) ((y)/scaleB - (30 - scaleB * 30 )));
            }
            if (RollingPinClientEvent.downRoll) {
                float scaleSpeed = 0.1F;  // 控制放大速度
                scaleB -= scaleSpeed * delta;
                scaleB = Math.max(scaleB, 1.0F);
                // 放大倍率
                graphics.pose().scale(scaleB, scaleB, scaleB);
                // 渲染贴图
                graphics.renderItem(icon3, (int) ((x + 25) / scaleB + 6 - scaleB * (6)), (int) ((y + 46)/scaleB + (30 - scaleB * 30 )));
            }
            if (mc.player != null && !RollingPinClientEvent.upRoll && !RollingPinClientEvent.downRoll && show) {
                graphics.renderItem(icon2, x + 25, y);
                graphics.renderItem(icon3, x + 25, y + 46);
            }
        }

        RenderSystem.disableBlend();

        graphics.pose().popPose();
    }

}
