package cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks;

import cn.solarmoon.immersive_delight.client.IMSounds;
import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.entities.TankBlockEntity;
import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.entities.CupBlockEntity;
import cn.solarmoon.immersive_delight.common.items.abstract_items.CupItem;
import cn.solarmoon.immersive_delight.common.recipes.CupRecipe;
import cn.solarmoon.immersive_delight.compat.create.Create;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.immersive_delight.init.Config;
import cn.solarmoon.immersive_delight.network.serializer.ClientPackSerializer;
import cn.solarmoon.immersive_delight.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.*;


/**
 * 作为大部分可饮用方块（如各类杯子）的抽象类
 */
public abstract class CupEntityBlock extends BasicEntityBlock {

    protected CupEntityBlock(Properties properties) {
        super(properties
                .strength(0f)
                .noParticlesOnBreak()
                .noOcclusion()
        );
    }

    /**
     * 使得该类方块能够与液体桶类物品进行液体交互
     * 使得该类方块能够每几下被喝走一定数量的液体
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        TankBlockEntity tankEntity = (TankBlockEntity) blockEntity;
        ItemStack heldItem = player.getItemInHand(hand);
        if(tankEntity == null) return InteractionResult.FAIL;
        FluidTank tank = tankEntity.tank;

        //空手shift+右键快速拿杯子
        if(getThis(hand, heldItem, player, level, pos, state)) return InteractionResult.SUCCESS;

        //计数装置
        CompoundTag playerTag = CountingDevice.player(player, pos, level);
        if(canEat(tankEntity, player)) {
            Util.deBug("点击次数：" + CountingDevice.getCount(playerTag), level);
        }

        //自动检测是否是流体容器，进行液体存取
        if(loadFluid(heldItem, tankEntity, player, level, pos, hand)) return InteractionResult.SUCCESS;

        //存取任意单个物品
        if(storage(heldItem, tankEntity, player, hand)) return InteractionResult.SUCCESS;

        /*喝！
        默认每两下喝一下，消耗默认50，小于50则全喝完不触发任何效果
        喝时触发效果：声音、余下同item
         */
        else {
            int tankAmount = tank.getFluidAmount();
            if (tankAmount >= getDrinkVolume()) {
                //能吃却不能吃 不让用
                if(!canEat(blockEntity, player)) return InteractionResult.FAIL;
                if(CountingDevice.getCount(playerTag) >= getPressCount()) {
                    CupItem.commonUse(tank.getFluid(), level, player);
                    tank.drain(getDrinkVolume(), IFluidHandler.FluidAction.EXECUTE);
                    tankEntity.setChanged();
                    CountingDevice.resetCount(playerTag, -1);
                    Util.deBug("已有tag：" + blockEntity.getPersistentData(), level);
                    if(!level.isClientSide) level.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1F, 1F);
                }
                return InteractionResult.SUCCESS;
            } else if (tankAmount > 0) {
                if(CountingDevice.getCount(playerTag) >= getPressCount()) {
                    tank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                    tankEntity.setChanged();
                    CountingDevice.resetCount(playerTag, -1);
                    if(!level.isClientSide) level.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1F, 1F);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }

    /**
     * 把forge的流体装取逻辑单独提出来方便万一的新物品对use的修改
     * 总之就是控制液体容器能自动装取液体
     */
    public boolean loadFluid(ItemStack heldItem, TankBlockEntity tankEntity, Player player, Level level, BlockPos pos, InteractionHand hand) {
        FluidActionResult result;
        FluidTank tank = tankEntity.tank;
        //存入液体
        //player填null：不发出声音
        result = FluidUtil.tryEmptyContainer(heldItem, tank, Integer.MAX_VALUE, null, true);
        if(result.isSuccess()) {
            if(!player.isCreative()) player.setItemInHand(hand, result.getResult());
            tankEntity.setChanged();
            //水壶倒水声
            level.playSound(null, pos, IMSounds.PLAYER_POUR.get(), SoundSource.PLAYERS, 0.8F, 1F);
            return true;
        }

        //取出液体
        result = FluidUtil.tryFillContainer(heldItem, tank, Integer.MAX_VALUE, player, true);
        if(result.isSuccess()) {
            if(!player.isCreative()) player.setItemInHand(hand, result.getResult());
            IFluidHandlerItem tankStack = FluidHelper.getTank(player.getItemInHand(hand));
            tankEntity.setChanged();
            Create.playFillingSound(tankStack.getFluidInTank(0), level, pos, player);
            return true;
        }
        return false;
    }

    /**
     * 可放入和配方输入匹配的物品，如果有物品且空手就取出
     * 物品的nbt自然需要保存
     */
    public boolean storage(ItemStack heldItem, TankBlockEntity tankBlockEntity, Player player, InteractionHand hand) {
        if(tankBlockEntity instanceof CupBlockEntity cupBlockEntity) {
            if (!heldItem.isEmpty() && cupBlockEntity.getItem().isEmpty()) {
                List<CupRecipe> recipes = RecipeHelper.GetRecipes.CupRecipe(player.level());
                for (var recipe :recipes) {
                    Ingredient ingredient = recipe.getInputIngredient();
                    for (var stack : ingredient.getItems()) {
                        if (stack.is(heldItem.getItem())) {
                            player.setItemInHand(hand, cupBlockEntity.insertItem(heldItem));
                            return true;
                        }
                    }
                }
            } else if (!cupBlockEntity.getItem().isEmpty() && heldItem.isEmpty()) {
                player.setItemInHand(hand, cupBlockEntity.extractItem());
                return true;
            }
        }
        return false;
    }

