package cn.solarmoon.idyllic_food_diary.core.network;

import cn.solarmoon.idyllic_food_diary.core.common.block_entity.GrillBlockEntity;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NETList;
import cn.solarmoon.solarmoon_core.api.network.IClientPackHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;


public class ClientPackHandler implements IClientPackHandler {

    @Override
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, ItemStack stack, CompoundTag nbt, float f, int[] ints, String string, List<ItemStack> stacks, String message) {
        switch (message) {
            case NETList.SYNC_UP_STEP -> {
                player.setMaxUpStep(f);
            }
            case NETList.SYNC_BURN_TIME -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof GrillBlockEntity grill) {
                    grill.setBurnTime((int) f);
                }
            }
            case NETList.SYNC_BURN_TIME_SAVING -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof GrillBlockEntity grill) {
                    grill.saveBurnTime = (int) f;
                }
            }
            case NETList.SYNC_BOIL_TIME -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof IKettleRecipe kettle) {
                    kettle.setBoilTime(nbt.getInt(NBTList.BOIL_TICK));
                }
            }
        }
    }

}
