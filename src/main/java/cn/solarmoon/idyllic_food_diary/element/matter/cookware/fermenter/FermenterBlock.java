package cn.solarmoon.idyllic_food_diary.element.matter.cookware.fermenter;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.fluid.FluidHandlerUtil;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FermenterBlock extends CookwareBlock {

    public FermenterBlock() {
        super(Properties.of()
                .sound(SoundType.DECORATED_POT)
                .strength(2.0f)
        );
    }

    public FermenterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult originUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        FermenterBlockEntity f = (FermenterBlockEntity) level.getBlockEntity(pos);
        if (f == null) return InteractionResult.FAIL;

        if (FluidHandlerUtil.loadFluid(f.getTank(), player, hand, true)) {
            return InteractionResult.SUCCESS;
        }

        if (ItemHandlerUtil.storage(f.getInventory(), player, hand, 1, 1)) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        FermenterBlockEntity f = (FermenterBlockEntity) blockEntity;
        f.tryFerment();
    }

    @Override
    public VoxelShape getOriginShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.FERMENTER.get();
    }

}
