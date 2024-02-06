package cn.solarmoon.immersive_delight.api.common.entity_block;

import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseTankBlockEntity;
import cn.solarmoon.immersive_delight.api.util.FluidUtil;
import cn.solarmoon.immersive_delight.api.util.namespace.NETList;
import cn.solarmoon.immersive_delight.common.registry.IMPacks;
import cn.solarmoon.immersive_delight.common.registry.IMSounds;
import cn.solarmoon.immersive_delight.compat.create.util.PotionUtil;
import cn.solarmoon.immersive_delight.api.util.namespace.NBTList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class BaseTankEntityBlock extends BasicEntityBlock {

    public BaseTankEntityBlock(Properties properties) {
        super(properties);
    }

    /**
     * 存取设置，0为能存能取，1为只能存不能取，2为只能取不能存，3为不能存取
     */
    public int fluidLoadSetting() {return 0;}

    /**
     * 倒水声设置，默认无
     * 如果为true，将禁用倒水的默认声改为本模组倒水声
     */
    public boolean pouringSound() {return false;}

    /**
     * 强制继承类实现use，因为容器一般需要特殊功能
     */
    @Override
    public abstract InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult);

    /**
     * 把forge的流体装取逻辑单独提出来方便万一的新物品对use的修改
     * 总之就是控制液体容器能自动装取液体
     */
    public boolean loadFluid(BaseTankBlockEntity tankEntity, Player player, Level level, BlockPos pos, InteractionHand hand) {
        if (fluidLoadSetting() == 3) return false;
        ItemStack heldItem = player.getItemInHand(hand);

        FluidActionResult result;
        FluidTank tank = tankEntity.tank;

        //存入液体
        //player填null：不发出声音
        if (fluidLoadSetting() == 0 || fluidLoadSetting() == 1) {
            Player flag = player;
            if (pouringSound()) flag = null;
            result = net.minecraftforge.fluids.FluidUtil.tryEmptyContainer(heldItem, tank, Integer.MAX_VALUE, flag, true);
            if (result.isSuccess()) {
                if (!player.isCreative()) player.setItemInHand(hand, result.getResult());
                tankEntity.setChanged();
                if (pouringSound()) {
                    level.playSound(null, pos, IMSounds.PLAYER_POUR.get(), SoundSource.PLAYERS, 0.8F, 1F);
                } else PotionUtil.playPouringSound(tank.getFluid(), level, pos);
                return true;
            }
        }

        //取出液体
        if (fluidLoadSetting() == 0 || fluidLoadSetting() == 2) {
            result = net.minecraftforge.fluids.FluidUtil.tryFillContainer(heldItem, tank, Integer.MAX_VALUE, player, true);
            if (result.isSuccess()) {
                if (!player.isCreative()) player.setItemInHand(hand, result.getResult());
                IFluidHandlerItem tankStack = FluidUtil.getTank(player.getItemInHand(hand));
                tankEntity.setChanged();
                PotionUtil.playFillingSound(tankStack.getFluidInTank(0), level, pos, player);
                return true;
            }
        }
        return false;
    }

    /**
     * 默认进行流体的同步
     */
    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        //防止放入液体时液体值和物品未在客户端同步 而 造成的 假右键操作
        if (blockEntity instanceof BaseTankBlockEntity t) {
            CompoundTag tag = new CompoundTag();
            tag.put(NBTList.FLUID, t.tank.writeToNBT(new CompoundTag()));
            IMPacks.CLIENT_PACK.getSender().send(NETList.SYNC_T_BLOCK, pos, tag);
        }
    }

    /**
     * 使得放置物具有手上物品tank的读取
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity == null) return;
        FluidUtil.setTank(blockEntity, stack);
    }

    /**
     * 使得复制物具有该实体的tank内容
     */
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity == null) return stack;
        FluidUtil.setTank(stack, blockEntity);
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
            FluidUtil.setTank(stack, blockEntity);
            return Collections.singletonList(stack);
        }
        return super.getDrops(state, builder);
    }

    /**
     * 设定红石信号逻辑
     * 按液体比例输出信号
     */
    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BaseTankBlockEntity t) {
            return (int) (FluidUtil.getScale(t.tank) * 15);
        }
        return 0;
    }

}
