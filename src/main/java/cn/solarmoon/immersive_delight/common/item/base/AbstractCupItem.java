package cn.solarmoon.immersive_delight.common.item.base;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.client.Item_renderer.LittleCupItemRenderer;
import cn.solarmoon.immersive_delight.compat.create.util.PotionUtil;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.PotionEffect;
import cn.solarmoon.immersive_delight.data.fluid_foods.serializer.FluidFood;
import cn.solarmoon.immersive_delight.util.FarmerUtil;
import cn.solarmoon.solarmoon_core.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.client.ItemRenderer.IItemInventoryRendererProvider;
import cn.solarmoon.solarmoon_core.common.item.IContainerItem;
import cn.solarmoon.solarmoon_core.common.item.ITankItem;
import cn.solarmoon.solarmoon_core.util.FluidUtil;
import cn.solarmoon.solarmoon_core.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;


/**
 * 作为可饮用方块的对应物品的基本抽象类<br/>
 * 基本实现了绝大部分本模组杯子物品所需的功能，简单继承即可使用
 */
public abstract class AbstractCupItem extends BlockItem implements ITankItem, IContainerItem, IItemInventoryRendererProvider {

    public AbstractCupItem(Block block, Properties properties) {
        super(block, properties
                .food(new FoodProperties.Builder().build()).stacksTo(1)
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
        if(!remainFluid(stack)) return InteractionResultHolder.pass(stack);
        //如果foodValue有效，但是玩家还没到吃的条件，就不让用
        if(!canEat(stack, player)) return InteractionResultHolder.pass(stack);
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    /**
     * 结束使用时，消耗50液体并触发效果，如果不足50则光消耗不触发
     * 效果：药水、清空状态、着火
     * 联动 - 机械动力：能够喝取到机械动力的potion液体的药水效果
     */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        IFluidHandlerItem tankStack = FluidUtil.getTank(stack);
        int tankAmount = tankStack.getFluidInTank(0).getAmount();
        if(tankAmount >= getDrinkVolume(tankStack.getFluidInTank(0))) {
            FluidStack fluidStack = tankStack.getFluidInTank(0);
            FarmerUtil.commonDrink(fluidStack, level, entity, true);
            tankStack.drain(getDrinkVolume(tankStack.getFluidInTank(0)), IFluidHandler.FluidAction.EXECUTE);
        } else if (tankAmount > 0) tankStack.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        return stack;
    }

    /**
     * @return 设置每次喝掉的液体量（默认50）
     */
    public int getDrinkVolume(FluidStack fluidStack) {
        FluidFood fluidFood = FluidFood.getByFluid(fluidStack.getFluid());
        if (fluidFood != null) {
            return fluidFood.fluidAmount;
        }
        return 50;
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
            if(fluidEffect.clear) components.add(ImmersiveDelight.TRANSLATOR.set("tooltip", "fluid_effect_clear", ChatFormatting.BLUE));
            if(fluidEffect.extinguishing) components.add(ImmersiveDelight.TRANSLATOR.set("tooltip", "fluid_effect_extinguishing", ChatFormatting.BLUE));
            if(fluidEffect.fire > 0) components.add(ImmersiveDelight.TRANSLATOR.set("tooltip", "fluid_effect_fire", ChatFormatting.BLUE, fluidEffect.fire));
        }

        //Create药水效果显示
        FluidStack fluidStack = FluidUtil.getFluidStack(stack);
        PotionUtil.addPotionHoverText(fluidStack, components);
    }

    /**
     * 应用模型渲染
     */
    @Override
    public Supplier<BaseItemRenderer> getRendererFactory() {
        return LittleCupItemRenderer::new;
    }

    /**
     * 让物品根据所装溶液动态改变显示名称
     */
    @Override
    public Component getName(ItemStack stack) {
        FluidStack fluidStack = FluidUtil.getFluidStack(stack);
        int fluidAmount = fluidStack.getAmount();
        String fluid = fluidStack.getFluid().getFluidType().getDescription().getString();
        if(fluidAmount != 0) return Component.translatable(stack.getDescriptionId() + "_with_fluid", fluid);
        return super.getName(stack);
    }

}
