package cn.solarmoon.immersive_delight.compat.jade.provider;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractSteamerBaseBlockEntity;
import cn.solarmoon.solarmoon_core.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.ProgressStyle;

public class SteamerBaseProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    private final String configId;

    public SteamerBaseProvider(String configId) {
        this.configId = configId;
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        AbstractSteamerBaseBlockEntity steamerBase = (AbstractSteamerBaseBlockEntity) blockAccessor.getBlockEntity();
        if (steamerBase.isBoiling()) {
            float scale = (float) steamerBase.getTime() / steamerBase.getRecipeTime();
            int time = steamerBase.getTime() / 20;
            int needTime = steamerBase.getRecipeTime() / 20;
            IElementHelper ehp = iTooltip.getElementHelper();
            IElement progress = ehp.progress(
                    scale,
                    Component.literal(TextUtil.toMinuteFormat(time) + "/" + TextUtil.toMinuteFormat(needTime)).withStyle(ChatFormatting.WHITE),
                    new ProgressStyle(),
                    BoxStyle.DEFAULT,
                    true
            );
            iTooltip.add(progress);
        } else if (steamerBase.isEvaporating()) {
            iTooltip.add(ImmersiveDelight.TRANSLATOR.set("jade", "steamer_base.evaporating", 5));
        }
        iTooltip.add(ImmersiveDelight.TRANSLATOR.set("jade", "steamer_base.working"));
        if (steamerBase.isWorking()) iTooltip.append(Component.literal("✔").withStyle(ChatFormatting.GREEN));
        else if (!steamerBase.isWorking()) iTooltip.append(Component.literal("✖").withStyle(ChatFormatting.RED));
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {

    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(ImmersiveDelight.MOD_ID, configId);
    }

}
