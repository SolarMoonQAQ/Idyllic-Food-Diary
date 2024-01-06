package cn.solarmoon.immersive_delight.common.items.abstract_items;

import cn.solarmoon.immersive_delight.compat.create.Create;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FoodValue;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.PotionEffect;
import cn.solarmoon.immersive_delight.init.Config;
import cn.solarmoon.immersive_delight.network.serializer.ClientPackSerializer;
import cn.solarmoon.immersive_delight.util.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * 作为可饮用方块的对应物品的基本抽象类
 * 基本实现了绝大部分本模组杯子物品所需的功能，简单继承即可使用
 */
public abstract class DrinkableItem extends BlockItem {

    public DrinkableItem(Block block, Properties properties) {
        super(block, properties
                .food(new FoodProperties.Builder().build())
                .stacksTo(1)
        );
    }

    /**
     * 获取最大容量
     */
    public int getMaxCapacity() {
        return 250;
    }

    /**
     * 使用时长
     */
    @Override
    public int getUseDuration(ItemStack stack) {
        return 16;
    }

    /**
     * 使用动画
     */
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    /**
     * 使用时（需内有液体）开始使用（开始喝）
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        //不存有液体就返回
        if(!remainFluid(stack)) return InteractionResultHolder.fail(stack);
        //如果foodValue有效，但是玩家还没到吃的条件，就不让用
        if(!canEat(stack, player)) return InteractionResultHolder.fail(stack);
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    /**
     * 结束使用时，消耗50液体并触发效果，如果不足50则光消耗不触发
     * 效果：药水、清空状态、着火
     * 联动 - 机械动力：能够喝取到机械动力的potion液体的药水效果
     */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        int tankAmount = tankStack.getFluidInTank(0).getAmount();
        if(tankAmount >= getDrinkVolume()) {
            FluidStack fluidStack = tankStack.getFluidInTank(0);
            commonUse(fluidStack, level, entity);
            tankStack.drain(getDrinkVolume(), IFluidHandler.FluidAction.EXECUTE);
        } else if (tankAmount > 0) tankStack.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        return stack;
    }

    /**
     * 为了和block的use方法互通
     */
    public static void commonUse(FluidStack fluidStack, Level level, LivingEntity entity) {
        //根据液体id获取对应的FluidEffect数据
        String fluidId = fluidStack.getFluid().getFluidType().toString();
        String fluidTag = fluidStack.getTag() != null ? fluidStack.getTag().toString() : "empty";
        Util.deBug("喝下液体：" + fluidId + " " + fluidTag, level);

        //机械动力联动：根据药水tag获取药水效果
        Create.applyPotionFluidEffect(fluidTag, entity, level);

        FluidEffect fluidEffect = FluidEffect.get(fluidId);
        if(fluidEffect != null) {
            Util.deBug("数据已读取", level);
            //获取potion（因为多种药水效果并行所以为s）
            List<PotionEffect> potionEffects = fluidEffect.effects;
            //如果clear为true则先清空药水效果
            if(fluidEffect.clear) if(!level.isClientSide) entity.removeAllEffects();
            //如果fire不为0就点燃
            if(fluidEffect.fire > 0) if(!level.isClientSide) entity.setSecondsOnFire(fluidEffect.fire);
            //如果extinguishing为true就灭火
            if(fluidEffect.extinguishing) if(!level.isClientSide) entity.clearFire();
            //如果foodValue有作用就加饱食度
            if(fluidEffect.getFoodValue().isValid()) {
                if(entity instanceof Player player) {
                    if(player.canEat(false) || fluidEffect.canAlwaysDrink) {
                        FoodValue foodValue = fluidEffect.getFoodValue();
                        if(!level.isClientSide) {
                            player.getFoodData().eat(foodValue.hunger, foodValue.saturation);
                            level.playSound(null, player.getOnPos(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1F, 1F);
                        }
                    }
                }
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
                    Util.deBug("存在药水效果：" + mobEffectInstance, level);
                }
            }
        }

    }

    /**
     * @return 设置每次喝掉的液体量（默认50）
     */
    public int getDrinkVolume() {
        return Config.drinkingConsumption.get();
    }

    /**
     * 检查物品内液体是否大于0
     */
    public boolean remainFluid(ItemStack stack) {
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        return tankStack.getFluidInTank(0).getAmount() > 0;
    }

    /**
     * 识别容器内的液体是否可被吃，且玩家能吃
     */
    public boolean canEat(ItemStack stack, Player player) {
        FluidEffect fluidEffect = FluidEffect.getFluidEffectFromStack(stack);
        if(fluidEffect == null) return true;
        if(fluidEffect.canAlwaysDrink) return true;
        if(fluidEffect.getFoodValue().isValid()) return player.canEat(false);
        return true;
    }

    /**
     * 防止没有食物属性时对着方块不触发使用（需内有液体）
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        //存有液体,并且有食物属性的话要能吃才能使用
        if(remainFluid(stack) && canEat(stack, context.getPlayer())) {
            Player player = context.getPlayer();
            if (player == null) return InteractionResult.FAIL;
            if (!player.isCrouching()) player.startUsingItem(context.getHand());
        }
        return super.useOn(context);
    }

    /**
     * 将其赋予一个容器
     * 能够实现与各类液体容器交互
     */
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new FluidHandlerItemStack(stack, getMaxCapacity());
    }

    /**
     * 让杯子类物品显示其存储的液体信息
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        components.add(Component.literal(tankStack.getFluidInTank(0).getFluid().getFluidType().getDescription().getString() + tankStack.getFluidInTank(0).getAmount()));
    }

    /**
     * 让杯子根据所装溶液动态改变显示名称
     */
    @Override
    public Component getName(ItemStack stack) {
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        FluidStack fluidStack = tankStack.getFluidInTank(0);
        int fluidAmount = fluidStack.getAmount();
        String fluid = fluidStack.getFluid().getFluidType().getDescription().getString();
        if(fluidAmount != 0) return Component.translatable(stack.getDescriptionId() + "_with_fluid", fluid);
        return super.getName(stack);
    }

    /**
     * 同步流体信息，防止假右键操作
     */
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int tick, boolean p_41408_) {
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(stack);
        ClientPackSerializer.sendPacket(entity.getOnPos(), stacks, tankStack.getFluidInTank(0), 0, "updateCupItem");
    }

}
