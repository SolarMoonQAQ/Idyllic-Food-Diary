package cn.solarmoon.idyllic_food_diary.network;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.common.block_entity.base.AbstractGrillBlockEntity;
import cn.solarmoon.idyllic_food_diary.common.recipe.SoupServingRecipe;
import cn.solarmoon.idyllic_food_diary.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.common.registry.IMSounds;
import cn.solarmoon.idyllic_food_diary.util.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.util.namespace.NETList;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.api.common.item.ITankItem;
import cn.solarmoon.solarmoon_core.api.network.IServerPackHandler;
import cn.solarmoon.solarmoon_core.api.util.*;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNETList;
import cn.solarmoon.solarmoon_core.common.registry.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

public class ServerPackHandler implements IServerPackHandler {

    @Override
    public void handle(ServerPlayer player, ServerLevel level, BlockPos pos, ItemStack stack, CompoundTag nbt, float f, int[] ints, String string, List<ItemStack> stacks, String message) {
        switch (message) {
            //倒水技能
            case NETList.POURING -> {
                ItemStack itemStack = player.getMainHandItem();
                if(itemStack.getItem() instanceof ITankItem) {
                    IFluidHandlerItem tankStack = FluidUtil.getTank(itemStack);
                    FluidStack fluidStack = tankStack.getFluidInTank(0);
                    int fluidAmount = fluidStack.getAmount();

                    //要产生效果至少要符合最低的标准量
                    int needAmount = 50;
                    List<SoupServingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.SOUP_SERVING.get());
                    for (var recipe : recipes) {
                        if (fluidStack.getFluid().isSame(recipe.fluidToServe().getFluid())) {
                            needAmount = recipe.getAmountToServe();
                            break;
                        }
                    }

                    if(fluidAmount >= needAmount) {
                        Vec3 lookVec = player.getLookAngle();
                        Vec3 inFrontVec = lookVec.scale(1);
                        Vec3 from = player.position().add(0, 1, 0).add(inFrontVec);
                        Vec3 to = from.add(lookVec.add(0, -2, 0).scale(2));
                        AABB aabb = new AABB(from, to).inflate(1, 1, 1);
                        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb);
                        for(var entity : entities) {
                            if(!entity.equals(player) || player.getXRot() < -30) {
                                // 众所周知液体穿墙是特性（点名表扬喷溅药水）
                                FarmerUtil.commonDrink(fluidStack, level, entity, false);
                            }
                        }
                        tankStack.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                        if(!level.isClientSide) level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1F, 1F);
                        if(!level.isClientSide) level.playSound(null, pos, IMSounds.PLAYER_SPILLING_WATER.get(), SoundSource.PLAYERS, 1F, 1F);
                    }
                }
            }
            case SolarNETList.SYNC_RECIPE_INDEX -> {
                ItemStack held = ItemStackUtil.getItemInHand(player, stack.getItem());
                if (held != null) {
                    RecipeSelectorData selector = CapabilityUtil.getData(held, SolarCapabilities.ITEMSTACK_DATA).getRecipeSelectorData();
                    selector.deserializeNBT(nbt);
                    IdyllicFoodDiary.DEBUG.send(selector.serializeNBT().toString());
                }
            }
            case NETList.SYNC_SLOT_SET -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof AbstractGrillBlockEntity grill) {
                    grill.setInventory(nbt);
                }
            }
        }
    }

}
