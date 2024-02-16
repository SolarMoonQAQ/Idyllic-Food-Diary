package cn.solarmoon.immersive_delight.network.handler;

import cn.solarmoon.immersive_delight.common.registry.IMDamageTypes;
import cn.solarmoon.immersive_delight.common.registry.IMSounds;
import cn.solarmoon.immersive_delight.compat.create.util.PotionUtil;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.PotionEffect;
import cn.solarmoon.immersive_delight.data.tags.IMFluidTags;
import cn.solarmoon.immersive_delight.util.namespace.NETList;
import cn.solarmoon.solarmoon_core.common.item.IOptionalRecipeItem;
import cn.solarmoon.solarmoon_core.common.item.ITankItem;
import cn.solarmoon.solarmoon_core.network.IServerPackHandler;
import cn.solarmoon.solarmoon_core.registry.SolarCapabilities;
import cn.solarmoon.solarmoon_core.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.util.FluidUtil;
import cn.solarmoon.solarmoon_core.util.namespace.SolarNETList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.List;
import java.util.Random;

public class ServerPackHandler implements IServerPackHandler {

    @Override
    public void handle(ServerPlayer player, ServerLevel level, BlockPos pos, ItemStack stack, float f, String message) {
        switch (message) {

            //倒水技能
            case NETList.POURING -> {
                ItemStack itemStack = player.getMainHandItem();
                if(itemStack.getItem() instanceof ITankItem) {
                    IFluidHandlerItem tankStack = FluidUtil.getTank(itemStack);
                    FluidStack fluidStack = tankStack.getFluidInTank(0);
                    int fluidAmount = fluidStack.getAmount();
                    if(fluidAmount > 0) {
                        Vec3 lookVec = player.getLookAngle();
                        Vec3 inFrontVec = lookVec.scale(1);
                        Vec3 from = player.position().add(0, 1, 0).add(inFrontVec);
                        Vec3 to = from.add(lookVec.add(0, -2, 0).scale(2));
                        AABB aabb = new AABB(from, to).inflate(1, 1, 1);
                        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb);
                        for(var entity : entities) {
                            if(!entity.equals(player) || player.getXRot() < -30) {
                                // 众所周知液体穿墙是特性（点名表扬喷溅药水）
                                commonUse(fluidStack, level, entity);
                            }
                        }
                        tankStack.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                        if(!level.isClientSide) level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1F, 1F);
                        if(!level.isClientSide) level.playSound(null, pos, IMSounds.PLAYER_SPILLING_WATER.get(), SoundSource.PLAYERS, 1F, 1F);
                    }
                }
            }
            case SolarNETList.SYNC_INDEX -> {
                if (stack.getItem() instanceof IOptionalRecipeItem<?> op) {
                    CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA).getRecipeSelectorData().setIndex((int) f, op.getRecipeType());
                }
            }
            case SolarNETList.SYNC_RECIPE_INDEX -> {
                if (stack.getItem() instanceof IOptionalRecipeItem<?> op) {
                    CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA).getRecipeSelectorData().setRecipeIndex((int) f, op.getRecipeType());
                }
            }
        }
    }

    public static void commonUse(FluidStack fluidStack, Level level, LivingEntity entity) {
        //根据液体id获取对应的FluidEffect数据
        String fluidId = fluidStack.getFluid().getFluidType().toString();
        String fluidTag = fluidStack.getTag() != null ? fluidStack.getTag().toString() : "empty";

        //机械动力联动：根据药水tag获取药水效果
        PotionUtil.applyPotionFluidEffect(fluidTag, entity, level);

        FluidEffect fluidEffect = FluidEffect.get(fluidId);
        if(fluidEffect != null) {
            //获取potion（因为多种药水效果并行所以为s）
            List<PotionEffect> potionEffects = fluidEffect.effects;
            //如果clear为true则先清空药水效果
            if(fluidEffect.clear) if(!level.isClientSide) entity.removeAllEffects();
            //如果fire不为0就点燃
            if(fluidEffect.fire > 0) if(!level.isClientSide) entity.setSecondsOnFire(fluidEffect.fire);
            //如果extinguishing为true就灭火
            if(fluidEffect.extinguishing) if(!level.isClientSide) entity.clearFire();
            //泼热水造成伤害
            if(fluidStack.getFluid().defaultFluidState().is(IMFluidTags.HOT_FLUID)) {
                entity.hurt(IMDamageTypes.SCALD.getSimple(level), 4f);
                level.playSound(null, entity.getOnPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS);
            }
            //把每种药水效果按概率应用于玩家
            if(potionEffects != null) {
                for (var potion : potionEffects) {
                    MobEffectInstance mobEffectInstance = potion.getEffect();
                    double chance = potion.getChance();
                    Random random = new Random();
                    double rand = random.nextDouble();
                    if (rand <= chance) {
                        if(!level.isClientSide) entity.addEffect(mobEffectInstance);
                    }
                }
            }
        }

    }

}
