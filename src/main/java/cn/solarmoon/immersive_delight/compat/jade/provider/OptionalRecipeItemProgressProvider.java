package cn.solarmoon.immersive_delight.compat.jade.provider;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.util.TextUtil;
import cn.solarmoon.immersive_delight.compat.jade.impl.IJadeRecipeProgressItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.Accessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.callback.JadeTooltipCollectedCallback;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.config.PluginConfig;
import snownee.jade.impl.ui.ProgressStyle;

public enum OptionalRecipeItemProgressProvider implements JadeTooltipCollectedCallback {
    INSTANCE;

    @Override
    public void onTooltipCollected(ITooltip iTooltip, Accessor<?> accessor) {
        ResourceLocation pinId = new ResourceLocation(ImmersiveDelight.MOD_ID, "rolling_pin");
        if (PluginConfig.INSTANCE.get(pinId)) {
            Player player = accessor.getPlayer();
            ItemStack stack = player.getUseItem();
            if (stack.getItem() instanceof IJadeRecipeProgressItem rp) {
                float scale = (float) player.getTicksUsingItem() / rp.getRecipeTime();
                if (rp.getRecipeTime() != 0) {
                    int time = player.getTicksUsingItem() / 20;
                    int needTime = rp.getRecipeTime() / 20;
                    IElementHelper ehp = iTooltip.getElementHelper();
                    IElement progress = ehp.progress(
                            scale,
                            Component.literal(TextUtil.toMinuteFormat(time) + "/" + TextUtil.toMinuteFormat(needTime)).withStyle(ChatFormatting.WHITE),
                            new ProgressStyle().color(0xFFDEAD),
                            BoxStyle.DEFAULT,
                            true
                    );
                    iTooltip.add(progress);
                }
            }
        }
    }
}
