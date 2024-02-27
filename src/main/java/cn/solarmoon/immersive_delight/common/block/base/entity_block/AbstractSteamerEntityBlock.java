package cn.solarmoon.immersive_delight.common.block.base.entity_block;

import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractSteamerBlockEntity;
import cn.solarmoon.immersive_delight.common.block_entity.inventoryHandler.SteamerInventory;
import cn.solarmoon.immersive_delight.common.recipe.SteamerRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.common.block.IStackBlock;
import cn.solarmoon.solarmoon_core.common.block.entity_block.BaseContainerEntityBlock;
import cn.solarmoon.solarmoon_core.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.util.FluidUtil;
import cn.solarmoon.solarmoon_core.util.LevelSummonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AbstractSteamerEntityBlock extends BaseContainerEntityBlock implements IStackBlock {

    public static final BooleanProperty HAS_LID = BooleanProperty.create("has_lid");


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
        //没盖子就放入盖子
        if (heldItem.is(IMItems.STEAMER_LID.get()) && !steamer.hasLid()) {
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

    protected static final VoxelShape[] SHAPE_BY_STACK = new VoxelShape[]{
            Block.box(0D, 0.0D, 0D, 16D, 8D, 16D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16D, 16.0D),
    };
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (state.getValue(STACK) == 1) {
            return SHAPE_BY_STACK[0];
        }
        return SHAPE_BY_STACK[1];
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        AbstractSteamerBlockEntity steamer = (AbstractSteamerBlockEntity) blockEntity;
        for (int i = 0; i < steamer.getInventory().getSlots(); i++) {
            SteamerRecipe recipe = steamer.getCheckedRecipe(i);
            if (recipe != null) {
                steamer.getRecipeTimes()[i] = recipe.time();
                steamer.getTimes()[i] = steamer.getTimes()[i] + 1;
                if (steamer.getTimes()[i] >= recipe.time()) {
                    steamer.getInventory().setStackInSlot(i, recipe.output().getDefaultInstance());
                    steamer.getTimes()[i] = 0;
                    steamer.getRecipeTimes()[i] = 0;
                    steamer.setChanged();
                }
            } else {
                steamer.getTimes()[i] = 0;
                steamer.getRecipeTimes()[i] = 0;
            }
        }

        //当stack为2时开放容量
        if (state.getValue(STACK) == 1) {
            steamer.set2ndInv(false);
        } else if (state.getValue(STACK) == 2) {
            steamer.set2ndInv(true);
        }

        //不让在连接中段放盖子
        if (steamer.isConnected() && steamer.hasLid() && !steamer.isTop()) {
            level.setBlock(pos, state.setValue(HAS_LID, false), 3);
            LevelSummonUtil.summonDrop(IMItems.STEAMER_LID.get(), level, pos, 1);
        }

    }

    /**
     * 让克隆物只能包含第一层的物品（因为stack属性不会给到itemStack上）
     */
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        AbstractSteamerBlockEntity steamer = (AbstractSteamerBlockEntity) level.getBlockEntity(pos);
        if (steamer != null) {
            SteamerInventory inv1 = steamer.getInv(1);
            ContainerUtil.setInventory(stack, inv1);
        }
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
            state = state.setValue(HAS_LID, context.getItemInHand().getOrCreateTag().getBoolean(NBTList.HAS_LID));
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HAS_LID);
    }

}
