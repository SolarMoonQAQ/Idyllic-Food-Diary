package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.data.IMFluidTags;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.BaseCookwareBlock;
import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.TeaBrewingUtil;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMSounds;
import cn.solarmoon.solarmoon_core.api.blockentity_base.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.api.capability.CountingDevice;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * 作为大部分可饮用方块（如各类杯子）的抽象类<br/>
 * 液体只能存不能取（可以倒不能拿）
 */
public class CupBlock extends BaseCookwareBlock {

    public CupBlock() {
        super(Block.Properties.of()
                .sound(SoundType.GLASS)
                .strength(0.5f)
                .noOcclusion());
    }

    public CupBlock(Properties properties) {
        super(properties);
    }

    /**
     * 使得该类方块能够与液体桶类物品进行液体交互
     * 使得该类方块能够每几下被喝走一定数量的液体
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        CupBlockEntity cup = (CupBlockEntity) blockEntity;
        ItemStack heldItem = player.getItemInHand(hand);
        if(cup == null) return InteractionResult.FAIL;
        FluidTank tank = cup.getTank();

        //计数装置
        CountingDevice counting = player.getCapability(SolarCapabilities.PLAYER_DATA).orElse(null).getCountingDevice();
        counting.setCount(counting.getCount() + 1, pos);
        if(TeaBrewingUtil.canEat(tank.getFluid(), player)) {
            IdyllicFoodDiary.DEBUG.send("点击次数：" + counting.getCount());
        }

        //只能存不能取液体
        if (cup.putFluid(player, hand, false)) {
            level.playSound(null, pos, IMSounds.PLAYER_POUR.get(), SoundSource.PLAYERS, 0.8F, 1F);
            cup.setChanged();
            return InteractionResult.SUCCESS;
        }

        if (cup.storage(player, hand, 1, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.2f, 1f);
            blockEntity.setChanged();
            return InteractionResult.SUCCESS;
        }

        /*喝！
        默认每两下喝一下，消耗默认50，小于50则全喝完不触发任何效果
        喝时触发效果：声音、余下同item
         */
        else {
            int tankAmount = tank.getFluidAmount();
            if (tankAmount >= TeaBrewingUtil.getDrinkVolume(level, tank.getFluid())) {
                //能吃却不能吃 不让用
                if(!TeaBrewingUtil.canEat(tank.getFluid(), player)) return InteractionResult.PASS;
                if(counting.getCount() >= getPressCount()) {
                    TeaBrewingUtil.commonDrink(tank.getFluid(), player, true);
                    tank.drain(TeaBrewingUtil.getDrinkVolume(level, tank.getFluid()), IFluidHandler.FluidAction.EXECUTE);
                    cup.setChanged();
                    counting.resetCount();
                    IdyllicFoodDiary.DEBUG.send("已有tag：" + blockEntity.getPersistentData());
                    if(!level.isClientSide) level.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1F, 1F);
                }
                return InteractionResult.SUCCESS;
            } else if (tankAmount > 0) {
                if(counting.getCount() >= getPressCount()) {
                    tank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                    cup.setChanged();
                    counting.resetCount();
                    if(!level.isClientSide) level.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1F, 1F);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        getThis(player, level, pos, state, InteractionHand.MAIN_HAND, true);
        super.attack(state, level, pos, player);
    }

    /**
     * @return 设置需要右键多少下喝一下（默认2）
     */
    public int getPressCount() {
        return 2;
    }

    /**
     * shift才能放置
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if(!Objects.requireNonNull(context.getPlayer()).isCrouching()) return null;
        return super.getStateForPlacement(context);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        CupBlockEntity cup = (CupBlockEntity) blockEntity;
        if (!cup.needHeating()) cup.tryBrewTea(); // 只能泡无需加热的茶
    }

    /**
     * 热气粒子
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BaseTCBlockEntity cup) {
            Vec3 spoutPos = pos.getCenter();
            FluidStack fluidStack = cup.getTank().getFluid();
            if(!fluidStack.isEmpty() && fluidStack.getFluid().defaultFluidState().is(IMFluidTags.WARM_FLUID)) {
                if (random.nextInt(5) == 3)
                    level.addParticle(ParticleTypes.CLOUD, spoutPos.x, spoutPos.y, spoutPos.z, 0, 0.01, 0);
            }
        }
    }

    protected static final VoxelShape[] SHAPE = new VoxelShape[]{
            Block.box(6.5D, 0.0D, 6.5D, 9.5D, 0.5D, 9.5D),
            Block.box(6.0D, 0.5D, 6.0D, 10.0D, 4.5D, 10.0D)
    };
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.or(SHAPE[0], SHAPE[1]);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.LITTLE_CUP.get();
    }

}
