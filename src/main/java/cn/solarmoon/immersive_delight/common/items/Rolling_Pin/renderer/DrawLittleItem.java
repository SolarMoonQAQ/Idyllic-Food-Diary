package cn.solarmoon.immersive_delight.common.items.Rolling_Pin.renderer;

import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.events.client.ChooseOutput;
import cn.solarmoon.immersive_delight.ImmersiveDelight;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static cn.solarmoon.immersive_delight.util.Constants.mc;
import static cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client.HitResultRecipeCheck.hitResultRecipeCheck;
import static cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client.HoldRollingCheck.holdRollingCheck;


@OnlyIn(Dist.CLIENT)
public class DrawLittleItem {

    private static float alpha = 0.0F;

    public static float scaleB = 1.0F;

    private static final int x = 325;
    private static final int y = 80;

    public static void drawLittleItem(GuiGraphics graphics) {

        graphics.pose().pushPose();

        boolean show = holdRollingCheck()
                && hitResultRecipeCheck()
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
        if (ChooseOutput.possibleOutputs != null) {
            int size = ChooseOutput.possibleOutputs.size();
            if (size > 0) {
                icon2 = ChooseOutput.possibleOutputs.get((ChooseOutput.currentRecipeIndex - 1 + size) % size);
                icon3 = ChooseOutput.possibleOutputs.get((ChooseOutput.currentRecipeIndex + 1) % size);
            }
            if (ChooseOutput.upRoll) {
                float scaleSpeed = 0.1F;  // 控制放大速度
                scaleB -= scaleSpeed * delta;
                scaleB = Math.max(scaleB, 1.0F);
                // 放大倍率
                graphics.pose().scale(scaleB, scaleB, scaleB);
                // 渲染贴图
                graphics.renderItem(icon2, (int) ((x + 25) / scaleB + 6 - scaleB * (6)), (int) ((y)/scaleB - (30 - scaleB * 30 )));
            }
            if (ChooseOutput.downRoll) {
                float scaleSpeed = 0.1F;  // 控制放大速度
                scaleB -= scaleSpeed * delta;
                scaleB = Math.max(scaleB, 1.0F);
                // 放大倍率
                graphics.pose().scale(scaleB, scaleB, scaleB);
                // 渲染贴图
                graphics.renderItem(icon3, (int) ((x + 25) / scaleB + 6 - scaleB * (6)), (int) ((y + 46)/scaleB + (30 - scaleB * 30 )));
            }
            if (mc.player != null && !ChooseOutput.upRoll && !ChooseOutput.downRoll && show) {
                graphics.renderItem(icon2, x + 25, y);
                graphics.renderItem(icon3, x + 25, y + 46);
            }
        }

        RenderSystem.disableBlend();

        graphics.pose().popPose();
    }

}
