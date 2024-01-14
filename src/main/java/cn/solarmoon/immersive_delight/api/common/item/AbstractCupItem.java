package cn.solarmoon.immersive_delight.api.common.item;

import cn.solarmoon.immersive_delight.api.client.ItemRenderer.ICustomItemRendererProvider;
import cn.solarmoon.immersive_delight.api.client.ItemRenderer.ItemStackRenderer;
import cn.solarmoon.immersive_delight.client.ItemRenderer.LittleCupItemRenderer;
import cn.solarmoon.immersive_delight.compat.create.Create;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FoodValue;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.PotionEffect;
import cn.solarmoon.immersive_delight.init.Config;
import cn.solarmoon.immersive_delight.util.FluidHelper;
import cn.solarmoon.immersive_delight.util.TextUtil;
import cn.solarmoon.immersive_delight.util.Util;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;


/**
 * 作为可饮用方块的对应物品的基本抽象类
 * 基本实现了绝大部分本模组杯子物品所需的功能，简单继承即可使用
 */
public abstract class AbstractCupItem extends BaseTankItem implements ICustomItemRendererProvider {

    public AbstractCupItem(Block block, Properties properties) {
        super(block, properties
                .food(new FoodProperties.Builder().build())
        );
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
        IFluidHandlerItem tankStack = FluidHelper.getTank(stack);
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
     * 显示药水信息
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        //data效果显示
        FluidEffect fluidEffect = FluidEffect.getFluidEffectFromStack(stack);
        if(fluidEffect != null) {
            List<PotionEffect> potionEffects = fluidEffect.effects;
            if (potionEffects != null) {
                for (var effect : potionEffects) {
                    String name = effect.getEffect().getEffect().getDisplayName().getString();
                    int seconds = effect.duration;
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    String time = String.format("(%02d:%02d)", minutes, seconds);
                    int amplifier = effect.amplifier + 1;
                    String levelRoman = TextUtil.toRoman(amplifier);
                    Component base = Component.literal(name + " " + levelRoman + " " + time).withStyle(ChatFormatting.BLUE);
                    components.add(base);
                }
            }
            if(fluidEffect.clear) components.add(TextUtil.translation("tooltip", "fluid_effect_clear", ChatFormatting.BLUE));
            if(fluidEffect.extinguishing) components.add(TextUtil.translation("tooltip", "fluid_effect_extinguishing", ChatFormatting.BLUE));
            if(fluidEffect.fire > 0) components.add(TextUtil.translation("tooltip", "fluid_effect_fire", ChatFormatting.BLUE, fluidEffect.fire));
        }

        //Create药水效果显示
        FluidStack fluidStack = FluidHelper.getFluidStack(stack);
        String fluidTag = fluidStack.getTag() != null ? fluidStack.getTag().toString() : "empty";
        List<MobEffectInstance> effects = Create.getEffects(fluidTag);
        for (var effect : effects) {
            String name = effect.getEffect().getDisplayName().getString();
            int seconds = effect.getDuration();
            int minutes = seconds / 60;
            seconds = seconds % 60;
            String time = String.format("(%02d:%02d)", minutes, seconds);
            int amplifier = effect.getAmplifier() + 1;
            String levelRoman = TextUtil.toRoman(amplifier);
            Component base = Component.literal(name + " " + levelRoman + " " + time).withStyle(ChatFormatting.BLUE);
            components.add(base);
        }
    }

    /**
     * 应用模型渲染
     */
    @Override
    public Supplier<ItemStackRenderer> getRendererFactory() {
        return LittleCupItemRenderer::new;
    }

}
