package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle;

import cn.solarmoon.solarmoon_core.api.common.item.IContainerItem;
import cn.solarmoon.solarmoon_core.api.common.item.ITankItem;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 具有从液体源接水的功能
 */
public abstract class AbstractKettleItem extends BlockItem implements ITankItem, IContainerItem {

    public AbstractKettleItem(Block block, Properties properties) {
        super(block, properties.stacksTo(1));
    }

    /**
     * 通用的接水方法
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        IFluidHandlerItem stackTank = FluidUtil.getTank(heldStack);
        FluidStack fluidStack = FluidUtil.getFluidStack(heldStack);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos pos = blockhitresult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Direction direction = blockhitresult.getDirection();
        BlockPos pos1 = pos.relative(direction);
        if (!state.getFluidState().isEmpty()) { // 这里包括上面都是类似水桶的接水功能
            if(level.mayInteract(player, pos) && player.mayUseItemAt(pos1, direction, heldStack)) {
                if (state.getBlock() instanceof BucketPickup bp) {
                    if (getRemainCapability(heldStack) >= 1000
                            && fluidStack.getFluid().getFluidType().equals(state.getFluidState().getFluidType())
                            || fluidStack.isEmpty()) {
                        bp.pickupBlock(level, pos, state);
                        FluidStack fillStack = new FluidStack(state.getFluidState().getType(), 1000);
                        if (!player.isCreative()) stackTank.fill(fillStack, IFluidHandler.FluidAction.EXECUTE);
                        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
                        player.swing(hand);
                    }
                }
            }
        }
        return InteractionResultHolder.fail(heldStack);
    }

    /**
     * 优先把功能让位给接水
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos pos = blockhitresult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (!state.getFluidState().isEmpty()) {
            return InteractionResult.PASS;
        }
        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        components.add(1, Component.literal("")); //留空间给tooltip
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
