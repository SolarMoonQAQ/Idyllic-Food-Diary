package cn.solarmoon.idyllic_food_diary.api.common.block.entity_block;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.util.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.CupBlockEntity;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.CupRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMSounds;
import cn.solarmoon.idyllic_food_diary.core.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.idyllic_food_diary.core.data.tags.IMFluidTags;
import cn.solarmoon.solarmoon_core.api.common.block.entity_block.BasicEntityBlock;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.player.CountingDevice;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;
import java.util.Objects;


/**
 * 作为大部分可饮用方块（如各类杯子）的抽象类<br/>
 * 液体只能存不能取（可以倒不能拿）
 */
public abstract class AbstractCupEntityBlock extends BasicEntityBlock {

    protected AbstractCupEntityBlock(Properties properties) {
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

        //空手shift+右键快速拿杯子
        if(getThis(player, level, pos, state, hand, true)) return InteractionResult.SUCCESS;

        //计数装置
        CountingDevice counting = CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA).getCountingDevice();
        counting.setCount(counting.getCount() + 1, pos);
        if(canEat(blockEntity, player)) {
            IdyllicFoodDiary.DEBUG.send("点击次数：" + counting.getCount());
        }

        //只能存不能取液体
        if (cup.putFluid(player, hand, false)) {
            level.playSound(null, pos, IMSounds.PLAYER_POUR.get(), SoundSource.PLAYERS, 0.8F, 1F);
            cup.setChanged();
            return InteractionResult.SUCCESS;
        }

        //存取任意单个物品，需要匹配配方的输入才允许输入
        List<CupRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.CUP.get());
        for (var recipe :recipes) {
            Ingredient ingredient = recipe.ingredient();
            if (ingredient.test(heldItem)) {
                if (cup.putItem(player, hand, 1)) {
                    level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.2f, 1f);
                    blockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (cup.takeItem(player, hand, 1)) {
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
            if (tankAmount >= FarmerUtil.getDrinkVolume(level, tank.getFluid())) {
                //能吃却不能吃 不让用
                if(!canEat(blockEntity, player)) return InteractionResult.PASS;
                if(counting.getCount() >= getPressCount()) {
                    FarmerUtil.commonDrink(tank.getFluid(), level, player, true);
                    tank.drain(FarmerUtil.getDrinkVolume(level, tank.getFluid()), IFluidHandler.FluidAction.EXECUTE);
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

    /**
     * @return 设置需要右键多少下喝一下（默认2）
     */
    public int getPressCount() {
        return 2;
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

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        CupBlockEntity cup = (CupBlockEntity) blockEntity;
        cup.tryMakeTea();
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

}
