package cn.solarmoon.idyllic_food_diary.common.block.base;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.solarmoon_core.api.util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.HashMap;

/**
 * 基本的拿取类型的食物
 * 可选拿取所需的物品
 * 根据拿取数改变形态
 */
public abstract class AbstractObtainableFoodBlock extends BaseRemainBlock {

    public AbstractObtainableFoodBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (getThis(player, level, pos, state, hand, true)) {
            return InteractionResult.SUCCESS;
        }

        ItemStack heldItem = player.getItemInHand(hand);
        int remain = state.getValue(REMAIN);
        if (remain > 0) {
            if (heldItem.is(getContainer())) {

                BlockState targetState = state.setValue(REMAIN, remain - 1);

                level.setBlock(pos, targetState, 3);

                if (targetState.getValue(REMAIN) == 0) {
                    BlockState stateTo = getBlockLeft().defaultBlockState();
                    BlockUtil.replaceBlockWithAllState(state, stateTo, level, pos);
                }

                if (!getContainer().equals(Items.AIR)) heldItem.shrink(1);

                //给予物品逻辑，可设定具体值所获取的物品
                if (!setSpecialTakenMap().isEmpty()) {
                    for (var entry : setSpecialTakenMap().entrySet()) {
                        if (remain == entry.getKey()) LevelSummonUtil.addItemToInventory(player, entry.getValue());
                        else LevelSummonUtil.addItemToInventory(player, getFood());
                    }
                } else LevelSummonUtil.addItemToInventory(player, getFood());

                level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS);

                return InteractionResult.SUCCESS;
            } else if (getContainer() == Items.AIR) player.displayClientMessage(IdyllicFoodDiary.TRANSLATOR.set("message", "container_required_empty"), true);
            else player.displayClientMessage(IdyllicFoodDiary.TRANSLATOR.set("message", "container_required", getContainer().getDefaultInstance().getHoverName()), true);
        }
        return InteractionResult.PASS;
    }

    public boolean getThis(Player player, Level level, BlockPos pos, BlockState state, InteractionHand hand, boolean defaultSound) {
        ItemStack heldItem = player.getItemInHand(hand);
        if(hand.equals(InteractionHand.MAIN_HAND) && heldItem.isEmpty() && player.isCrouching()) {
            ItemStack copy = getCloneItemStack(level, pos, state);
            boolean flag = BlockUtil.removeDoubleBlock(level, pos);
            if (!flag) level.removeBlock(pos, false);
            if (defaultSound) {
                level.playSound(player, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1F, 1F);
            }
            player.setItemInHand(hand, copy);
            return true;
        }
        return false;
    }

    /**
     * @return 决定每次右击所获取的物品
     */
    public abstract ItemStack getFood();

    /**
     * @return 决定食用所需容器 (空手填入Items.AIR)
     */
    public abstract Item getContainer();

    /**
     * @return 决定吃完后留下的方块
     */
    public abstract Block getBlockLeft();

    /**
     * @return 设定具体remain数所能对应拿取的具体物品（有需要的话则改）
     */
    public HashMap<Integer, ItemStack> setSpecialTakenMap() {
        return new HashMap<>();
    }

    /**
     * 只能生存在有足够碰撞箱的方块上
     */
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    /**
     * 红石信号规则：根据剩余可拿取食物比例数减小
     * 也就是放出来满格，越拿越少
     */
    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return (int) ((state.getValue(REMAIN) / (float) getMaxRemain()) * 15);
    }

}
