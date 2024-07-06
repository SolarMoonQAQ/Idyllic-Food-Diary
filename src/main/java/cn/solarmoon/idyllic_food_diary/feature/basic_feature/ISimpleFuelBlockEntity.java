package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;

import static cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock.LIT;

public interface ISimpleFuelBlockEntity {

    String BURNING_TIME = "BurningTime";
    String BURNING_TIME_SAVING = "BurningTimeSaving";

    default BlockEntity sf() {
        return (BlockEntity) this;
    }

    /**
     * @return 返回要消耗和计算的燃料
     */
    ItemStack getFuel();

    default void tryControlLit() {
        BlockState state = sf().getBlockState();
        BlockPos pos = sf().getBlockPos();
        Level level = sf().getLevel();
        //消耗煤炭，控制lit属性
        if (state.getValue(LIT)) {
            //有燃料就保存其燃烧时间，并且消耗一个
            if (!noFuel() && getFuelTime() == 0) {
                setFuelTime(ForgeHooks.getBurnTime(getFuel(), null));
                getFuel().shrink(1);
                sf().setChanged();
            }
            setBurnTime(getBurnTime() + 1);
            //燃烧时间超过燃料所能提供，就刷新
            if (getBurnTime() >= getFuelTime()) {
                setBurnTime(0);
                setFuelTime(0);
            }
            //无燃料且没有燃烧时间了就停止lit
            if (noFuel() && getBurnTime() == 0) {
                if (level != null) {
                    level.setBlock(pos, state.setValue(LIT, false), 3);
                }
                sf().setChanged();
            }
        }
    }

    /**
     * 手动点燃燃料
     * @return 成功返回true
     */
    default boolean litByHand(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        BlockPos pos = sf().getBlockPos();
        BlockState state = sf().getBlockState();
        Level level = sf().getLevel();
        if (level == null) return false;
        //打火石等点燃
        if (!state.getValue(LIT) && !getFuel().isEmpty()) {
            if (heldItem.getItem() instanceof FlintAndSteelItem) {
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                heldItem.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(hand));
                return true;
            }
        }
        return false;
    }

    default boolean isBurning() {
        BlockState state = sf().getBlockState();
        return state.getValue(LIT);
    }

    default boolean noFuel() {
        return getFuel().isEmpty();
    }

    int getBurnTime();

    void setBurnTime(int time);

    int getFuelTime();

    void setFuelTime(int time);

}
