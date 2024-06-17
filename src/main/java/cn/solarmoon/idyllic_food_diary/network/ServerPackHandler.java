package cn.solarmoon.idyllic_food_diary.network;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill.GrillBlockEntity;
import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.Temp;
import cn.solarmoon.idyllic_food_diary.feature.water_pouring.WaterPouringUtil;
import cn.solarmoon.solarmoon_core.api.item_util.ItemStackUtil;
import cn.solarmoon.solarmoon_core.api.network.IServerPackHandler;
import cn.solarmoon.solarmoon_core.api.optional_recipe_item.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

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
                    RecipeSelectorData selector = held.getCapability(SolarCapabilities.ITEMSTACK_DATA).orElse(null).getRecipeSelectorData();
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
            case "2" -> {
                BlockEntity be = level.getBlockEntity(pos);
                if (be != null) {
                    IFluidHandler tank = FluidUtil.getTank(be);
                    if (tank != null) {
                        FluidStack updatedFluid = Temp.tick(fluidStack, level);
                        if (updatedFluid != null) {
                            tank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                            tank.fill(updatedFluid, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }

            }
        }
    }

}
