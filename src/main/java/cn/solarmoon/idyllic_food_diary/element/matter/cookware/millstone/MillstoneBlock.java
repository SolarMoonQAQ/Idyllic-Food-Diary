package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.api.AnimTicker;
import cn.solarmoon.idyllic_food_diary.api.Timer;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.ParticleSpawner;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.block_use_caller.IBlockUseCaller;
import cn.solarmoon.solarmoon_core.api.phys.VoxelShapeUtil;
import cn.solarmoon.solarmoon_core.api.tile.fluid.FluidHandlerUtil;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import cn.solarmoon.solarmoon_core.api.util.DropUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.Event;

public class MillstoneBlock extends CookwareBlock implements IBlockUseCaller {

    public MillstoneBlock() {
        super(Properties.of()
                .sound(SoundType.STONE)
                .strength(2.5f)
                .noOcclusion()
        );
    }

    @Override
    public InteractionResult originUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        MillstoneBlockEntity mill = (MillstoneBlockEntity) level.getBlockEntity(pos);
        if (mill == null) return InteractionResult.FAIL;

        if (FluidHandlerUtil.loadFluid(mill.getTank(0), player, hand, true)) {
            return InteractionResult.SUCCESS;
        }

        // 蹲下才能手抽物品，且必须空手
        if (!player.isCrouching()) {
            if (handle(mill)) return InteractionResult.SUCCESS;
        } else if (handleInvAct(mill, player)) return InteractionResult.SUCCESS;

        return InteractionResult.FAIL;
    }

    private static boolean handle(MillstoneBlockEntity mill) {
        if (!mill.isRecipeSmooth()) return false;
        AnimHelper.getMap(mill).get("rotation").getTimer().start();
        return true;
    }

    private static boolean handleInvAct(MillstoneBlockEntity mill, Player player) {
        for (int i = mill.inventory.getSlots() - 1; i >= 0; i--) {
            if (!mill.inventory.realExtract(i, 64, true).isEmpty()) {
                DropUtil.addItemToInventory(player, mill.inventory.realExtract(i, 64, false));
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        MillstoneBlockEntity mill = (MillstoneBlockEntity) blockEntity;
        mill.tryGrind();

        // 空气漏斗
        level.getEntities(null, new AABB(pos.above()).setMaxY(pos.above().getY() + 1 / 16f)).forEach(entity -> {
            if (entity instanceof ItemEntity item) {
                if (!ItemHandlerUtil.insertItem(mill.inventory, item.getItem()).equals(item.getItem(), false)) {
                    item.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        });

        // 传递输出液体
        mill.transferOfFluid();

        //————————————————————————————————————————动画————————————————————————————————————————————————//
        AnimTicker animRot = AnimHelper.getMap(mill).get("rotation");
        AnimTicker animFlow = AnimHelper.getMap(mill).get("flow");
        Timer timerRot = animRot.getTimer();

        // 声音和粒子
        if (timerRot.getTiming()) {
            ItemStack in = mill.getInventory().getStackInSlot(0);
            int pt = animRot.getFixedValues().getOrDefault("particle", 0f).intValue();
            if (!in.isEmpty() && pt % 2 == 0) {
                ParticleSpawner.rolling(pos, level, in, 3/16d, 1);
                float scale = animRot.getFixedValues().getOrDefault("velocity", 0f);
                level.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.5F * scale, 0.5F);
            }
            pt++;
            if (pt > 5) {
                pt = 0;
            }
            animRot.getFixedValues().put("particle", (float) pt);
        }

        // 旋转角
        if (timerRot.getTiming()) {
            float v = (timerRot.getMaxTime() - timerRot.getTime()) / 20;
            v = Math.min(v, 1) * 3; // 最大tick是30，留10tick，期间不减速作为启动缓冲
            animRot.getFixedValues().put("rot", animRot.getFixedValues().getOrDefault("rot", 0f) + v);
            animRot.getFixedValues().put("velocity", v);
        }

        // 液体类水管下流动画
        int flowMaxTick = 10;
        int tick = animFlow.getFixedValues().getOrDefault("flow", 0f).intValue();
        if (mill.isFlowing() && tick < flowMaxTick) {
            tick++;
        } else if (!mill.isFlowing() && tick > 0) {
            tick--;
        }
        animFlow.getFixedValues().put("flow", (float) tick);
        animFlow.getFixedValues().put("flowMax", (float) flowMaxTick);

        // 默认液体动画存储
        AnimHelper.Fluid.onFluidAnimStop(mill, mill.getTank(1).getFluid());
    }

    @Override
    public VoxelShape getOriginShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        VoxelShape body = box(3, 1, 3, 13, 9, 13);
        VoxelShape trench = Shapes.joinUnoptimized(
                box(1, 1, 1, 15, 3, 15),
                box(3, 1, 3, 13, 3, 13),
                BooleanOp.ONLY_FIRST
        );
        VoxelShape bottom = box(0, 0, 0, 16, 3, 16);
        VoxelShape rB = Shapes.joinUnoptimized(bottom, trench, BooleanOp.ONLY_FIRST);
        VoxelShape r = Shapes.or(rB, box(6, 0, -3, 10, 3, 1));
        VoxelShape o = Shapes.joinUnoptimized(r, box(7, 1, -3, 9, 3, 1), BooleanOp.ONLY_FIRST);
        return VoxelShapeUtil.rotateShape(direction, Shapes.or(o, body));
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.MILLSTONE.get();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public Event.Result getUseResult(BlockState blockState, BlockPos pos, Level level, Player player, ItemStack itemStack, BlockHitResult blockHitResult, InteractionHand hand) {
        MillstoneBlockEntity mill = (MillstoneBlockEntity) level.getBlockEntity(pos);
        ItemStack heldItem = player.getItemInHand(hand);
        if (mill == null) return Event.Result.DEFAULT;

        // 蹲下先取出而不是转盘
        if (player.isCrouching() && heldItem.isEmpty()) return Event.Result.ALLOW;

        return Event.Result.DEFAULT;
    }

    @Override
    public boolean dropSync() {
        return false;
    }

    @Override
    public boolean canGet() {
        return false;
    }

}