    /**
     * 把方块快速拿到空手里
     */
    public boolean getThis(InteractionHand hand, ItemStack heldItem, Player player, Level level, BlockPos pos, BlockState state) {
        if(hand.equals(InteractionHand.MAIN_HAND) && heldItem.isEmpty() && player.isCrouching()) {
            ItemStack drop = getCloneItemStack(level, pos, state);
            level.removeBlock(pos, false);
            level.playSound(player, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1F, 1F);
            player.setItemInHand(hand, drop);
            return true;
        }
        return false;
    }

    /**
     * @return 设置每次喝掉的液体量（默认50）
     */
    public int getDrinkVolume() {
        return Config.drinkingConsumption.get();
    }

    /**
     * @return 设置需要右键多少下喝一下（默认2）
     */
    public int getPressCount() {
        return Config.useAmountForDrinking.get() - 1;
    }

    /**
     * 识别容器内的液体是否可被吃，且玩家能吃
     * 当然本身不能吃的话就直接通过
     */
    public boolean canEat(BlockEntity blockEntity, Player player) {
        FluidEffect fluidEffect = FluidEffect.getFluidEffectFromBlock(blockEntity);
        if(fluidEffect == null) return true;
        if(fluidEffect.canAlwaysDrink) return true;
        if(fluidEffect.getFoodValue().isValid()) return player.canEat(false);
        return true;
    }

    /**
     * shift才能放置
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if(!Objects.requireNonNull(context.getPlayer()).isCrouching()) return null;
        return super.getStateForPlacement(context);
    }

    /**
     * 使得放置物具有手上物品tank的读取
     * 以及item内的item的读取
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity == null) return;
        FluidHelper.setTank(blockEntity, stack);
        if(blockEntity instanceof CupBlockEntity cupBlockEntity) {
            cupBlockEntity.setInventory(stack);
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {

        if(blockEntity instanceof TankBlockEntity tankBlockEntity) {
            if(tankBlockEntity.ticks < 10) tankBlockEntity.ticks++;
        }

        //防止取出液体时液体值未在客户端同步 而 造成的 假右键操作
        //在客户端同步内容物防止虚空物品
        if(blockEntity instanceof TankBlockEntity tankBlockEntity) {
            List<ItemStack> stacks = new ArrayList<>();
            if(tankBlockEntity instanceof CupBlockEntity cupBlockEntity) {
                stacks.add(cupBlockEntity.getItem());
            }
            ClientPackSerializer.sendPacket(pos, stacks, tankBlockEntity.tank.getFluid(), 0, "updateCupBlock");
        }

        //配方
        if(blockEntity instanceof CupBlockEntity cupBlockEntity) {
            CupRecipe recipe = getCheckedRecipe(level, pos, blockEntity);
            if(recipe != null) {
                FluidStack fluidStack = FluidHelper.getFluidStack(blockEntity);
                cupBlockEntity.time++;
                Util.deBug("Time：" + cupBlockEntity.time, level);
                if (cupBlockEntity.time > recipe.getTime()) {
                    //选取最小容量输出
                    int amount = Math.min(fluidStack.getAmount(), recipe.getFluidAmount());
                    FluidStack fluidStackOut = new FluidStack(recipe.getOutputFluid(), amount);
                    cupBlockEntity.tank.setFluid(fluidStackOut);
                    cupBlockEntity.time = 0;
                    cupBlockEntity.extractItem();
                    cupBlockEntity.setChanged();
                }
            } else cupBlockEntity.time = 0;
        }

    }

    /**
     * 获取匹配的配方（杯中有物品阶段的匹配），不匹配返回null
     */
    public CupRecipe getCheckedRecipe(Level level, BlockPos pos, BlockEntity blockEntity) {
        if(blockEntity instanceof TankBlockEntity tankBlockEntity) {
            FluidStack fluidStack = tankBlockEntity.tank.getFluid();
            ItemStack stackIn = ItemStack.EMPTY;
            if (tankBlockEntity instanceof CupBlockEntity cupBlockEntity) {
                stackIn = cupBlockEntity.getItem();
            }
            List<CupRecipe> recipes = RecipeHelper.GetRecipes.CupRecipe(level);
            for (var recipe : recipes) {
                if (recipe.inputMatches(level, fluidStack, stackIn, pos)) {
                    return recipe;
                }
            }
        }
        return null;
    }

    /**
     * 使得复制物具有该实体的tank内容
     * 以及内容物
     */
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity == null) return stack;
        FluidHelper.setTank(stack, blockEntity);
        if(blockEntity instanceof CupBlockEntity cupBlockEntity) {
            ContainerHelper.setInventory(stack, cupBlockEntity);
        }
        return stack;
    }

    /**
     * 让战利品表的同类掉落物存在tank信息
     * 和内容物信息
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        ItemStack stack = new ItemStack(this);
        if(blockEntity != null) {
            FluidHelper.setTank(stack, blockEntity);
            if(blockEntity instanceof CupBlockEntity cupBlockEntity) {
                ContainerHelper.setInventory(stack, cupBlockEntity);
                return Collections.singletonList(stack);
            }
        }
        return super.getDrops(state, builder);
    }

    /**
     * 设定红石信号逻辑
     * 有物品则输出满格信号
     * 只有液体则按液体比例输出信号
     */
    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CupBlockEntity cup) {
            int itemSignal = cup.getItem().isEmpty() ? 0 : 15;
            int tankSignal = (int) (FluidHelper.getScale(cup.tank) * 15);
            return itemSignal == 15 ? itemSignal : tankSignal;
        }
        return 0;
    }

}
