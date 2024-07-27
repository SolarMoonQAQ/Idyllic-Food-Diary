package cn.solarmoon.idyllic_food_diary.element.matter.cookware;

import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.IBuiltInStove;
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.InlaidStoveBlock;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.block_util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import cn.solarmoon.solarmoon_core.api.phys.VecUtil;
import cn.solarmoon.solarmoon_core.api.tile.SyncedEntityBlock;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.ArrayList;
import java.util.List;

public abstract class CookwareBlock extends SyncedEntityBlock implements IHorizontalFacingBlock {

    public CookwareBlock(Properties properties) {
        super(properties.lightLevel(ILitBlock::getCommonLightLevel));
        if (this instanceof IBuiltInStove) {
            this.registerDefaultState(this.getStateDefinition().any().setValue(IBuiltInStove.NESTED_IN_STOVE, false));
        }
    }

    public abstract InteractionResult originUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult);

    public abstract VoxelShape getOriginShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);

        // 当炉灶镶嵌模式时，增加点火功能，并且把使用范围限制在镶嵌槽内
        if (state.getBlock() instanceof IBuiltInStove bis && bis.isNestedInStove(state)) {
            // 锅盖啥的直接放
            if (heldItem.is(IMItems.STOVE_LID.get())) return InteractionResult.FAIL;

            if (InlaidStoveBlock.isClickInStove(hitResult, pos)) {
                return originUse(state, level, pos, player, hand, hitResult);
            }

            if (ILitBlock.controlLitByHand(state, pos, level, player, hand)) {
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        }

        return originUse(state, level, pos, player, hand, hitResult);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return dropSync() ? super.getCloneItemStack(state, target, level, pos, player) : new ItemStack(this);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = dropSync() ? super.getDrops(state, builder) : new ArrayList<>();
        // 不保存容器信息时，掉落容器内物品
        if (!dropSync()) {
            BlockEntity be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if (be != null) {
                be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(d -> {
                    drops.addAll(ItemHandlerUtil.getStacks(d));
                    drops.add(new ItemStack(this));
                });
            }
        }
        // 如果可镶嵌在灶台内，不保存stove镶嵌信息，打破带炉灶的厨具后自动拆分
        drops.stream()
                .filter(stack ->
                        state.getBlock().asItem() == stack.getItem()
                        && state.getValues().get(IBuiltInStove.NESTED_IN_STOVE) != null
                        && state.getValue(IBuiltInStove.NESTED_IN_STOVE)
                )
                .forEach(stack -> drops.add(new ItemStack(IMItems.INLAID_STOVE.get())));
        return drops;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape origin = getOriginShape(state, level, pos, context);
        if (state.getBlock() instanceof IBuiltInStove bis && bis.isNestedInStove(state)) {
            return Shapes.or(origin.move(0, bis.getYOffset(state), 0), bis.getShape(state));
        }
        return origin;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        if (this instanceof IBuiltInStove) {
            builder.add(IBuiltInStove.NESTED_IN_STOVE);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        if (this instanceof IBuiltInStove) {
            return RenderShape.INVISIBLE;
        }
        return super.getRenderShape(state);
    }

    public boolean dropSync() {
        return true;
    }

    public boolean canGet() {
        return true;
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (canGet()) {
            BlockPos rP = pos;
            if (this instanceof IBedPartBlock b) {
                rP = b.getFootPos(state, pos);
            }
            if (getThis(player, level, rP, state, InteractionHand.MAIN_HAND, true, true)) {
                if (state.getBlock() instanceof IBuiltInStove bis && bis.isNestedInStove(state)) {
                    BlockUtil.replaceBlockWithAllState(state, IMBlocks.INLAID_STOVE.get().defaultBlockState(), level, pos);
                }
            }
        }
        super.attack(state, level, pos, player);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (state.getBlock() instanceof IBuiltInStove bis && bis.isNestedInStove(state) && state.getValue(ILitBlock.LIT)) {
            InlaidStoveBlock.makeFire(state, level, pos, random);
        }
    }

}
