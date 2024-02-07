package cn.solarmoon.immersive_delight.compat.jade.provider;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseTankBlockEntity;
import cn.solarmoon.immersive_delight.api.util.TextUtil;
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

public class TankClassRecipeProgressProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    private final String configId;

    public TankClassRecipeProgressProvider(String configId) {
        this.configId = configId;
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BaseTankBlockEntity t = (BaseTankBlockEntity) blockAccessor.getBlockEntity();
        float scale = (float) t.time / t.recipeTime;
        if (t.time != 0) {
            int time = t.time / 20;
            int needTime = t.recipeTime / 20;
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
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(ImmersiveDelight.MOD_ID, configId);
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {

    }

}
