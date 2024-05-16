package cn.solarmoon.idyllic_food_diary.core.network;

import cn.solarmoon.idyllic_food_diary.api.util.ParticleSpawner;
import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.util.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NETList;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.GrillBlockEntity;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.SoupServingRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMSounds;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.api.common.item.ITankItem;
import cn.solarmoon.solarmoon_core.api.network.IServerPackHandler;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import cn.solarmoon.solarmoon_core.api.util.ItemStackUtil;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNETList;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
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
    public void handle(ServerPlayer player, ServerLevel level, BlockPos pos, ItemStack stack, CompoundTag nbt, FluidStack fluidStack, float f, int[] ints, String string, List<ItemStack> stacks, List<Vec3> vec3List, String message) {
        switch (message) {
            //倒水技能
            case NETList.POURING -> {
                ItemStack itemStack = player.getMainHandItem(); // 必须使用主手而非发来的item，原因未知
                if(itemStack.getItem() instanceof ITankItem) {
                    IFluidHandlerItem tankStack = FluidUtil.getTank(itemStack);
                    FluidStack fluidStack0 = tankStack.getFluidInTank(0);
                    int fluidAmount = fluidStack0.getAmount();
                    ParticleSpawner.fluidPouring(fluidStack0, player, level);

                    //要产生效果至少要符合最低的标准量
                    int needAmount = 50;
                    List<SoupServingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.SOUP_SERVING.get());
                    for (var recipe : recipes) {
                        if (fluidStack0.getFluid().isSame(recipe.fluidToServe().getFluid())) {
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
                                FarmerUtil.commonDrink(fluidStack0, entity, false);
                            }
                        }
                        tankStack.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                        level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1F, 1F);
                        level.playSound(null, pos, IMSounds.PLAYER_SPILLING_WATER.get(), SoundSource.PLAYERS, 1F, 1F);
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
                if (blockEntity instanceof GrillBlockEntity grill) {
                    grill.setInventory(nbt);
                }
            }
        }
    }

}
