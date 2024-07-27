package cn.solarmoon.idyllic_food_diary.element.matter.cookware.oven;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.baking.BakingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.block_base.BasicEntityBlock;
import cn.solarmoon.solarmoon_core.api.block_use_caller.IBlockUseCaller;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import cn.solarmoon.solarmoon_core.api.phys.VoxelShapeUtil;
import cn.solarmoon.solarmoon_core.api.tile.SyncedEntityBlock;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class OvenBlock extends CookwareBlock {

    public OvenBlock() {
        super(Properties.of()
                .strength(2)
                .sound(SoundType.STONE)
        );
    }

    @Override
    public InteractionResult originUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        OvenBlockEntity oven = (OvenBlockEntity) level.getBlockEntity(pos);
        if (oven == null) return InteractionResult.FAIL;

        if (ItemHandlerUtil.storage(oven.getInventory(), player, hand, 1, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        OvenBlockEntity oven = (OvenBlockEntity) level.getBlockEntity(pos);
        if (oven == null) return;
        if (oven.isOnHeatSource()) {
            BlockPos top = oven.getTopChimney();
            Vec3 center = top.getCenter();
            level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, center.x, center.y - 0.5, center.z, 0, 0.1, 0);
        }
        if (oven.isBaking()) {
            float xInRange = pos.getX() + 4 / 16f + random.nextFloat() * 8 / 16;
            float h = pos.getY() + 0.5F;
            float zInRange = pos.getZ() + 2 / 16f + random.nextFloat() * 12 / 16;
            level.addAlwaysVisibleParticle(ParticleTypes.SMOKE, xInRange, h, zInRange, 0, 0.05, 0);
        }
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        OvenBlockEntity oven = (OvenBlockEntity) blockEntity;
        oven.tryBake();
    }

    @Override
    public VoxelShape getOriginShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        Direction direction = state.getValue(FACING);
        VoxelShape body = Shapes.block();
        VoxelShape empty = Block.box(4, 1, 0, 12, 10, 14);
        return VoxelShapeUtil.rotateShape(direction, Shapes.joinUnoptimized(body, empty, BooleanOp.ONLY_FIRST));
    }

    @Override
    public boolean dropSync() {
        return false;
    }

    @Override
    public boolean canGet() {
        return false;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.OVEN.get();
    }

}
