package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IStackBlock;
import cn.solarmoon.solarmoon_core.api.tile.SyncedEntityBlock;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileItemContainerHelper;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SteamerBlock extends SyncedEntityBlock implements IStackBlock, IHorizontalFacingBlock {

    public static final BooleanProperty HAS_LID = BooleanProperty.create("covered");
    public static final String HAS_LID$ = "HasLid";
    public static final String STACK$ = "Stack";


    public SteamerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(HAS_LID, false));
    }

    public SteamerBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.BAMBOO).strength(2).noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(HAS_LID, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        SteamerBlockEntity steamer = (SteamerBlockEntity) level.getBlockEntity(pos);
        if (steamer == null) return InteractionResult.PASS;
        ItemStack heldItem = player.getItemInHand(hand);
        int stackValue = state.getValue(STACK);
        //----------------------------------------堆叠蒸笼-------------------------------------------//
        // 同种蒸笼右键可以加入（包括其中物品）
        // 同时要求stack为1，且没有盖子（因为这里会设置新的盖子属性），且点击为上表面
        if (heldItem.is(state.getBlock().asItem())
                && stackValue < getMaxStack()
                && !steamer.hasLid()
                && hit.getDirection().equals(Direction.UP)) {
            if (!player.isCreative()) {
                heldItem.shrink(1);
            }
            level.setBlock(pos, state
                    .setValue(STACK, stackValue + 1)
                    .setValue(HAS_LID, heldItem.getOrCreateTag().getBoolean(HAS_LID$)),
                    3); // 添加盖子
            TileItemContainerHelper.getInventory(heldItem).ifPresent(invIn -> {
                steamer.set2ndInv(true);
                for (int i = 0; i < invIn.getSlots(); i++) {
                    ItemStack stackIn = invIn.getStackInSlot(i);
                    if (!stackIn.isEmpty()) {
                        steamer.getInventory().setStackInSlot(i + 4, stackIn);
                    }
                }
                steamer.setChanged();
                level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.PLAYERS);
            });
            return InteractionResult.SUCCESS;
        }
        //----------------------------------------盖子-------------------------------------------//
        //没盖子且点击的是上表面就放入盖子
        if (heldItem.is(IMItems.STEAMER_LID.get()) && !steamer.hasLid() && hit.getDirection().equals(Direction.UP)) {
            heldItem.shrink(1);
            level.setBlock(pos, state.setValue(HAS_LID, true), 3);
            level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        }
        //如果盖着盖子且点击的是上表面就取下盖子
        if (heldItem.isEmpty() && hit.getDirection().equals(Direction.UP) && steamer.hasLid()) {
            LevelSummonUtil.addItemToInventory(player, new ItemStack(IMItems.STEAMER_LID.get()));
            level.setBlock(pos, state.setValue(HAS_LID, false), 3);
            level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        }
        //----------------------------------------存储物品-------------------------------------------//
        if (steamer.storage(player, hand, 1, 1)) {
            level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        }
        //-----------------------------------------------------------------------------------//
        return InteractionResult.PASS;
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        SteamerBlockEntity steamer = (SteamerBlockEntity) level.getBlockEntity(pos);
        ItemStack heldItem = player.getMainHandItem();
        if (steamer != null && heldItem.isEmpty() && player.isCrouching()) {
            if (state.getValue(STACK) == 2) {
                ItemStack get = steamer.getItem(2);
                player.setItemInHand(InteractionHand.MAIN_HAND, get);
                steamer.set2ndInv(false); //这里必须手动设置一下以防过快的输入管道在tick之前就输入物品
                steamer.clearInv(2);
                steamer.setChanged();
                level.setBlock(pos, state.setValue(HAS_LID, false).setValue(STACK, 1), 3);
            } else if (state.getValue(STACK) == 1) {
                ItemStack get = steamer.getItem(1);
                player.setItemInHand(InteractionHand.MAIN_HAND, get);
                level.removeBlock(pos, false);
            }
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1);
            return;
        }
        super.attack(state, level, pos, player);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        SteamerBlockEntity steamer = (SteamerBlockEntity) blockEntity;
        steamer.tryCook();
        steamer.tryOpen2ndInv();
        steamer.tryPreventLidSet();
    }

    /**
     * 让克隆物有盖、堆栈属性
     */
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        stack.getOrCreateTag().putInt(STACK$, state.getValue(STACK));
        stack.getOrCreateTag().putBoolean(HAS_LID$, state.getValue(HAS_LID));
        return stack;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.STEAMER.get();
    }

    @Override
    public int getMaxStack() {
        return 2;
    }

    /**
     * 把前四个物品和液体信息存入第一个stack掉落物，把后四个物品存入第二个stack。有盖子掉盖子
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        List<ItemStack> drops = new ArrayList<>();
        List<ItemStack> origin = super.getDrops(state, builder);
        int stackValue = state.getValue(STACK);
        if (blockEntity instanceof SteamerBlockEntity steamer) {
            if (steamer.hasLid()) {
                drops.add(new ItemStack(IMItems.STEAMER_LID.get()));
                origin.add(new ItemStack(IMItems.STEAMER_LID.get()));
            }
            if (stackValue == 2) {
                ItemStack drop1 = steamer.getItem(1);
                ItemStack drop2 = steamer.getItem(2);
                drops.add(drop1);
                drops.add(drop2);
                return drops;
            }
        }
        return origin;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            CompoundTag tag = context.getItemInHand().getOrCreateTag();
            state = state
                    .setValue(HAS_LID, tag.getBoolean(HAS_LID$))
                    .setValue(STACK, Math.max(tag.getInt(STACK$), 1));
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HAS_LID);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        SteamerBlockEntity steamer = (SteamerBlockEntity) level.getBlockEntity(pos);
        if (steamer == null) return;
        if (steamer.getBase() != null && steamer.isTop() && steamer.getBase().isEvaporating()) {
            if (!steamer.hasLid() || (steamer.hasLid() && random.nextInt(10) < 2)) {
                level.addAlwaysVisibleParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + state.getValue(STACK) / 2D, pos.getZ() + 0.5, 0, 0.1, 0);
            }
        }
    }

    protected static final VoxelShape[] SHAPE_BY_STACK = new VoxelShape[]{
            Block.box(1D, 0.0D, 1D, 15D, 8D, 15D),
            Block.box(1D, 0.0D, 1D, 15.0D, 16D, 15.0D),
    };
    protected static final VoxelShape[] SHAPE_ADD = new VoxelShape[]{
            Block.box(0.5D, 0D, 0.5D, 15.5D, 4D, 15.5D),
            Block.box(0.5D, 8D, 0.5D, 15.5D, 12D, 15.5D)
    };
    protected static final VoxelShape[] SHAPE_DISCARD = new VoxelShape[]{
            Block.box(2, 7, 2, 14, 8, 14),
            Block.box(2, 15, 2, 14, 16, 14)
    };
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape = Shapes.block();
        if (state.getValue(STACK) == 1) {
            shape = Shapes.joinUnoptimized(Shapes.or(SHAPE_BY_STACK[0], SHAPE_ADD[0]), SHAPE_DISCARD[0], BooleanOp.ONLY_FIRST);
        } else if (state.getValue(STACK) == 2) {
            shape = Shapes.joinUnoptimized(Shapes.or(SHAPE_BY_STACK[1], SHAPE_ADD), SHAPE_DISCARD[1], BooleanOp.ONLY_FIRST);
        }
        return shape;
    }

}
