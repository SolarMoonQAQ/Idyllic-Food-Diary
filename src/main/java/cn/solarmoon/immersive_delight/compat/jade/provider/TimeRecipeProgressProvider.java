package cn.solarmoon.immersive_delight.compat.jade.provider;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractSoupPotBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.iutor.ITimeRecipeBlockEntity;
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

public class TimeRecipeProgressProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    private final String configId;

    public TimeRecipeProgressProvider(String configId) {
        this.configId = configId;
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        ITimeRecipeBlockEntity<?> t = (ITimeRecipeBlockEntity<?>) blockAccessor.getBlockEntity();
        float scale = (float) t.getTime() / t.getRecipeTime();
        if (t.getTime() != 0) {
            int time = t.getTime() / 20;
            int needTime = t.getRecipeTime() / 20;
            IElementHelper ehp = iTooltip.getElementHelper();
            IElement progress = ehp.progress(
                    scale,
                    Component.literal(TextUtil.toMinuteFormat(time) + "/" + TextUtil.toMinuteFormat(needTime)).withStyle(ChatFormatting.WHITE),
                    new ProgressStyle(),
                    BoxStyle.DEFAULT,
                    true
            );
            iTooltip.add(progress);
        }

        if (blockAccessor.getBlockEntity() instanceof AbstractSoupPotBlockEntity soupPot) {
            if (soupPot.isBoiling()) {
                soupPotBoil(soupPot, iTooltip);
            }
        }
    }

    private void soupPotBoil(AbstractSoupPotBlockEntity soupPot, ITooltip iTooltip) {
        float scale = (float) soupPot.boilTime / soupPot.boilRecipeTime;
        int time = soupPot.boilTime / 20;
        int needTime = soupPot.boilRecipeTime / 20;
        IElementHelper ehp = iTooltip.getElementHelper();
        IElement progress = ehp.progress(
                scale,
                Component.literal(TextUtil.toMinuteFormat(time) + "/" + TextUtil.toMinuteFormat(needTime)).withStyle(ChatFormatting.WHITE),
                new ProgressStyle(),
                BoxStyle.DEFAULT,
                true
        );
        iTooltip.add(progress);
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(ImmersiveDelight.MOD_ID, configId);
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {

    }

}
