package cn.solarmoon.idyllic_food_diary.element.matter.stove;

import cn.solarmoon.solarmoon_core.api.block_base.BaseBlock;
import cn.solarmoon.solarmoon_core.api.block_util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import cn.solarmoon.solarmoon_core.api.phys.VecUtil;
import cn.solarmoon.solarmoon_core.api.util.DropUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;

public class StoveBlock extends BaseBlock implements ILitBlock, IHorizontalFacingBlock {

    public StoveBlock() {
        super(Properties.of()
                .sound(SoundType.STONE)
                .strength(1.5f)
                .lightLevel((state) -> state.getValue(LIT) ? 13 : 0)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);

        //打火石等点燃和熄灭
        if (controlLitByHand(state, pos, level, player, hand)) {
            return InteractionResult.SUCCESS;
        }

        // 放入可镶嵌厨具
        if (VecUtil.isInside(hitResult.getLocation(), pos, 2 / 16f, 10 / 16f, 2 / 16f, 14 / 16f, 16 / 16f, 14 / 16f, true)) {
            Block cookware = Block.byItem(heldItem.getItem());
            if (cookware instanceof IBuiltInStove) {
                BlockState d = cookware.getStateForPlacement(new BlockPlaceContext(player, hand, heldItem, hitResult));
                if (d != null) {
                    BlockUtil.replaceBlockWithAllState(state, d.setValue(IBuiltInStove.NESTED_IN_STOVE, true), level, pos);
                    cookware.setPlacedBy(level, pos, level.getBlockState(pos), player, heldItem);
                    level.playSound(null, pos, d.getSoundType().getPlaceSound(), SoundSource.BLOCKS);
                    if (!player.isCreative()) heldItem.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

}
