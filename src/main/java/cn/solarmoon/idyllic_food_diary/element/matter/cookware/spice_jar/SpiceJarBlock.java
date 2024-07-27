package cn.solarmoon.idyllic_food_diary.element.matter.cookware.spice_jar;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.block_use_caller.IBlockUseCaller;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.tile.SyncedEntityBlock;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public class SpiceJarBlock extends CookwareBlock implements IBlockUseCaller {

    public SpiceJarBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS));
    }

    public SpiceJarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult originUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        SpiceJarBlockEntity jar = (SpiceJarBlockEntity) level.getBlockEntity(pos);
        if (jar == null) return InteractionResult.PASS;

        if (ItemHandlerUtil.specialStorage(jar.getInventory(), player, hand)) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public Event.Result getUseResult(BlockState blockState, BlockPos blockPos, Level level, Player player, ItemStack itemStack, BlockHitResult blockHitResult, InteractionHand hand) {
        return Event.Result.ALLOW;
    }

    @Override
    public VoxelShape getOriginShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (player != null && !player.isCrouching()) return null;
        return super.getStateForPlacement(context);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.SPICE_JAR.get();
    }

}
