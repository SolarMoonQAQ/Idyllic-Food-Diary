package cn.solarmoon.immersive_delight.client.ItemRenderer;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.util.FluidHelper;
import cn.solarmoon.immersive_delight.api.util.TextUtil;
import cn.solarmoon.immersive_delight.common.IMItems;
import cn.solarmoon.immersive_delight.api.common.item.BaseTankItem;
import cn.solarmoon.immersive_delight.util.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemStackHandler;
import org.joml.Matrix4f;

public class TankableTooltipRenderer implements ClientTooltipComponent {

    private final TankTooltip tankTooltip;
    //渲染大小
    private final int size = 16;
    //渲染贴图数，间接决定渲染长度
    private final int count = 5;
    //高度偏移
    private final int height = 3;
    //液体和文字和物品的整体偏移量
    //除贴图外的长度偏移，用于和贴图匹配
    private final int deltaF = 5;
    //除贴图外的高度偏移，用于和贴图匹配
    private final int hOffset = 2;

    public TankableTooltipRenderer(TankTooltip tankTooltip) {
        this.tankTooltip = tankTooltip;
    }

    @Override
    public int getHeight() {
        return couldBeRendered() ? size + 10 + height : 0;
    }

    /**
     * 宽度
     * 为液体渲染宽度
     * 实测这玩意是累增，这个相当于设置了个最小值
     */
    @Override
    public int getWidth(Font font) {
        return couldBeRendered() ? size * count + deltaF + 5 : 0;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        if(couldBeRendered()) {

            ItemStack stack = tankTooltip.itemStack;
            IFluidHandlerItem stackTank = FluidHelper.getTank(stack);
            FluidStack fluidStack = FluidHelper.getFluidStack(stack);

            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            drawFluid(fluidStack, guiGraphics, stackTank, x ,y);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            //液体名称容量显示
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            drawText(poseStack, fluidStack, font, guiGraphics, x, y);
            poseStack.popPose();

            //渲染内容物
            poseStack.pushPose();
            drawItem(stack, poseStack, guiGraphics, x, y);
            poseStack.popPose();

            //渲染图片
            poseStack.pushPose();
            drawGUI(stack, poseStack, guiGraphics, x, y);
            poseStack.popPose();

            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();

        }
    }

    /**
     * 渲染液体容量描述
     */
    public void drawText(PoseStack poseStack, FluidStack fluidStack, Font font, GuiGraphics guiGraphics, int x, int y) {
        poseStack.translate(0,0,2);
        if(!fluidStack.isEmpty()) {
            String str1 = fluidStack.getFluid().getFluidType().getDescription().getString();
            String str2 = fluidStack.getAmount() + "mB";
            int str1Length = font.width(str1);
            guiGraphics.drawString(font, str1, x + 2 + deltaF, y + 4 + height + hOffset, 0xFFFFFF);
            guiGraphics.drawString(font, str2, x + str1Length + 3 + deltaF, y + 4 + height + hOffset, 0xFFFFFF);
        } else {
            guiGraphics.drawString(font, TextUtil.translation("tooltip", "fluid_empty"), x + 2 + deltaF, y + 4 + height + hOffset, 0xFFFFFF);
        }
    }

    /**
     * 渲染物品
     */
    public void drawItem(ItemStack stack, PoseStack poseStack, GuiGraphics guiGraphics, int x, int y) {
        ItemStackHandler inventory = ContainerHelper.getInventory(stack);
        ItemStack stackIn = inventory.getStackInSlot(0);
        if (!stackIn.isEmpty()) {
            poseStack.translate(0,0,2);
            float scale = 1f;
            poseStack.scale(1f/scale, 1f/scale, 1f);
            guiGraphics.renderItem(stackIn, (int) ((x + size * count - 16 + deltaF) * scale), (int) ((y + height + hOffset) * scale));
        }
    }

    /**
     * 渲染gui
     */
    public void drawGUI(ItemStack stack, PoseStack poseStack, GuiGraphics guiGraphics, int x, int y) {
        if (stack.getItem() instanceof BaseTankItem) {
            String cate = "";
            if (stack.is(IMItems.CELADON_CUP.get())) {
                cate = "celadon_cup";
            }
            String res = "textures/gui/" + cate + ".png";
            float scale = 2.5f;
            poseStack.scale(1f/scale, 1f/scale, 1f);
            guiGraphics.blit(new ResourceLocation(ImmersiveDelight.MOD_ID, res),
                    (int) (x*scale), (int) (y*scale) + height - 2,
                    0, 0, 256, 64);
        }
    }

    /**
     * 渲染液体
     */
    public void drawFluid(FluidStack fluidStack, GuiGraphics guiGraphics, IFluidHandlerItem stackTank, int x, int y) {
        if(!fluidStack.isEmpty()) {
            TextureAtlasSprite fluidStillSprite = FluidRenderHelper.FluidRenderMap.getFluidTexture(fluidStack, FluidRenderHelper.FluidRenderMap.FluidFlow.STILL);
            float[] color = FluidRenderHelper.FluidRenderMap.getColorARGB(fluidStack);

            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            RenderSystem.setShaderColor(color[0], color[1], color[2], color[3]);

            float uMin = fluidStillSprite.getU0();
            float uMax = fluidStillSprite.getU1();
            float vMin = fluidStillSprite.getV0();
            float vMax = fluidStillSprite.getV1();

            Matrix4f matrix = guiGraphics.pose().last().pose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferBuilder = tesselator.getBuilder();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            for (int i = 0; i < count; i++) {
                float percentage = FluidHelper.getScale(stackTank);

                if (percentage > (float) i / count) {
                    float reduction = 0;
                    //实际长度与已渲染长度的差额
                    float difference = (i + 1) * size - count * size * percentage;
                    //小于0，表明目前长度比总长度小，故而无需削减（具体到i就是渲染的第i+1个贴图长度和比总长（实际长度）小，所以自身无需应用削减）
                    //也因此削减总是在一个size范围内，因为这里的差值永远被i跟进，从而永远是一个size内的差值
                    if (difference > 0) reduction = difference;
                    //偏移量，使能连续无缝跟进贴图
                    int offset = i * size;

                    uMax = uMax - (reduction / 16F * (uMax - uMin));
                    bufferBuilder.vertex(matrix, x + deltaF + offset, y + size + height + hOffset, 1).uv(uMin, vMax).endVertex();
                    bufferBuilder.vertex(matrix, x + deltaF + size + offset - reduction, y + size + height + hOffset, 1).uv(uMax, vMax).endVertex();
                    bufferBuilder.vertex(matrix, x + deltaF + size + offset - reduction, y + height + hOffset, 1).uv(uMax, vMin).endVertex();
                    bufferBuilder.vertex(matrix, x + deltaF + offset, y + height + hOffset, 1).uv(uMin, vMin).endVertex();
                }
            }
            BufferUploader.drawWithShader(bufferBuilder.end());
        }
    }

    /**
     * 渲染液体的条件
     * 需要物品属于tank
     */
    public boolean couldBeRendered() {
        return tankTooltip.itemStack.getItem() instanceof BaseTankItem;
    }

    public static class TankTooltip implements TooltipComponent {

        private final ItemStack itemStack;

        public TankTooltip(ItemStack stack) {
            this.itemStack = stack;
        }

    }

}
