package cn.solarmoon.immersive_delight.common.blocks.abstract_blocks;

import cn.solarmoon.immersive_delight.common.blocks.abstract_blocks.entities.TankBlockEntity;
import cn.solarmoon.immersive_delight.common.items.abstract_items.DrinkableItem;
import cn.solarmoon.immersive_delight.compat.create.Create;
import cn.solarmoon.immersive_delight.data.FluidEffect;
import cn.solarmoon.immersive_delight.init.Config;
import cn.solarmoon.immersive_delight.network.serializer.ClientPackSerializer;
import cn.solarmoon.immersive_delight.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * 作为大部分可饮用方块（如各类杯子）的抽象类
 */
public abstract class DrinkableEntityBlock extends BasicEntityBlock {

    protected DrinkableEntityBlock(Properties properties) {
        super(properties);
    }

    private long lastPressTime;
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

        //计数装置
        CompoundTag tag = blockEntity.getPersistentData();
        lastPressTime = level.getGameTime();
        if(!tag.contains("PressCount")) tag.putInt("PressCount", 0);
        int lastCount = tag.getInt("PressCount");
        tag.putInt("PressCount", lastCount + 1);
        if(canEat(tankEntity, player)) {
            Util.deBug(tag.getInt("PressCount") + "", level);
        }

        //自动检测是否是流体容器，进行液体存取
        if(loadFluid(heldItem, tankEntity, player, level, pos, hand)) return InteractionResult.SUCCESS;

        /*喝！
        默认每两下喝一下，消耗默认50，小于50则全喝完不触发任何效果
        喝时触发效果：声音、余下同item
         */
        else {
            int tankAmount = tank.getFluidAmount();
            if (tankAmount >= getDrinkVolume()) {
                //能吃却不能吃不让用
                if(!canEat(blockEntity, player)) return InteractionResult.FAIL;
                if(tag.getInt("PressCount") >= getPressCount()) {
                    DrinkableItem.commonUse(tank.getFluid(), level, player);
                    tank.drain(getDrinkVolume(), IFluidHandler.FluidAction.EXECUTE);
                    tankEntity.setChanged();
                    tag.putInt("PressCount", 0);
                    Util.deBug("已有tag：" + blockEntity.getPersistentData(), level);
                    level.playSound(player, pos, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1F, 1F);
                }
                return InteractionResult.SUCCESS;
            } else if (tankAmount > 0) {
                if(tag.getInt("PressCount") >= getPressCount()) {
                    tank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                    tankEntity.setChanged();
                    tag.putInt("PressCount", 0);
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
        result = FluidUtil.tryEmptyContainer(heldItem, tank, Integer.MAX_VALUE, player, true);
        if(result.isSuccess()) {
            if(!player.isCreative()) player.setItemInHand(hand, result.getResult());
            tankEntity.setChanged();
            Create.playPouringSound(tank, level, pos);
            return true;
        }

        //取出液体
        result = FluidUtil.tryFillContainer(heldItem, tank, Integer.MAX_VALUE, player, true);
        if(result.isSuccess()) {
            if(!player.isCreative()) player.setItemInHand(hand, result.getResult());
            IFluidHandlerItem tankStack = player.getItemInHand(hand).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
            tankEntity.setChanged();
            Create.playFillingSound(tankStack.getFluidInTank(0), level, pos);
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
        return Config.useAmountForDrinking.get();
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
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity == null) return;
        setTank(blockEntity, stack);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, getBlockEntityType(), this::tick);
    }

    /**
     * 决定ticker所对应的实体类型（具体到注册类）
     */
    public BlockEntityType<?> getBlockEntityType() {
        return null;
    }

    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        //防止取出液体时液体值未在客户端同步 而 造成的 假右键操作
        if (blockEntity instanceof TankBlockEntity tankBlockEntity) {
            ClientPackSerializer.sendPacket(pos, new ArrayList<>(), tankBlockEntity.tank.getFluid(), tankBlockEntity.tank.getFluidAmount(), "updateCupTank");
        }
        if (level.getGameTime() - lastPressTime > 5) blockEntity.getPersistentData().putInt("PressCount", 0);
    }

    /**
     * 用于强制设置物品里的液体（前者是被设置的，后者是设置的内容）
     */
    public void setTank(ItemStack stack, BlockEntity blockEntity) {
        //把blockEntity的tank注入item
        IFluidHandler blockTankEntity = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        FluidStack fluidStack = blockTankEntity.getFluidInTank(0);
        tankStack.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        tankStack.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
    }

    /**
     * 用于强制设置方块实体里的液体（前者是被设置的，后者是设置的内容）
     */
    public void setTank(BlockEntity blockEntity, ItemStack stack) {
        //从stack注入blockEntity
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        IFluidHandler blockTankEntity = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
        FluidStack fluidStack = tankStack.getFluidInTank(0);
        blockTankEntity.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        blockTankEntity.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
    }

    /**
     * 使得复制物具有该实体的tank内容
     */
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity == null) return stack;
        setTank(stack, blockEntity);
        return stack;
    }

    /**
     * 让战利品表的同类掉落物存在tank信息
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        ItemStack stack = new ItemStack(this);
        if(blockEntity != null) {
            setTank(stack, blockEntity);
            return Collections.singletonList(stack);
        }
        return super.getDrops(state, builder);
    }

}
