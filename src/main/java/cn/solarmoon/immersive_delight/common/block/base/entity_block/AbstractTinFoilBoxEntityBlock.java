package cn.solarmoon.immersive_delight.common.block.base.entity_block;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractTinFoilBoxBlockEntity;
import cn.solarmoon.immersive_delight.common.recipe.TinFoilGrillingRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import cn.solarmoon.immersive_delight.util.BlockUtil;
import cn.solarmoon.solarmoon_core.common.block.entity_block.BaseContainerEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractTinFoilBoxEntityBlock extends BaseContainerEntityBlock {

    public AbstractTinFoilBoxEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        AbstractTinFoilBoxBlockEntity foilBox = (AbstractTinFoilBoxBlockEntity) level.getBlockEntity(pos);
        if (foilBox == null) return InteractionResult.PASS;

        if (getThis(player, level, pos, state, hand)) {
            return InteractionResult.SUCCESS;
        }

        if (storage(foilBox, player, hand)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1.2f);
            foilBox.setChanged();
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof AbstractTinFoilBoxBlockEntity tinFoil) {
            TinFoilGrillingRecipe recipe = tinFoil.getCheckedRecipe(level, pos);
            if (recipe != null) {
                tinFoil.setRecipeTime(recipe.time());
                tinFoil.setTime(tinFoil.getTime() + 1);
                if (tinFoil.getTime() >= tinFoil.getRecipeTime()) {
                    BlockState outputState = recipe.getOutput();
                    if (outputState != null) {
                        //使朝向一致
                        BlockUtil.setBlockWithDirection(state, outputState, level, pos);
                        tinFoil.setTime(0);
                        tinFoil.setRecipeTime(0);
                    }
                }
            } else {
                tinFoil.setTime(0);
                tinFoil.setRecipeTime(0);
            }
        }
        super.tick(level, pos, state, blockEntity);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.TIN_FOIL_BOX.get();
    }

}
