package cn.solarmoon.idyllic_food_diary.core.compat.jade.provider;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.SteamerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IIndividualTimeRecipeBlockEntity;
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
import snownee.jade.api.ui.IElementHelper;

public class IndividualTimeRecipeProgressProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    private final String configId;

    public IndividualTimeRecipeProgressProvider(String configId) {
        this.configId = configId;
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        IIndividualTimeRecipeBlockEntity<?> t = (IIndividualTimeRecipeBlockEntity<?>) blockAccessor.getBlockEntity();
        int n = 0;
        for (int i = 0; i < t.getTimes().length; i ++) {
            if (t.getTimes()[i] != 0) {
                int time = t.getTimes()[i] / 20;
                int needTime = t.getRecipeTimes()[i] / 20;
                IElementHelper ehp = iTooltip.getElementHelper();
                if (blockAccessor.getBlockEntity() instanceof IContainerBlockEntity c) {
                    if (n % 2 == 0) {
                        iTooltip.add(ehp.smallItem(c.getInventory().getStackInSlot(i)));
                    } else {
                        iTooltip.append(iTooltip.size() - 1, ehp.smallItem(c.getInventory().getStackInSlot(i)));
                    }
                }
                n++;
                iTooltip.append(Component.literal(TextUtil.toMinuteFormat(time) + "/" + TextUtil.toMinuteFormat(needTime)).withStyle(ChatFormatting.WHITE));
            }
        }

        if (blockAccessor.getBlockEntity() instanceof SteamerBlockEntity steamer) {
            iTooltip.add(IdyllicFoodDiary.TRANSLATOR.set("jade", "steamer_base.working"));
            if (steamer.canWork()) iTooltip.append(Component.literal("✔").withStyle(ChatFormatting.GREEN));
            else if (!steamer.canWork()) iTooltip.append(Component.literal("✖").withStyle(ChatFormatting.RED));
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
