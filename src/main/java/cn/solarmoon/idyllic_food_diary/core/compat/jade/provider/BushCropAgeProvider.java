package cn.solarmoon.idyllic_food_diary.core.compat.jade.provider;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.solarmoon_core.api.common.block.crop.BaseBushCropBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

/**
 * 这里可能可以给核心通用
 */
public class BushCropAgeProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    private final String configId;

    public BushCropAgeProvider(String configId) {
        this.configId = configId;
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BaseBushCropBlock bush = (BaseBushCropBlock) blockAccessor.getBlock();
        BlockState state = blockAccessor.getBlockState();
        addMaturityTooltip(iTooltip, (float) state.getValue(BaseBushCropBlock.AGE) / bush.getMaxAge());
    }

    private static void addMaturityTooltip(ITooltip tooltip, float growthValue) {
        growthValue *= 100.0F;
        if (growthValue < 100.0F)
            tooltip.add(Component.translatable("tooltip.jade.crop_growth", IThemeHelper.get().info(String.format("%.0f%%", growthValue))));
        else
            tooltip.add(Component.translatable("tooltip.jade.crop_growth", IThemeHelper.get().success(Component.translatable("tooltip.jade.crop_mature"))));
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(IdyllicFoodDiary.MOD_ID, configId);
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {}

}
