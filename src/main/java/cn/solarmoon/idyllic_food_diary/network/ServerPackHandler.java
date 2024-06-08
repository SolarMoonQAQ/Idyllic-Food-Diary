package cn.solarmoon.idyllic_food_diary.network;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill.GrillBlockEntity;
import cn.solarmoon.idyllic_food_diary.feature.logic.water_pouring.WaterPouringUtil;
import cn.solarmoon.idyllic_food_diary.util.namespace.NETList;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.api.network.IServerPackHandler;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.api.util.ItemStackUtil;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ServerPackHandler implements IServerPackHandler {

    @Override
    public void handle(ServerPlayer player, ServerLevel level, BlockPos pos, ItemStack stack, CompoundTag nbt, FluidStack fluidStack, float f, int[] ints, String string, List<ItemStack> stacks, List<Vec3> vec3List, boolean flag, int i, String message) {
        switch (message) {
            //倒水技能
            case NETList.POURING -> {
                WaterPouringUtil.doPouring(player, level, pos);
            }
            case NETList.SYNC_RECIPE_INDEX -> {
                ItemStack held = ItemStackUtil.getItemInHand(player, stack.getItem());
                if (held != null) {
                    RecipeSelectorData selector = CapabilityUtil.getData(held, SolarCapabilities.ITEMSTACK_DATA).getRecipeSelectorData();
                    selector.deserializeNBT(nbt);
                    IdyllicFoodDiary.DEBUG.send(selector.serializeNBT().toString());
                }
            }
            case NETList.SYNC_SLOT_SET -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof GrillBlockEntity grill) {
                    grill.setInventory(nbt);
                }
            }
        }
    }

}
