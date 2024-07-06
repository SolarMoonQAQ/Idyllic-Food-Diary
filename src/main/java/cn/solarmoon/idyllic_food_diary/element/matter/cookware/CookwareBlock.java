package cn.solarmoon.idyllic_food_diary.element.matter.cookware;

import cn.solarmoon.idyllic_food_diary.element.matter.stove.IBuiltInStove;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.block_util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import cn.solarmoon.solarmoon_core.api.phys.VecUtil;
import cn.solarmoon.solarmoon_core.api.tile.SyncedEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public abstract class CookwareBlock extends SyncedEntityBlock implements IHorizontalFacingBlock, ILitBlock {

    public CookwareBlock(Properties properties) {
        super(properties.lightLevel((state) -> state.getValue(LIT) ? 13 : 0));
        this.registerDefaultState(this.getStateDefinition().any().setValue(IBuiltInStove.NESTED_IN_STOVE, false));
    }

    public abstract VoxelShape getOriginShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    public abstract InteractionResult originUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult);

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // 当炉灶镶嵌模式时，增加点火功能，并且把使用范围限制在镶嵌槽内
        if (state.getBlock() instanceof IBuiltInStove bis && bis.isNestedInStove(state)) {
            Vec3 hit = hitResult.getLocation();

            if (litByHand(state, pos, level, player, hand)) {
                return InteractionResult.SUCCESS;
            }

            if (VecUtil.isInside(hit, pos, 2 / 16f, 10 / 16f, 2 / 16f, 14 / 16f, 16 / 16f, 14 / 16f, true)) {
                return originUse(state, level, pos, player, hand, hitResult);
            }

            return InteractionResult.PASS;
        }

        return originUse(state, level, pos, player, hand, hitResult);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        // 不保存stove镶嵌信息，打破带炉灶的厨具后自动拆分
        drops.stream()
                .filter(stack ->
                        state.getBlock().asItem() == stack.getItem()
                        && state.getValues().get(IBuiltInStove.NESTED_IN_STOVE) != null
                        && state.getValue(IBuiltInStove.NESTED_IN_STOVE)
                )
                .forEach(stack -> drops.add(new ItemStack(IMItems.STOVE.get())));
        return drops;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape origin = getOriginShape(state, level, pos, context);
        if (state.getBlock() instanceof IBuiltInStove bis && bis.isNestedInStove(state)) {
            return Shapes.or(origin.move(0, bis.getYOffset(), 0), bis.getShape(state));
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

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        getThis(player, level, pos, state, InteractionHand.MAIN_HAND, true);
        if (state.getBlock() instanceof IBuiltInStove bis && bis.isNestedInStove(state)) {
            BlockUtil.replaceBlockWithAllState(state, IMBlocks.STOVE.get().defaultBlockState(), level, pos);
        }
        super.attack(state, level, pos, player);
    }

}
