package cn.solarmoon.idyllic_food_diary.common.block.base.entity_block;

import cn.solarmoon.idyllic_food_diary.common.block_entity.base.AbstractSteamerBlockEntity;
import cn.solarmoon.idyllic_food_diary.common.registry.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.common.registry.IMItems;
import cn.solarmoon.idyllic_food_diary.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.api.common.block.IStackBlock;
import cn.solarmoon.solarmoon_core.api.common.block.entity_block.BaseContainerEntityBlock;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSteamerEntityBlock extends BaseContainerEntityBlock implements IStackBlock {

    public static final BooleanProperty HAS_LID = BooleanProperty.create("covered");


    public AbstractSteamerEntityBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(HAS_LID, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        AbstractSteamerBlockEntity steamer = (AbstractSteamerBlockEntity) level.getBlockEntity(pos);
        if (steamer == null) return InteractionResult.PASS;
        ItemStack heldItem = player.getItemInHand(hand);
        int stackValue = state.getValue(STACK);
        //----------------------------------------堆叠蒸笼-------------------------------------------//
        // 同种蒸笼右键可以加入（包括其中物品）
        // 同时要求stack为1，且没有盖子（因为这里会设置新的盖子属性）
        if (heldItem.is(state.getBlock().asItem()) && stackValue < getMaxStack() && !steamer.hasLid()) {
            if (!player.isCreative()) {
                heldItem.shrink(1);
            }
            level.setBlock(pos, state
                    .setValue(STACK, stackValue + 1)
                    .setValue(HAS_LID, heldItem.getOrCreateTag().getBoolean(NBTList.HAS_LID)),
                    3); // 添加盖子
            ItemStackHandler invIn = ContainerUtil.getInventory(heldItem);
            steamer.set2ndInv(true);
            for (int i = 0; i < invIn.getSlots(); i++) {
                ItemStack stackIn = invIn.getStackInSlot(i);
                if (!stackIn.isEmpty()) {
                    steamer.getInventory().setStackInSlot(i + 4, stackIn);
                }
            }
            steamer.setChanged();
            level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        }
        //----------------------------------------盖子-------------------------------------------//
        double top = state.getValue(STACK) == 1 ? pos.getY() + 0.5 : pos.getY() + 1; // 顶部y坐标
        //没盖子且点击的是上表面就放入盖子
        if (heldItem.is(IMItems.STEAMER_LID.get()) && !steamer.hasLid() && hit.getLocation().y == top) {
            heldItem.shrink(1);
            level.setBlock(pos, state.setValue(HAS_LID, true), 3);
            level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        }
        //如果盖着盖子且点击的是上表面就取下盖子
        if (heldItem.isEmpty() && hit.getLocation().y == top && steamer.hasLid()) {
            LevelSummonUtil.addItemToInventory(player, new ItemStack(IMItems.STEAMER_LID.get()));
            level.setBlock(pos, state.setValue(HAS_LID, false), 3);
            level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        }
        //----------------------------------------获取蒸笼-------------------------------------------//
        if (heldItem.isEmpty() && player.isCrouching()) {
            if (state.getValue(STACK) == 2) {
                ItemStack get = steamer.getItem(2);
                player.setItemInHand(hand, get);
                steamer.set2ndInv(false); //这里必须手动设置一下以防过快的输入管道在tick之前就输入物品
                steamer.clearInv(2);
                steamer.setChanged();
                level.setBlock(pos, state.setValue(HAS_LID, false).setValue(STACK, 1), 3);
            } else if (state.getValue(STACK) == 1) {
                ItemStack get = steamer.getItem(1);
                player.setItemInHand(hand, get);
                level.removeBlock(pos, false);
            }
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1);
            return InteractionResult.SUCCESS;
        }
        //----------------------------------------存储物品-------------------------------------------//
        //匹配配方才能手动放置
        for (var recipe : level.getRecipeManager().getAllRecipesFor(IMRecipes.STEAMER.get())) {
            if (recipe.input().test(heldItem)) {
                if (putItem(steamer, player, hand, 1)) {
                    level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.PLAYERS);
                    steamer.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (takeItem(steamer, player, hand, 1)) {
            level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.PLAYERS);
            steamer.setChanged();
            return InteractionResult.SUCCESS;
        }
        //-----------------------------------------------------------------------------------//
        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        AbstractSteamerBlockEntity steamer = (AbstractSteamerBlockEntity) blockEntity;
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
        stack.getOrCreateTag().putInt(NBTList.STACK, state.getValue(STACK));
        stack.getOrCreateTag().putBoolean(NBTList.HAS_LID, state.getValue(HAS_LID));
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
        if (blockEntity instanceof AbstractSteamerBlockEntity steamer) {
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
                    .setValue(HAS_LID, tag.getBoolean(NBTList.HAS_LID))
                    .setValue(STACK, Math.max(tag.getInt(NBTList.STACK), 1));
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
        AbstractSteamerBlockEntity steamer = (AbstractSteamerBlockEntity) level.getBlockEntity(pos);
        if (steamer == null) return;
        if (steamer.getBase() != null && steamer.isTop() && steamer.getBase().isEvaporating()) {
            if (!steamer.hasLid() || (steamer.hasLid() && random.nextInt(10) < 2)) {
                level.addAlwaysVisibleParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + state.getValue(STACK) / 2D, pos.getZ() + 0.5, 0, 0.1, 0);
            }
        }
    }

}
