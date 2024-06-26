package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup;

import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.TeaBrewingUtil;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTileItem;
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
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;


/**
 * 作为可饮用方块的对应物品的基本抽象类<br/>
 * 基本实现了绝大部分本模组杯子物品所需的功能，简单继承即可使用
 */
public abstract class AbstractCupItem extends BlockItem implements ITankTileItem {

    public AbstractCupItem(Block block, Properties properties) {
        super(block, properties
                .food(new FoodProperties.Builder().build()).stacksTo(1)
        );
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 16;
    }

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
        return FluidUtil.getFluidHandler(stack).map(tank -> {
            FluidStack fluidStack = tank.getFluidInTank(0);
            //如果foodValue有效，但是玩家还没到吃的条件，就不让用
            if(!TeaBrewingUtil.canEat(fluidStack, player)) return InteractionResultHolder.pass(stack);
            return ItemUtils.startUsingInstantly(level, player, hand);
        }).orElse(InteractionResultHolder.pass(stack));
    }

    /**
     * 结束使用时，消耗50液体并触发效果，如果不足50则光消耗不触发
     * 效果：药水、清空状态、着火
     * 联动 - 机械动力：能够喝取到机械动力的potion液体的药水效果
     */
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        return FluidUtil.getFluidHandler(stack).map(tankStack -> {
            int tankAmount = tankStack.getFluidInTank(0).getAmount();
            if(tankAmount >= TeaBrewingUtil.getDrinkVolume(level, tankStack.getFluidInTank(0))) {
                FluidStack fluidStack = tankStack.getFluidInTank(0);
                TeaBrewingUtil.commonDrink(fluidStack, entity, true);
                tankStack.drain(TeaBrewingUtil.getDrinkVolume(level, tankStack.getFluidInTank(0)), IFluidHandler.FluidAction.EXECUTE);
            } else if (tankAmount > 0) tankStack.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
            return stack;
        }).orElse(stack);
    }

    /**
     * 防止没有食物属性时对着方块不触发使用（需内有液体）
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        return FluidUtil.getFluidHandler(stack).map(tank -> {
            FluidStack fluidStack = tank.getFluidInTank(0);
            //存有液体,并且有食物属性的话要能吃才能使用
            if(TeaBrewingUtil.canEat(fluidStack, context.getPlayer())) {
                Player player = context.getPlayer();
                if (player == null) return InteractionResult.FAIL;
                if (!player.isCrouching()) player.startUsingItem(context.getHand());
            }
            return super.useOn(context);
        }).orElse(InteractionResult.FAIL);
    }

    /**
     * 显示药水信息
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        FluidStack fluidStack = FluidUtil.getFluidHandler(stack).map(tank -> tank.getFluidInTank(0)).orElse(FluidStack.EMPTY);
        //data效果显示
        TeaBrewingUtil.showTeaIngredientTooltip(fluidStack, components);
    }

}
