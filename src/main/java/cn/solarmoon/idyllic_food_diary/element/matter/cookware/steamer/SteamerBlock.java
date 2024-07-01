package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.registry.client.IMParticles;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.tile.SyncedEntityBlock;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import cn.solarmoon.solarmoon_core.api.util.HitResultUtil;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

public class SteamerBlock extends SyncedEntityBlock implements IHorizontalFacingBlock {


    public SteamerBlock(Properties properties) {
        super(properties);
    }

    public SteamerBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.BAMBOO)
                .strength(2)
                .noOcclusion()
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        SteamerBlockEntity steamer = (SteamerBlockEntity) level.getBlockEntity(pos);
        if (steamer == null) return InteractionResult.FAIL;

        // 放上盖子
        if (hit.getDirection() == Direction.UP && !steamer.hasLid() && heldItem.getItem() instanceof SteamerLidItem) {
            steamer.setLid(heldItem.copyWithCount(1));
            if (!player.isCreative()) heldItem.shrink(1);
            level.playSound(null, pos, SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }

        // 取回盖子
        if (hit.getDirection() == Direction.UP && heldItem.isEmpty()) {
            if (steamer.returnLid(player)) {
                level.playSound(null, pos, SoundEvents.BAMBOO_WOOD_HIT, SoundSource.BLOCKS);
                return InteractionResult.SUCCESS;
            }
        }

        // 堆叠蒸笼
        if (hit.getDirection() == Direction.UP && steamer.grow(heldItem)) {
            if (!player.isCreative()) heldItem.shrink(1);
            level.playSound(null, pos, SoundEvents.BAMBOO_PLACE, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }

        // 插入物品
        double dy = hit.getLocation().y - pos.getY();
        int layer = (int) (dy * 16 / steamer.getMaxStack());
        if (hit.getDirection() == Direction.UP && !steamer.hasLid()) layer--; // 防止顶端算下一层，同时如果有盖子的话阻止从上方加入物品
        if (layer < steamer.getInvList().size() && hand == InteractionHand.MAIN_HAND
                && !(heldItem.getItem() instanceof SteamerItem) // 蒸笼就别放进去了，直接叠上去多好
                && ItemHandlerUtil.storage(steamer.getInvList().get(layer), player, hand, 1, 1)) {
            level.playSound(null, pos, SoundEvents.BAMBOO_HIT, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        super.attack(state, level, pos, player);
        SteamerBlockEntity steamer = (SteamerBlockEntity) level.getBlockEntity(pos);
        if (steamer != null) {
            ItemStack main = player.getMainHandItem();

            steamer.shrink(main).ifPresent(copy -> {
                ItemStack s = new ItemStack(IMItems.STEAMER.get());
                copy.saveToItem(s);
                player.setItemInHand(InteractionHand.MAIN_HAND, s);
            });
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        SteamerBlockEntity steamer = (SteamerBlockEntity) level.getBlockEntity(pos);
        if (steamer != null && steamer.getBase() != null && steamer.isTop()) {
            float xInRange = pos.getX() + 2 / 16f + random.nextFloat() * 12 / 16;
            float h = pos.getY() + 4 / 16f * steamer.getStack() - 1 / 16f;
            float zInRange = pos.getZ() + 2 / 16f + random.nextFloat() * 12 / 16;
            if (steamer.getBase().isValidForSteamer()) {
                // 有盖子降低蒸汽外流几率
                if (random.nextInt(10) < (steamer.hasLid() ? 3 : 10)) {
                    level.addAlwaysVisibleParticle(IMParticles.CRASHLESS_CLOUD.get(), xInRange, h, zInRange, 0, 0.1, 0);
                }
            }
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        SteamerBlockEntity steamer = (SteamerBlockEntity) blockEntity;
        steamer.trySteam();
        steamer.tryPreventLidSet();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext collisionContext) {
        SteamerBlockEntity steamer = (SteamerBlockEntity) level.getBlockEntity(pos);
        if (steamer == null) return Shapes.block();
        int stack = steamer.getStack();
        VoxelShape shape = Block.box(0, 0, 0, 16, stack * 16d / steamer.getMaxStack(), 16);
        return shape;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.STEAMER.get();
    }

}
