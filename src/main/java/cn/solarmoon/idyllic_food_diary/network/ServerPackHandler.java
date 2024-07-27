package cn.solarmoon.idyllic_food_diary.network;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.AbstractKettleItem;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.IStirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.water_pouring.WaterPouringUtil;
import cn.solarmoon.idyllic_food_diary.registry.common.IMSounds;
import cn.solarmoon.solarmoon_core.api.item_util.ItemStackUtil;
import cn.solarmoon.solarmoon_core.api.network.IServerPackHandler;
import cn.solarmoon.solarmoon_core.api.optional_recipe_item.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
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
                    held.getCapability(SolarCapabilities.ITEMSTACK_DATA).ifPresent(s -> {
                        RecipeSelectorData selector = s.getRecipeSelectorData();
                        selector.deserializeNBT(nbt);
                        IdyllicFoodDiary.DEBUG.send(selector.serializeNBT().toString());
                    });
                }
            }
            case NETList.SET_DUMP_VOLUME -> {
                ItemStack kettle = player.getItemInHand(InteractionHand.MAIN_HAND);
                AbstractKettleItem.setDrainAmount(kettle, i);
            }
        }
    }

}
