package cn.solarmoon.immersive_delight.api.common.item.specific;

import cn.solarmoon.immersive_delight.api.common.item.BaseTankItem;
import cn.solarmoon.immersive_delight.api.util.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

/**
 * 具有从液体源接水的功能
 */
public abstract class AbstractKettleItem extends BaseTankItem {

    public AbstractKettleItem(Block block, int maxCapacity, Properties properties) {
        super(block, maxCapacity, properties);
    }

    /**
     * 通用的接水方法
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        IFluidHandlerItem stackTank = FluidHelper.getTank(heldStack);
        FluidStack fluidStack = FluidHelper.getFluidStack(heldStack);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos pos = blockhitresult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Direction direction = blockhitresult.getDirection();
        BlockPos pos1 = pos.relative(direction);
        if (!state.getFluidState().isEmpty()) {
            if(level.mayInteract(player, pos) && player.mayUseItemAt(pos1, direction, heldStack)) {
                if (state.getBlock() instanceof BucketPickup bp) {
                    if (getRemainFluid(heldStack) >= 1000
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

}
