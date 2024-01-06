package cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks;

import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.entities.TankBlockEntity;
import cn.solarmoon.immersive_delight.common.recipes.KettleRecipe;
import cn.solarmoon.immersive_delight.common.recipes.helper.GetRecipes;
import cn.solarmoon.immersive_delight.compat.create.Create;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
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

/**
 * 基本的烧水壶抽象类
 * 应用tankBlockEntity，是一个液体容器（但是不使其具有原有的液体交互功能，而是替换为倒水功能）
 * 具有烧水、倒水功能
 */
public abstract class WaterKettleEntityBlock extends BasicEntityBlock {

    protected WaterKettleEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        TankBlockEntity tankEntity = (TankBlockEntity) blockEntity;
        ItemStack heldItem = player.getItemInHand(hand);
        if(tankEntity == null) return InteractionResult.FAIL;

        if(getThis(hand, heldItem, player, level, pos ,state)) return InteractionResult.SUCCESS;

        //能够装入液体（不能取）
        if (loadFluid(heldItem, tankEntity, player, level, pos, hand)) return InteractionResult.SUCCESS;
        return InteractionResult.FAIL;
    }

    /**
     * 把forge的流体装取逻辑单独提出来方便万一的新物品对use的修改
     * 总之就是控制液体容器能自动装[入]液体
     */
    public boolean loadFluid(ItemStack heldItem, TankBlockEntity tankEntity, Player player, Level level, BlockPos pos, InteractionHand hand) {
        FluidActionResult result;
        FluidTank tank = tankEntity.tank;
        //存入液体
        result = FluidUtil.tryEmptyContainer(heldItem, tank, Integer.MAX_VALUE, player, true);
        if(result.isSuccess()) {
            if(!player.isCreative()) player.setItemInHand(hand, result.getResult());
            tankEntity.setChanged();
            Create.playPouringSound(tank.getFluid(), level, pos);
            return true;
        }
        return false;
    }

    /**
     * 把方块快速拿到空手里
     */
    public boolean getThis(InteractionHand hand, ItemStack heldItem, Player player, Level level, BlockPos pos, BlockState state) {
        if(hand.equals(InteractionHand.MAIN_HAND) && heldItem.isEmpty() && player.isCrouching()) {
            ItemStack drop = getCloneItemStack(level, pos, state);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            level.playSound(player, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1F, 1F);
            level.playSound(player, pos, SoundEvents.LANTERN_BREAK, SoundSource.PLAYERS, 1F, 1F);
            player.setItemInHand(hand, drop);
            return true;
        }
        return false;
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

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        //防止放入液体时液体值未在客户端同步 而 造成的 假右键操作
        if (blockEntity instanceof TankBlockEntity tankBlockEntity) {
            ClientPackSerializer.sendPacket(pos, new ArrayList<>(), tankBlockEntity.tank.getFluid(), tankBlockEntity.getPersistentData().getInt("PressCount"), "updateCupTank");
        }

        //工作中
        //生成一个最大容积的新液体
        if (blockEntity instanceof TankBlockEntity tankBlockEntity) {
            KettleRecipe kettleRecipe = getCheckedRecipe(level, pos, blockEntity);
            if (kettleRecipe != null) {
                CompoundTag tag = tankBlockEntity.getPersistentData();
                tankBlockEntity.getPersistentData().putInt("Time", tag.getInt("Time")+1);
                Util.deBug("Time："+tag.getInt("Time"), level);
                if (tag.getInt("Time") > kettleRecipe.getTime()) {
                    FluidStack fluidStack = new FluidStack(kettleRecipe.getOutputFluid(), tankBlockEntity.getTankMaxCapacity());
                    tankBlockEntity.tank.setFluid(fluidStack);
                    tag.putInt("Time", 0);
                    tankBlockEntity.setChanged();
                }
            }
        }

    }

    /**
     * 遍历所有配方检测液体是否匹配input且下方为热源
     * 返回匹配的配方
     */
    public KettleRecipe getCheckedRecipe(Level level, BlockPos pos ,BlockEntity blockEntity) {
        if(blockEntity instanceof TankBlockEntity tankBlockEntity) {
            FluidStack fluidStack = tankBlockEntity.tank.getFluid();
            for (KettleRecipe kettleRecipe : GetRecipes.kettleRecipes(level)) {
                if(kettleRecipe.inputMatches(level, fluidStack, pos)) {
                    return kettleRecipe;
                }
            }
        }
        return null;
    }



}
