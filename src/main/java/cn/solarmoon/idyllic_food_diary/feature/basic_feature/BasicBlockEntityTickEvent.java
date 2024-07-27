package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.api.AnimTicker;
import cn.solarmoon.idyllic_food_diary.feature.spice.ISpiceable;
import cn.solarmoon.solarmoon_core.api.event.BasicEntityBlockTickEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class BasicBlockEntityTickEvent {

    @SubscribeEvent
    public void onTick(BasicEntityBlockTickEvent event) {
        BlockEntity blockEntity = event.getBlockEntity();
        if (blockEntity instanceof ISpiceable spiceable) {
            if (spiceable.timeToResetSpices()) {
                var listCopy = List.copyOf(spiceable.getSpices());
                spiceable.clearSpices();
                if (!listCopy.isEmpty()) { // 只在变化时changed 因为IAnimateTicker会被重置为0很尴尬
                    blockEntity.setChanged();
                }
            }
        }
        AnimHelper.getMap(blockEntity).forEach((name, anim) -> anim.getTimer().tick());
    }

}
