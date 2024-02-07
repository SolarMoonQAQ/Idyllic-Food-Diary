package cn.solarmoon.immersive_delight.common.entity_block.core;

import cn.solarmoon.immersive_delight.api.common.entity_block.BaseTankEntityBlock;
import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseTankBlockEntity;
import cn.solarmoon.immersive_delight.api.util.RecipeUtil;
import cn.solarmoon.immersive_delight.common.entity_block.KettleEntityBlock;
import cn.solarmoon.immersive_delight.common.recipes.KettleRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.common.registry.IMSounds;
import cn.solarmoon.immersive_delight.data.tags.IMFluidTags;
import cn.solarmoon.immersive_delight.util.FarmerUtil;
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
    public int fluidLoadSetting() {return 1;}

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        BaseTankBlockEntity tankEntity = (BaseTankBlockEntity) blockEntity;
        if(tankEntity == null) return InteractionResult.PASS;

        if(getThis(player, level, pos ,state, hand)) {
            level.playSound(null, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }

        //能够装入液体（不能取）
        if (loadFluid(tankEntity, player, level, pos, hand)) return InteractionResult.SUCCESS;
        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);

        //工作中
        //生成一个最大容积的新液体
        if (blockEntity instanceof BaseTankBlockEntity tankBlockEntity) {
            KettleRecipe kettleRecipe = getCheckedRecipe(level, pos, blockEntity);
            if (kettleRecipe != null) {
                tankBlockEntity.recipeTime = kettleRecipe.getTime();
                tankBlockEntity.time++;
                if (tankBlockEntity.time > kettleRecipe.getTime()) {
                    FluidStack fluidStack = new FluidStack(kettleRecipe.getOutputFluid(), tankBlockEntity.maxCapacity);
                    tankBlockEntity.tank.setFluid(fluidStack);
                    tankBlockEntity.time = 0;
                    tankBlockEntity.setChanged();
                }
            } else {
                tankBlockEntity.time = 0;
                tankBlockEntity.recipeTime = 0;
            }
        }

        makeParticle(level, pos, state, blockEntity);

    }

    /**
     * 烧水粒子
     */
    public void makeParticle(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        BlockState stateBelow = level.getBlockState(pos.below());
        boolean isHeated = FarmerUtil.isHeatSource(stateBelow);
        Direction face = state.getValue(KettleEntityBlock.FACING);
        float delta1 = 0.5f;
        float delta2 = 0.5f;
        if (face.equals(Direction.NORTH)) {delta1 = 0.5f; delta2 = 0f;}
        if (face.equals(Direction.SOUTH)) {delta1 = -0.5f; delta2 = 0f;}
        if (face.equals(Direction.EAST)) {delta1 = 0f; delta2 = 0.5f;}
        if (face.equals(Direction.WEST)) {delta1 = 0f; delta2 = -0.5f;}
        Vec3 spoutPos = pos.getCenter().add(delta1, 0.07, delta2);

        if (blockEntity instanceof BaseTankBlockEntity tankBlockEntity) {
            FluidStack fluidStack = tankBlockEntity.tank.getFluid();

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

    /**
     * 遍历所有配方检测液体是否匹配input且下方为热源
     * 返回匹配的配方
     */
    public KettleRecipe getCheckedRecipe(Level level, BlockPos pos ,BlockEntity blockEntity) {
        if(blockEntity instanceof BaseTankBlockEntity tankBlockEntity) {
            FluidStack fluidStack = tankBlockEntity.tank.getFluid();
            for (KettleRecipe kettleRecipe : RecipeUtil.getRecipes(level, IMRecipes.KETTLE_RECIPE.get())) {
                if(kettleRecipe.inputMatches(level, fluidStack, pos)) {
                    return kettleRecipe;
                }
            }
        }
        return null;
    }

}
