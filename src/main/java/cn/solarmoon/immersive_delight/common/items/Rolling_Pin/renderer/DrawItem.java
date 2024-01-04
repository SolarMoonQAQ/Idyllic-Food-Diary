package cn.solarmoon.immersive_delight.common.items.Rolling_Pin.renderer;

import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.events.client.ChooseOutput;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static cn.solarmoon.immersive_delight.util.Constants.mc;
import static cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client.HitResultRecipeCheck.hitResultRecipeCheck;
import static cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client.HoldRollingCheck.holdRollingCheck;


@OnlyIn(Dist.CLIENT)
public class DrawItem {

    private static float alpha = 0.0F;

    public static float scale = 1.6F;

    private static final int x = 325;
    private static final int y = 80;

    public static void drawItem(GuiGraphics graphics) {

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

        float[] oldColor = RenderSystem.getShaderColor();
        float oldAlpha = oldColor[3];

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

        ItemStack icon = ItemStack.EMPTY;
        if (ChooseOutput.possibleOutputs != null && show) {
            int size = ChooseOutput.possibleOutputs.size();
            if(size > 0) {
                icon = ChooseOutput.possibleOutputs.get(ChooseOutput.currentRecipeIndex % size);
            }
            //渲染名称
            graphics.renderTooltip(mc.font, icon, x+40, y+9);
        }

        // icon放大动画
        if(ChooseOutput.upRoll) {
            float scaleSpeed = 0.1F;  // 控制放大速度
            scale += scaleSpeed * delta;
            scale = Math.min(scale, 1.6F);
            if (scale >= 1.6F) {
                ChooseOutput.upRoll = false;
            }
            // 放大倍率
            graphics.pose().scale(scale, scale, scale);
            // 计算渲染位置
            int renderX = (int) ((x+22) / scale + 10 - scale*(10/1.5));
            int renderY = (int) ((y+27) / scale + (80 - scale*(80/1.5)));
            // 渲染贴图
            graphics.renderItem(icon, renderX, renderY);
        }
        if(ChooseOutput.downRoll) {
            float scaleSpeed = 0.1F;  // 控制放大速度
            scale += scaleSpeed * delta;
            scale = Math.min(scale, 1.6F);
            if (scale >= 1.6F) {
                ChooseOutput.downRoll = false;
            }
            // 放大倍率
            graphics.pose().scale(scale, scale, scale);
            // 计算渲染位置
            int renderX = (int) ((x+22) / scale + 10 - scale*(10/1.5));
            int renderY = (int) ((y+12) / scale - (60 - scale*(60/1.5)));
            // 渲染贴图
            graphics.renderItem(icon, renderX, renderY);
        }
        if (!ChooseOutput.upRoll && !ChooseOutput.downRoll && show){
            // 放大倍率
            graphics.pose().scale(scale, scale, scale);
            // 计算渲染位置
            int renderX = (int) ((x+22) / scale);
            int renderY = (int) ((y+19) / scale);
            // 渲染贴图
            graphics.renderItem(icon, renderX, renderY);
        }

        RenderSystem.setShaderColor(oldColor[0], oldColor[1], oldColor[2], oldAlpha);
        RenderSystem.disableBlend();
        graphics.pose().popPose();

    }

}