package cn.solarmoon.idyllic_food_diary.core.compat.jade.provider;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IKettleRecipe;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.TextUtil;
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
        if (blockAccessor.getBlockEntity() instanceof ITimeRecipeBlockEntity<?> t) {
            addByTime(t.getTime(), t.getRecipeTime(), iTooltip);
        }
        if (blockAccessor.getBlockEntity() instanceof IKettleRecipe k) {
            addByTime(k.getBoilTime(), k.getBoilRecipeTime(), iTooltip);
        }
    }

    private void addByTime(int timeNow, int recipeTime, ITooltip iTooltip) {
        int time = timeNow / 20;
        int needTime = recipeTime / 20;
        float scale = (float) timeNow / recipeTime;
        if (timeNow != 0) {
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
        return new ResourceLocation(IdyllicFoodDiary.MOD_ID, configId);
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {

    }

}
