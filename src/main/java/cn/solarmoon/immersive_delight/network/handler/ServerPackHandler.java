package cn.solarmoon.immersive_delight.network.handler;

import cn.solarmoon.immersive_delight.api.util.CapabilityUtil;
import cn.solarmoon.immersive_delight.api.util.DamageUtil;
import cn.solarmoon.immersive_delight.api.util.FluidUtil;
import cn.solarmoon.immersive_delight.common.registry.IMSounds;
import cn.solarmoon.immersive_delight.api.common.item.BaseTankItem;
import cn.solarmoon.immersive_delight.common.registry.IMCapabilities;
import cn.solarmoon.immersive_delight.common.registry.IMDamageTypes;
import cn.solarmoon.immersive_delight.compat.create.util.PotionUtil;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.PotionEffect;
import cn.solarmoon.immersive_delight.api.network.serializer.ServerPackSerializer;
import cn.solarmoon.immersive_delight.data.tags.IMFluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.Random;

public class ServerPackHandler {
    public static void serverPackHandler(ServerPackSerializer packet, NetworkEvent.Context context) {

        //快乐的定义时间
        ServerPlayer player = context.getSender();
        Level level = null;
        if (player != null) {
            level = player.level();
        }
        BlockPos pos = packet.pos();
        ItemStack stack = packet.stack();
        Block block = null;
        if (level != null) {
            if (pos != null) {
                block = level.getBlockState(pos).getBlock();
            }
        }
        Block realBlock = packet.block();
        //处理
        switch (packet.message()) {
            //结束使用物品
            case "stopUse" -> {
                if(player == null) return;
                player.stopUsingItem();
            }
            //擀面结束的输出
            case "rollingOutput" -> {
                if(realBlock == null || level == null || pos == null || stack == null) return;
                level.destroyBlock(pos, false);
                level.setBlock(pos, realBlock.defaultBlockState(), 3);
            }
            case "rollingResults" -> {
                if(level == null || pos == null || stack == null) return;
                double maxY = level.getBlockState(pos).getShape(level, pos).max(Direction.Axis.Y);
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + maxY / 2, pos.getZ() + 0.5, stack);
                level.addFreshEntity(itemEntity);
            }
            //擀面杖收割技能
            case "sweepHarvest" -> {
                if(level == null || pos == null || stack == null) return;
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, block.asItem().getDefaultInstance());
                itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(0, 0.2, 0));
                level.destroyBlock(pos, false);
                for(int i = 0; i < 5; i++) {
                    level.playSound(null, pos, SoundEvents.ARROW_SHOOT, SoundSource.BLOCKS, 2.0f, (1.1f- (float) i /5));
                }
                level.addFreshEntity(itemEntity);
                stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(entity.getUsedItemHand()));
            }
            //倒水技能
            case "pouring" -> {
                if(player == null || pos == null) return;
                ItemStack itemStack = player.getMainHandItem();
                if(itemStack.getItem() instanceof BaseTankItem) {
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
            //出入水音效
            case "playSoundInWater" -> {
                if(pos == null || level == null) return;
                if(!level.isClientSide) level.playSound(null, pos, SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.PLAYERS, 0.6f, 1f);
            }
            case "playSoundOutWater" -> {
                if(pos == null || level == null) return;
                if(!level.isClientSide) level.playSound(null, pos, SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.PLAYERS, 0.8f, 1f);
            }
            case "updateIndex" -> {
                CapabilityUtil.getData(player, IMCapabilities.PLAYER_DATA).getRecipeSelectorData().setIndex(packet.i());
            }
            case "updateRecipeIndex" -> {
                CapabilityUtil.getData(player, IMCapabilities.PLAYER_DATA).getRecipeSelectorData().setRecipeIndex(packet.i());
            }
        }
    }

    /**
     * 为了和block的use方法互通
     */
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
                entity.hurt(DamageUtil.getSimpleDamageSource(level, IMDamageTypes.scald), 4f);
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
