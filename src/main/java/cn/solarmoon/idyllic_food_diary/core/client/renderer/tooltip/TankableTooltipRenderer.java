package cn.solarmoon.idyllic_food_diary.core.client.renderer.tooltip;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.util.TextureRenderUtil;
import cn.solarmoon.idyllic_food_diary.core.data.tags.IMItemTags;
import cn.solarmoon.solarmoon_core.api.client.renderer.tooltip.BaseTooltipComponent;
import cn.solarmoon.solarmoon_core.api.common.item.ITankItem;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import cn.solarmoon.solarmoon_core.api.util.TextUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Matrix4f;

public class TankableTooltipRenderer implements ClientTooltipComponent {

    private final TooltipComponent tankTooltip;
    //渲染大小
    private final int size = 16;
    //渲染贴图数，间接决定渲染长度
    private final int count = 5;
    //高度偏移
    private final int height = 3;
    //液体和文字和物品的整体偏移量
    //除贴图外的长度偏移，用于和贴图匹配
    private final int deltaF = 4;
    //除贴图外的高度偏移，用于和贴图匹配
    private final int hOffset = 2;

    public TankableTooltipRenderer(TooltipComponent tankTooltip) {
        this.tankTooltip = tankTooltip;
    }

    @Override
    public int getHeight() {
        return canBeRendered() ? size + height : 0;
    }

    @Override
    public int getWidth(Font font) {
        return canBeRendered() ? size * count + deltaF + 5 : 0;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        if(canBeRendered()) {

            ItemStack stack = tankTooltip.getItemStack();
            IFluidHandlerItem stackTank = FluidUtil.getTank(stack);
            FluidStack fluidStack = FluidUtil.getFluidStack(stack);

            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            drawFluid(fluidStack, guiGraphics, stackTank, x ,y);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            //液体名称容量显示
            PoseStack poseStack = guiGraphics.pose();

            drawText(poseStack, fluidStack, font, guiGraphics, x, y);

            //渲染内容物
            drawItem(stack, poseStack, guiGraphics, x, y);

            //渲染图片
            drawGUI(stack, poseStack, guiGraphics, x, y);

            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();

        }
    }

    /**
     * 渲染液体容量描述
     */
    public void drawText(PoseStack poseStack, FluidStack fluidStack, Font font, GuiGraphics guiGraphics, int x, int y) {
        poseStack.pushPose();
        poseStack.translate(0,0,2);
        if(!fluidStack.isEmpty()) {
            String str1 = fluidStack.getDisplayName().getString();
            String str2 = fluidStack.getAmount() + "mB";
            int str1Length = font.width(str1);
            guiGraphics.drawString(font, str1, x + 2 + deltaF, y + 4 + height + hOffset, 0xFFFFFF);
            guiGraphics.drawString(font, str2, x + str1Length + 3 + deltaF, y + 4 + height + hOffset, 0xFFFFFF);
        } else {
            guiGraphics.drawString(font, IdyllicFoodDiary.TRANSLATOR.set("tooltip", "fluid_empty"), x + 2 + deltaF, y + 4 + height + hOffset, 0xFFFFFF);
        }
        poseStack.popPose();
    }

    /**
     * 渲染物品
     */
    public void drawItem(ItemStack stack, PoseStack poseStack, GuiGraphics guiGraphics, int x, int y) {
        poseStack.pushPose();
        ItemStackHandler inventory = ContainerUtil.getInventory(stack);
        ItemStack stackIn = inventory.getStackInSlot(0);
        if (!stackIn.isEmpty()) {
            poseStack.translate(0, 0, 2);
            float scale = 1f;
            poseStack.scale(1f / scale, 1f / scale, 1f);
            guiGraphics.renderItem(stackIn, (int) ((x + size * count - 16 + deltaF) * scale), (int) ((y + height + hOffset) * scale));
        }
        poseStack.popPose();
    }

    /**
     * 渲染gui
     */
    public void drawGUI(ItemStack stack, PoseStack poseStack, GuiGraphics guiGraphics, int x, int y) {
        poseStack.pushPose();
        if (stack.getItem() instanceof ITankItem) {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (id != null) {
                String cate = TextUtil.extractString(id.toString(), ":");
                String res = "textures/gui/" + cate + ".png";
                float scale = 1f;
                poseStack.scale(1f / scale, 1f / scale, 1f);
                guiGraphics.blit(new ResourceLocation(IdyllicFoodDiary.MOD_ID, res),
                        (int) (x * scale), (int) (y * scale) + height - 2,
                        0, 0, 88, 24);
            }
        }
        poseStack.popPose();
    }

    /**
     * 渲染液体
     */
    public void drawFluid(FluidStack fluidStack, GuiGraphics guiGraphics, IFluidHandlerItem stackTank, int x, int y) {
        if(!fluidStack.isEmpty()) {
            TextureAtlasSprite fluidStillSprite = TextureRenderUtil.getFluidTexture(fluidStack, TextureRenderUtil.FluidFlow.STILL);
            float[] color = TextureRenderUtil.getColorARGB(fluidStack);

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
                float percentage = FluidUtil.getScale(stackTank);

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
    public boolean canBeRendered() {
        ItemStack stack = tankTooltip.getItemStack();
        return stack.is(IMItemTags.TOOLTIP_FLUID_RENDER);
    }

    public static class TooltipComponent extends BaseTooltipComponent {
        public TooltipComponent(ItemStack itemStack) {
            super(itemStack);
        }
    }

}
