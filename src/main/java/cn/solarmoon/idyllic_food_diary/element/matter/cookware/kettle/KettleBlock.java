package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock;
import cn.solarmoon.idyllic_food_diary.registry.common.IMParticles;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMSounds;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.tile.fluid.FluidHandlerUtil;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * 基本的烧水壶抽象类
 * 应用tankBlockEntity，是一个液体容器（但是不使其具有原有的液体交互功能，而是替换为倒水功能）
 * 具有烧水、倒水功能
 */
public class KettleBlock extends CookwareBlock {

    public KettleBlock() {
        super(Properties.of()
                .sound(SoundType.LANTERN)
                .strength(2f, 6.0F)
                .mapColor(MapColor.METAL)
                .forceSolidOn()
        );
    }

    @Override
    public InteractionResult originUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        KettleBlockEntity kettle = (KettleBlockEntity) blockEntity;
        if(kettle == null) return InteractionResult.PASS;

        //液体交互
        if (FluidHandlerUtil.loadFluid(kettle.getTank(), player, hand, false)) {
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        }
        if (hand == InteractionHand.MAIN_HAND && ItemHandlerUtil.storage(kettle.getInventory(), player, hand, 1, 1)) {
            level.playSound(null, pos, SoundEvents.LANTERN_HIT, SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        KettleBlockEntity kettle = (KettleBlockEntity) blockEntity;

        if (!kettle.tryBrewTea()) kettle.tryBoilWater();

        //里面是hot类型的液体就冒热气
        makeBoilParticle(level, pos, state, blockEntity);
    }

    /**
     * 烧水粒子
     */
    protected static void makeBoilParticle(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        Direction face = state.getValue(KettleBlock.FACING);
        float delta1 = -face.getStepZ() * 0.5f;
        float delta2 = face.getStepX() * 0.5f;
        Vec3 spoutPos = pos.getCenter().add(delta1, 0.07, delta2);

        if (blockEntity instanceof KettleBlockEntity kettle) {
            Random random = new Random();
            if (kettle.isInBoil()) {
                if (random.nextFloat() < 0.8) {
                    level.addParticle(IMParticles.CRASHLESS_CLOUD.get(), spoutPos.x, spoutPos.y, spoutPos.z, 0, 0.1, 0);
                }
                if (random.nextFloat() < 0.1) {
                    level.playSound(null, pos, IMSounds.BOILING_WATER.get(), SoundSource.BLOCKS, 1F, 1F);
                }
            }
            else if (kettle.isBoiling()) {
                if (random.nextFloat() < 0.1)
                    level.addParticle(IMParticles.CRASHLESS_CLOUD.get(), spoutPos.x, spoutPos.y, spoutPos.z, 0, 0.01, 0);
            }

        }
    }

    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 7.0D, 12.0D);
    @Override
    public @NotNull VoxelShape getOriginShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.KETTLE.get();
    }

}
