package cn.solarmoon.immersive_delight.network.handler;

import cn.solarmoon.immersive_delight.util.namespace.NETList;
import cn.solarmoon.solarmoon_core.network.IClientPackHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.List;


public class ClientPackHandler implements IClientPackHandler {

    @Override
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, CompoundTag tag, float f, String string, List<ItemStack> stacks, String message) {
        switch (message) {
            case NETList.SYNC_UP_STEP -> {
                player.setMaxUpStep(f);
            }
        }
    }

}
