package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.network.NETList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMPacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class DumpVolumeScreen extends Screen {

    private EditBox editBox;
    private String lastValidInput = "";
    private final ItemStack kettle;

    public DumpVolumeScreen(ItemStack kettle) {
        super(Component.translatable(kettle.getDescriptionId()));
        this.kettle = kettle;
    }

    @Override
    protected void init() {
        int editBoxWidth = 150;
        int editBoxHeight = 20;

        // 使box位于屏幕中心
        int editBoxX = width / 2 - editBoxWidth / 2;
        int editBoxY = height / 2 - editBoxHeight / 2;

        this.editBox = new EditBox(font, editBoxX, editBoxY, editBoxWidth, editBoxHeight, Component.literal("233"));
        editBox.setMaxLength(200);
        lastValidInput = editBox.getValue();
        editBox.setResponder(this::checkIfNumeric);
        addWidget(editBox); // 添加组件
        setInitialFocus(editBox); // 设置开启焦点
    }

    private void checkIfNumeric(String newValue) {
        if (!newValue.matches("\\d*")) {
            editBox.setValue(lastValidInput);
        } else {
            lastValidInput = newValue;
        }
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifier) {
        if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
            setDone();
            return true;
        }
        return super.keyPressed(key, scanCode, modifier);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String s = editBox.getValue();
        this.init(minecraft, width, height);
        this.editBox.setValue(s);
    }

    @Override
    public boolean isPauseScreen() {
        return !isValid();
    }

    @Override
    public void tick() {
        editBox.tick();
        if (!isValid()) onClose();
        super.tick();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!isValid()) return;
        renderBackground(guiGraphics);
        editBox.render(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private boolean isValid() {
        return minecraft != null && minecraft.player != null;
    }

    private void onDone() {
        if (!isValid()) return;
        if (!editBox.getValue().isEmpty()) {
            int amount = Integer.parseInt(editBox.getValue());
            IMPacks.SERVER_PACK.getSender().i(amount).stack(kettle).send(NETList.SET_DUMP_VOLUME);
        } else {
            minecraft.player.displayClientMessage(IdyllicFoodDiary.TRANSLATOR.set("message", "dump_volume_empty"), true);
        }
    }

    public void setDone() {
        onDone();
        onClose();
    }

    public static void open(ItemStack heldItem) {
        Minecraft.getInstance().setScreen(new DumpVolumeScreen(heldItem));
    }

}
