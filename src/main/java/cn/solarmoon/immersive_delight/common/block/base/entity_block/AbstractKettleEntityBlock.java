package cn.solarmoon.immersive_delight.common.block.base.entity_block;

import cn.solarmoon.immersive_delight.common.block.entity_block.KettleEntityBlock;
import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractKettleBlockEntity;
import cn.solarmoon.immersive_delight.common.recipe.KettleRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMSounds;
import cn.solarmoon.immersive_delight.data.tags.IMFluidTags;
import cn.solarmoon.immersive_delight.util.FarmerUtil;
import cn.solarmoon.solarmoon_core.common.block.entity_block.BaseTankEntityBlock;
import cn.solarmoon.solarmoon_core.common.block_entity.BaseTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

/**
 * 基本的烧水壶抽象类
 * 应用tankBlockEntity，是一个液体容器（但是不使其具有原有的液体交互功能，而是替换为倒水功能）
 * 具有烧水、倒水功能
 */
public abstract class AbstractKettleEntityBlock extends BaseTankEntityBlock {


    protected AbstractKettleEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        BaseTankBlockEntity tankEntity = (BaseTankBlockEntity) blockEntity;
        if(tankEntity == null) return InteractionResult.PASS;

        if(getThis(player, level, pos ,state, hand)) {
            level.playSound(null, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS);
            tankEntity.setChanged();
            return InteractionResult.SUCCESS;
        }

        //能够装入液体（不能取）
        if (putFluid(tankEntity, player, hand, false)) {
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        AbstractKettleBlockEntity kettle = (AbstractKettleBlockEntity) blockEntity;
        //工作中
        //生成一个匹配生成前容积的新液体
        kettle.tryBoilWater();
        //里面是hot类型的液体就冒热气
        makeParticle(level, pos, state, blockEntity);
    }

    /**
     * 烧水粒子
     */
    public void makeParticle(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        BlockState stateBelow = level.getBlockState(pos.below());
        boolean isHeated = FarmerUtil.isHeatSource(stateBelow);

        Direction face = state.getValue(KettleEntityBlock.FACING);
        float delta1 = -face.getStepZ() * 0.5f;
        float delta2 = face.getStepX() * 0.5f;
        Vec3 spoutPos = pos.getCenter().add(delta1, 0.07, delta2);

        if (blockEntity instanceof BaseTankBlockEntity tankBlockEntity) {
            FluidStack fluidStack = tankBlockEntity.getTank().getFluid();

            if (!fluidStack.isEmpty() && isHeated) {
                Random random = new Random();
                if (fluidStack.getFluid().defaultFluidState().is(IMFluidTags.HOT_FLUID)) {
                    if (random.nextFloat() < 0.8) {
                        level.addParticle(ParticleTypes.CLOUD, spoutPos.x, spoutPos.y, spoutPos.z, 0, 0.1, 0);
                    }
                    if (random.nextFloat() < 0.1) {
                        level.playSound(null, pos, IMSounds.BOILING_WATER.get(), SoundSource.BLOCKS, 1F, 1F);
                    }
                }
                else {
                    if (random.nextFloat() < 0.1)
                        level.addParticle(ParticleTypes.CLOUD, spoutPos.x, spoutPos.y, spoutPos.z, 0, 0.01, 0);
                }
            }

        }
    }

}
