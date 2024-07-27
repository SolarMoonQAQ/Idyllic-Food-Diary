package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.TooltipUtil;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTileItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * 具有从液体源接水的功能
 */
public abstract class AbstractKettleItem extends BlockItem implements ITankTileItem {

    public static final String DRAIN = "DrainAmount";

    public AbstractKettleItem(Block block, Properties properties) {
        super(block, properties.stacksTo(1));
    }

    public static Optional<Integer> getDrainAmount(ItemStack kettle) {
        CompoundTag tag = BlockItem.getBlockEntityData(kettle);
        if (tag != null && tag.contains(DRAIN)) {
            return Optional.of(tag.getInt(DRAIN));
        } else if (kettle.getItem() instanceof AbstractKettleItem) {
            return Optional.of(1000);
        }
        return Optional.empty();
    }

    public static void setDrainAmount(ItemStack kettle, int amount) {
        kettle.getOrCreateTagElement(BlockItem.BLOCK_ENTITY_TAG).putInt(DRAIN, amount);
    }

    /**
     * 通用的接水方法
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        FluidUtil.getFluidHandler(heldStack).ifPresent(tank -> {
            FluidStack fluidStack = tank.getFluidInTank(0);
            BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            BlockPos pos = blockhitresult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            Direction direction = blockhitresult.getDirection();
            BlockPos pos1 = pos.relative(direction);
            if (!state.getFluidState().isEmpty()) { // 这里包括上面都是类似水桶的接水功能
                if(level.mayInteract(player, pos) && player.mayUseItemAt(pos1, direction, heldStack)) {
                    if (state.getBlock() instanceof BucketPickup bp) {
                        if (getMaxCapacity() - fluidStack.getAmount() >= 1000
                                && fluidStack.getFluid().getFluidType().equals(state.getFluidState().getFluidType())
                                || fluidStack.isEmpty()) {
                            bp.pickupBlock(level, pos, state);
                            FluidStack fillStack = new FluidStack(state.getFluidState().getType(), 1000);
                            if (!player.isCreative()) tank.fill(fillStack, IFluidHandler.FluidAction.EXECUTE);
                            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS);
                            player.swing(hand);
                        }
                    }
                }
            }
        });
        return InteractionResultHolder.fail(heldStack);
    }

    /**
     * 优先把功能让位给接水
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (player == null) return super.useOn(context);
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
        AbstractKettleItem.getDrainAmount(stack).ifPresent(i -> {
            TooltipUtil.addShiftShowTooltip(components, () -> {
                components.add(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "kettle.dump",
                        Component.literal(i + "").withStyle(ChatFormatting.YELLOW)));
            });
        });
        super.appendHoverText(stack, level, components, flag);
    }

}
